package controllers.agent;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import services.MessageFolderService;
import services.MessageService;
import controllers.AbstractController;
import domain.Agent;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/messageFolder/agent")
public class MessageFolderAgentController extends AbstractController{
	
//	Services --------------------------------------------------------

	@Autowired
	private MessageFolderService	messageFolderService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private AgentService agentService;


	//	Constructors

	public MessageFolderAgentController() {
		super();
	}

	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<MessageFolder> messageFolders;
		Agent principal;
		
		principal = this.agentService.findByPrincipal();
		messageFolders = this.messageFolderService.findMessageFolderByActor(principal.getId());

		result = new ModelAndView("messageFolder/list");
		result.addObject("messageFolders", messageFolders);
		result.addObject("RequestURIedit", "messageFolder/agent/edit.do");
		result.addObject("RequestURImessages", "message/agent/list.do?d-16544-p=1");
		result.addObject("requestURI", "messageFolder/agent/list.do");

		return result;
	}
	
	// Create-------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();
		result = this.createEditModelAndView(messageFolder);

		return result;

	}
	
	// Edit---------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(messageFolderId);
		Assert.notNull(messageFolder);
		Assert.isTrue(messageFolder.isModifiable()==true);
		result = this.createEditModelAndView(messageFolder);

		return result;

	}
	
	//	Save-------------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(MessageFolder messageFolder, BindingResult bindingResult) {
		ModelAndView result;

		messageFolder = this.messageFolderService.reconstruct(messageFolder, bindingResult);
		
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(messageFolder);
		else
			try {
				this.messageFolderService.saveToPrincipal(messageFolder);
				result = new ModelAndView("redirect:/messageFolder/agent/list.do");
			} catch (final Throwable oops) {

				if (oops.getMessage().equals("This message folder doesn't edit"))
					result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error.notModifiable");
				else if(oops.getMessage().equals("This folder exits"))
					result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error.exits");
				else
					result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
			}

		return result;
	}
	
	//Delete --------------------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(MessageFolder messageFolder, BindingResult bindingResult) {
		ModelAndView result;
		messageFolder = this.messageFolderService.reconstruct(messageFolder, bindingResult);
		try {
			this.messageFolderService.delete(messageFolder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
		}

		return result;
	}
	
	// Ancillary methods ------------------------------------------------------
		protected ModelAndView createEditModelAndView(MessageFolder messageFolder) {
			ModelAndView result;
			result = this.createEditModelAndView(messageFolder, null);
			return result;
		}

		protected ModelAndView createEditModelAndView(MessageFolder messageFolder, String messageCode) {
			ModelAndView result;
			Collection<Message> mess;

			mess = this.messageService.findMessagesByMessageFolder(messageFolder.getId());

			result = new ModelAndView("messageFolder/edit");
			result.addObject("messageFolder", messageFolder);
			result.addObject("mes", mess);
			result.addObject("message", messageCode);
			result.addObject("RequestURIcancel", "messageFolder/agent/list.do");
			result.addObject("requestURI", "messageFolder/agent/edit.do");

			return result;

		}
	

}
