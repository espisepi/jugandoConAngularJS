
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import domain.Agent;
import forms.AgentForm;

@Controller
@RequestMapping("/agent")
public class AgentController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private AgentService	agentService;


	//Constructor--------------------------------------------------------

	public AgentController() {
		super();
	}

	//Listing-----------------------------------------------------------

	//Displaying----------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int agentId) {

		ModelAndView result;
		Agent agent;

		agent = this.agentService.findOne(agentId);

		result = new ModelAndView("agent/display");
		result.addObject("agent", agent);
		result.addObject("requestURI", "agent/display.do");

		return result;
	}

	//Create----------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createagent() {
		ModelAndView result;
		Agent agent;

		agent = this.agentService.create();

		AgentForm cf;
		cf = new AgentForm(agent);

		result = new ModelAndView("agent/edit");
		result.addObject("agentForm", cf);

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAgent(@ModelAttribute("agentForm") AgentForm agentForm, final BindingResult binding) {
		ModelAndView result;

		agentForm = this.agentService.reconstruct(agentForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(agentForm);
		else
			try {
				if ((agentForm.getAgent().getId() == 0)) {
					Assert.isTrue(agentForm.getAgent().getUserAccount().getPassword().equals(agentForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(agentForm.getConditions(), "the conditions must be accepted");
				}
				this.agentService.save(agentForm.getAgent());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(agentForm, "agent.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(agentForm, "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(agentForm, "agent.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(agentForm, "agent.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final AgentForm agentForm) {
		ModelAndView result;
		result = this.createEditModelAndView(agentForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final AgentForm agentForm, final String message) {
		ModelAndView result;
		result = new ModelAndView("agent/edit");
		result.addObject("agent", agentForm);
		result.addObject("message", message);

		return result;

	}

}
