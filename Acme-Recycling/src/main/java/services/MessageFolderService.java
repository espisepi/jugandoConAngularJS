package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageFolderRepository;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Service
@Transactional
public class MessageFolderService {
	
	// Managed repository -----------------------------------------------------
	@Autowired
	private MessageFolderRepository messageFolderRepository;
		
	// Supporting services ----------------------------------------------------
		
	@Autowired
	private ActorService actorService;
		
	@Autowired
	private MessageService messageService;
		
	@Autowired
	private Validator	validator;
		
	// Constructors -----------------------------------------------------------
	public MessageFolderService(){
		super();
	}
	
	// Simple CRUD methods ----------------------------------------------------
	
	public MessageFolder create(){
		
		MessageFolder result;
		Actor actor;
		
		actor = this.actorService.findPrincipal();
		
		result = new MessageFolder();
		result.setActor(actor);
		result.setModifiable(true);
		
		return result;
	
	}
	
	public MessageFolder findOne(int messageFolderId){
		
		Assert.isTrue(messageFolderId != 0);
		
		MessageFolder result;
		Actor principal;
		
		principal = this.actorService.findPrincipal();
		
		result = this.messageFolderRepository.findOne(messageFolderId);
		
		Assert.isTrue(principal.equals(result.getActor()));
		
		return result;
		
	}

	public Collection<MessageFolder> findAll(){
		
		Collection<MessageFolder> result;
		
		result = this.messageFolderRepository.findAll();
		
		return result;
		
	}
	
	public void save(MessageFolder messageFolder){
		
		Assert.notNull(messageFolder);
			
		this.messageFolderRepository.save(messageFolder);
	
		
	}
	
	public void saveToPrincipal(MessageFolder messageFolder){
		
		Assert.notNull(messageFolder);
		
		Collection<String> messageFolders;
		Actor principal = null;
		
		principal = this.actorService.findPrincipal();
		messageFolders = this.findMessageFolderNameByActor(principal.getId());
		
		
		Assert.isTrue(!messageFolders.contains(messageFolder.getName()), "This folder exits");
		
		if(messageFolder.getId() != 0)
				
			Assert.isTrue(messageFolder.isModifiable() == true, "This message folder doesn't edit");
			Assert.isTrue(messageFolder.getActor() == principal);
			
			
		this.messageFolderRepository.save(messageFolder);
	
		
	}
	
	public void delete(MessageFolder messageFolder){
		
		Actor principal;
		Collection<Message> messages;
		
		principal = this.actorService.findPrincipal();
		messages = this.messageService.findMessagesByMessageFolder(messageFolder.getId());
		
		Assert.notNull(messageFolder);
		Assert.isTrue(messageFolder.getId() != 0);
		Assert.isTrue(messageFolder.isModifiable() == true, "This is a default folder so it can not be deleted");
		Assert.isTrue(messageFolder.getActor().equals(principal));		
		
		if(messages.size() > 0)
			for(Message m: messages)
				this.messageService.delete(m);
		
		this.messageFolderRepository.delete(messageFolder);
	}
	
	public void flush(){
		this.messageFolderRepository.flush();
	}
	
	//Other method-------------------------------------------------
	
		public Collection<MessageFolder> createDefaultMessageFolder(Actor actor){
			
			MessageFolder inBox;
			MessageFolder outBox;
			MessageFolder spamBox;
			MessageFolder notificationBox;
			MessageFolder trashBox;
			Collection<MessageFolder> result;
			
			
			inBox = new MessageFolder();
			outBox = new MessageFolder();
			trashBox = new MessageFolder();
			spamBox = new MessageFolder();
			notificationBox = new MessageFolder();
			result = new ArrayList<>();
			
			inBox.setName("In box");
			outBox.setName("Out box");
			trashBox.setName("Trash box");
			spamBox.setName("Spam box");
			notificationBox.setName("Notification box");
			
			inBox.setActor(actor);
			outBox.setActor(actor);
			trashBox.setActor(actor);
			spamBox.setActor(actor);
			notificationBox.setActor(actor);

			
			inBox.setModifiable(false);
			outBox.setModifiable(false);
			trashBox.setModifiable(false);
			spamBox.setModifiable(false);
			notificationBox.setModifiable(false);
			
					
			this.save(inBox);
			this.save(outBox);
			this.save(trashBox);
			this.save(spamBox);
			this.save(notificationBox);
			
			result.add(inBox);
			result.add(outBox);
			result.add(trashBox);
			result.add(spamBox);
			result.add(notificationBox);
			
			return result;
			
			
		}
		
		public Collection<MessageFolder> findMessageFolderByActor(int actorId){
			
			Collection<MessageFolder> result;
			
			result = this.messageFolderRepository.findMessageFolderByActor(actorId);
			
			return result;
			
		}
		
		public MessageFolder findMessageFolderByNameAndActor(String name, int actorId){
			
			MessageFolder result;
			
			result = this.messageFolderRepository.findMessageFolderByNameAndActor(name, actorId);
			
			return result;
			
		}
		
		public Collection<String> findMessageFolderNameByActor(int actorId){
			
			Collection<String> result;
			
			result = this.messageFolderRepository.findMessageFolderNameByActor(actorId);
			
			return result;
			
		}
		
		
		public MessageFolder reconstruct(MessageFolder messageFolder, BindingResult bindingResult){
			MessageFolder result;
			MessageFolder messageFolderBD;
			Actor actor;
			
			actor = this.actorService.findPrincipal();
			
			if (messageFolder.getId() == 0) {
				messageFolder.setModifiable(true);
				messageFolder.setActor(actor);
				result = messageFolder;
				
			} else {
				messageFolderBD = this.messageFolderRepository.findOne(messageFolder.getId());
				messageFolder.setId(messageFolderBD.getId());
				messageFolder.setVersion(messageFolderBD.getVersion());
				messageFolder.setModifiable(messageFolderBD.isModifiable());
				messageFolder.setActor(messageFolderBD.getActor());
			
				result = messageFolder;
			}
			this.validator.validate(result, bindingResult);
			return result;
		}

}
