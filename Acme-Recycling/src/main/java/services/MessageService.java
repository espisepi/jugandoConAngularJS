package services;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Service
@Transactional
public class MessageService {
	
	// Managed repository -----------------------------------------------------
	@Autowired
	private MessageRepository messageRepository;
	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	private MessageFolderService messageFolderService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private TabooWordService tabooWordService;
	
	@Autowired
	private Validator	validator;
	
	// Constructors -----------------------------------------------------------
	
	public MessageService(){
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	
	public Message create() {
		Actor sender;
		Message result;
		MessageFolder messageFolder;
		
		sender = this.actorService.findPrincipal();
		result = new Message();
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setSender(sender);
		messageFolder = this.messageFolderService.findMessageFolderByNameAndActor("Out box", sender.getId());
		result.setMessageFolder(messageFolder);
		
		return result;
	}
	
	public Collection<Message> findAll() {
		Collection<Message> result;
		result = this.messageRepository.findAll();
		return result;

	}
	
	public Message findOne(int messageId) {
		
		Assert.notNull(messageId);
		Assert.isTrue(messageId != 0);
		
		Message result;
		Actor principal;
		Actor sender;
		Actor recipient;
		
		principal = this.actorService.findPrincipal();
		
		result = this.messageRepository.findOne(messageId);
		sender = result.getSender();
		recipient = result.getRecipient();
		
		
		Assert.isTrue((principal.equals(sender) || principal.equals(recipient)));
		
		return result;
		
		
	}
	
	//Send-----------------------------------------------------
	
	public Message send(Message message) {
		
		Assert.notNull(message);
		
		Date moment;
		Actor sender;
		Actor recipient;
		Message messageSaved;
		Message messageRecipient;
		Actor principal;
		
		principal = this.actorService.findPrincipal();
		moment = new Date(System.currentTimeMillis() - 1000);
		sender = message.getSender();
		recipient = message.getRecipient();
		
		message.setMoment(moment);
		
		Assert.isTrue(principal.equals(sender));
		
		messageSaved = this.messageRepository.save(message);
		messageRecipient = this.messageRepository.save(message);
		
		
		if(this.checkSpamMessage(messageRecipient)==true)
			this.saveMessageInFolder(recipient, "Spam box", messageRecipient);
		else
			this.saveMessageInFolder(recipient, "In box", messageRecipient);
		
		this.saveMessageInFolder(sender, "Out box", messageSaved);
		
		
		Assert.notNull(messageSaved);
		
		return messageRecipient;
		
	}
	
	public void save(Message message) {
		
		Assert.notNull(message);
		
		this.messageRepository.save(message);
		
	}
	
	//Delete----------------------------------------------------
	
	public void delete(Message message){
		Assert.notNull(message);
		Assert.isTrue(message.getId() != 0);
		
		Actor principal;
		Actor sender;
		Actor recipient;
		Collection<MessageFolder> messageFolders;
		
		principal = this.actorService.findPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(principal.getId());
		sender = message.getSender();
		recipient = message.getRecipient();
		
		Assert.isTrue((principal.equals(sender) || principal.equals(recipient)) && messageFolders.contains(message.getMessageFolder()));
		
		if(message.getMessageFolder().getName().equals("Trash box"))
			
			this.messageRepository.delete(message);
		
		else
		
			this.saveMessageInFolder(principal, "Trash box", message);
			
	}
	
	//Other method-------------------------------------------------------------
	
	public Collection<Message> findMessagesByMessageFolder(int messageFolderId){
		
		Collection<Message> result;
		
		result = this.messageRepository.findMessagesByMessageFolder(messageFolderId);
		
		return result;
	}
	
	public void saveMessageInFolder(Actor actor, String folderName, Message message){
		
		MessageFolder messageFolder;
		
		messageFolder = this.messageFolderService.findMessageFolderByNameAndActor(folderName, actor.getId());
		
		message.setMessageFolder(messageFolder);
		
	}
	
	public Message createReply(Message message) {
		Actor sender;
		Message result;
		MessageFolder messageFolder;
		
		sender = this.actorService.findPrincipal();
		result = new Message();
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setSender(sender);
		result.setRecipient(message.getSender());
		messageFolder = this.messageFolderService.findMessageFolderByNameAndActor("Out box", sender.getId());
		result.setMessageFolder(messageFolder);
		
		return result;
	}
	

	public Message reconstruct(Message message, BindingResult bindingResult){
		Message result;
		Message messageBD;
		Actor sender;
		MessageFolder messageFolder;
		
		if (message.getId() == 0) {
			
			sender = this.actorService.findPrincipal();
			message.setMoment(new Date(System.currentTimeMillis() - 1000));
			message.setSender(sender);
			messageFolder = this.messageFolderService.findMessageFolderByNameAndActor("Out box", sender.getId());
			message.setMessageFolder(messageFolder);
			
			result = message;
		} else {
			messageBD = this.messageRepository.findOne(message.getId());
			message.setId(messageBD.getId());
			message.setVersion(messageBD.getVersion());
			message.setSender(messageBD.getSender());
			message.setRecipient(messageBD.getRecipient());
			message.setPriority(messageBD.getPriority());
			message.setBody(messageBD.getBody());
			message.setMoment(messageBD.getMoment());
		
			result = message;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}
	
	public void flush(){
		this.messageRepository.flush();
	}
	
	
	public Collection<Message> findMessageWithTabooWord(int messageFolderId, String tabooWord){
		
		Collection<Message> result;
		
		result = this.messageRepository.findMessageWithTabooWord(messageFolderId, tabooWord);
		
		return result;
		
	}
	
	public Message findMessageWithTabooWord(String tabooWord, int messageId){
		
		Message result;
		
		result = this.messageRepository.findMessageWithTabooWord(tabooWord, messageId);
		
		return result;
		
	}
	
	public boolean checkSpamMessage(Message message){
		
		boolean result;
		Collection<String> tabooWords;
		Iterator<String> it;
		
		result = false;
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		
		while (it.hasNext())
			if(this.findMessageWithTabooWord(it.next(), message.getId()) != null){

				result = true;
				break;
			}
		
		return result;
		
	}
	

}
