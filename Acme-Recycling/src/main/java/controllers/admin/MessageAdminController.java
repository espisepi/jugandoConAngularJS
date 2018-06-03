package controllers.admin;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.MessageFolderService;
import services.MessageService;

import controllers.AbstractController;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/message/admin")
public class MessageAdminController extends AbstractController{
	
//	Services --------------------------------------------------------
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private MessageFolderService messageFolderService;
	
	//	Constructors
	
	public MessageAdminController(){
		super();
	}
	
	//	Listing ---------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam int messageFolderId) {

		ModelAndView result;
		Collection<Message> messages;
		Integer messageFolderId1 = messageFolderId;
		Actor actor;
		Collection<MessageFolder> messageFolders;

		actor = this.actorService.findPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(actor.getId());
		Assert.isTrue(messageFolders.contains(this.messageFolderService.findOne(messageFolderId)));

		messages = this.messageService.findMessagesByMessageFolder(messageFolderId);

		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("messageFolderId1", messageFolderId1);
		result.addObject("RequestURIChange", "message/admin/changeFolder.do");
		result.addObject("RequestURIDisplay", "message/admin/display.do");
		result.addObject("requestURI", "message/admin/list.do");

		return result;

	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam int messageId) {

		ModelAndView result;
		
		Message message;
		Actor principal;

		principal = this.actorService.findPrincipal();
		message = this.messageService.findOne(messageId);

		result = new ModelAndView("message/display");
		result.addObject("messageDisplay", message);
		result.addObject("RequestURIreply", "message/admin/reply.do");
		result.addObject("requestURI", "message/admin/display.do");
		if(!message.getSender().equals(principal)){
			result.addObject("show", true);
		}else 
			result.addObject("show", false);

		return result;

	}
	
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;

		Message message;
		message = this.messageService.create();

		result = this.createNewModelAndView(message);
		result.addObject("requestURI", "message/admin/send.do");

		return result;

	}
	
	@RequestMapping(value = "/send", method = RequestMethod.POST, params = "send")
	public ModelAndView send(@ModelAttribute("m") Message m, BindingResult bindingResult) {
		ModelAndView result;
		m = this.messageService.reconstruct(m, bindingResult);
		Message messageSend;
		
		if (bindingResult.hasErrors())
			result = this.createNewModelAndView(m);
		else
			try {
				
				messageSend = this.messageService.send(m);
				result = new ModelAndView("redirect:display.do?messageId=" + messageSend.getId());
			} catch (Throwable oops) {

				result = this.createNewModelAndView(m, "message.commit.error");

			}
		return result;
	}
	
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam int messageId) {

		ModelAndView result;
		Message message;
		Message reply;
		
		message = this.messageService.findOne(messageId);
		reply = this.messageService.createReply(message);

		result = this.createNewModelAndViewReply(reply);
		result.addObject("requestURI", "message/admin/reply.do");

		return result;

	}
	
	@RequestMapping(value = "/reply", method = RequestMethod.POST, params = "send")
	public ModelAndView reply(@ModelAttribute("m") Message m, BindingResult binding) {
		ModelAndView result;
		m = this.messageService.reconstruct(m, binding);
		
		if (binding.hasErrors())
			result = this.createNewModelAndViewReply(m);
		else
			try {
				MessageFolder folderToReturn = m.getMessageFolder();

				this.messageService.send(m);
				result = new ModelAndView("redirect:list.do?messageFolderId=" + folderToReturn.getId());
			} catch (Throwable oops) {

				result = this.createNewModelAndViewReply(m, "message.commit.error");

			}
		return result;
	}	

	//Change folder message
	@RequestMapping(value = "/changeFolder", method = RequestMethod.GET)
	public ModelAndView changeFolderMessage(@RequestParam int messageId) {

		ModelAndView result;
		Message message;
		Collection<MessageFolder> messageFolders;
		Actor principal;
		
		message = this.messageService.findOne(messageId);
		principal = this.actorService.findPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(principal.getId());
		messageFolders.remove(message.getMessageFolder());
		
		Assert.notNull(message);

		result = new ModelAndView("message/changeFolder");
		result.addObject("folders", messageFolders);
		result.addObject("requestURICancel", "message/admin/list.do?messageFolderId=" + message.getMessageFolder().getId());
		result.addObject("msg", message);
		
		return result;

	}
	
	@RequestMapping(value = "/changeFolder", method = RequestMethod.POST, params = "change")
	public ModelAndView changeFolderMessage(@ModelAttribute("m") Message m, BindingResult binding, @RequestParam int messageId) {
		ModelAndView result;
		Message message;
		Actor principal;
		MessageFolder messageFolder;
		
		message = this.messageService.findOne(messageId);
		principal = this.actorService.findPrincipal();
		messageFolder = message.getMessageFolder();
		
		message = this.messageService.reconstruct(message, binding);
		
		if (binding.hasErrors())
			result = this.createNewModelAndViewChange(m);
		else
			try {
				this.messageService.saveMessageInFolder(principal, m.getMessageFolder().getName(), message);
				result = new ModelAndView("redirect:/messageFolder/admin/list.do");
			} catch (Throwable oops) {
				
				if(message.getMessageFolder().equals(messageFolder))
					result = this.createNewModelAndViewChange(message, "message.commit.error.move");
				
				else
					
					result = this.createNewModelAndViewChange(message, "message.commit.error");

			}
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int messageId) {
		ModelAndView result;
		Message message;
		MessageFolder messageFolder;

		message = this.messageService.findOne(messageId);
		messageFolder = message.getMessageFolder();
		Assert.notNull(message);
		try {
			this.messageService.delete(message);
			result = new ModelAndView("redirect:list.do?messageFolderId=" + messageFolder.getId());
		} catch (final Throwable oops) {
			
			result = this.listWithMessage(message.getMessageFolder().getId() ,"message.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createNewModelAndView(Message m) {
		ModelAndView result;
		result = this.createNewModelAndView(m, null);
		return result;
	}

	protected ModelAndView createNewModelAndView(Message m, String message) {
		ModelAndView result;

		result = new ModelAndView("message/send");

		Actor actor = this.actorService.findPrincipal();
		Collection<Actor> actors = this.actorService.findAll();
		actors.remove(actor);

		result.addObject("actors", actors);
		String low = "LOW";
		String neutral = "NEUTRAL";
		String high = "HIGH";
		Collection<String> priorities = new ArrayList<String>();
		priorities.add(low);
		priorities.add(neutral);
		priorities.add(high);

		result.addObject("message", message);
		result.addObject("priorities", priorities);
		result.addObject("show", true);
		result.addObject("RequestURICancel", "messageFolder/admin/list.do");
		result.addObject("m", m);
		return result;
	}
	
	protected ModelAndView createNewModelAndViewReply(Message m) {
		ModelAndView result;
		result = this.createNewModelAndView(m, null);
		return result;
	}

	protected ModelAndView createNewModelAndViewReply(Message m, String message) {
		ModelAndView result;

		result = new ModelAndView("message/send");

		Actor actor = this.actorService.findPrincipal();
		Collection<Actor> actors = this.actorService.findAll();
		actors.remove(actor);

		result.addObject("actors", actors);
		String low = "LOW";
		String neutral = "NEUTRAL";
		String high = "HIGH";
		Collection<String> priorities = new ArrayList<String>();
		priorities.add(low);
		priorities.add(neutral);
		priorities.add(high);

		result.addObject("message", message);
		result.addObject("priorities", priorities);
		result.addObject("show", false);
		result.addObject("RequestURICancel", "messageFolder/admin/list.do");
		result.addObject("m", m);
		return result;
	}
	
	
	protected ModelAndView createNewModelAndViewChange(Message m) {
		ModelAndView result;
		result = this.createNewModelAndViewChange(m, null);
		return result;
	}

	protected ModelAndView createNewModelAndViewChange(Message m, String message) {
		ModelAndView result;
		
		Collection<MessageFolder> messageFolders;
		Actor principal;
		
		principal = this.actorService.findPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(principal.getId());
		messageFolders.remove(m.getMessageFolder());
		
		Assert.notNull(m);

		result = new ModelAndView("message/changeFolder");
		result.addObject("folders", messageFolders);
		result.addObject("requestURICancel", "message/admin/list.do?messageFolderId=" + m.getMessageFolder().getId());
		result.addObject("msg", m);
		result.addObject("message", message);
		return result;
	}

	protected ModelAndView listWithMessage( @RequestParam int messageFolderId, final String message) {
		ModelAndView result;
		Collection<Message> messages;
		Integer messageFolderId1 = messageFolderId;
		Actor actor;
		Collection<MessageFolder> messageFolders;

		actor = this.actorService.findPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(actor.getId());
		Assert.isTrue(messageFolders.contains(this.messageFolderService.findOne(messageFolderId)));

		messages = this.messageService.findMessagesByMessageFolder(messageFolderId);

		result = new ModelAndView("message/list");
		result.addObject("messages", messages);
		result.addObject("messageFolderId1", messageFolderId1);
		result.addObject("requestURI", "message/admin/list.do");
		result.addObject("message", message);
		return result;

	}

}
