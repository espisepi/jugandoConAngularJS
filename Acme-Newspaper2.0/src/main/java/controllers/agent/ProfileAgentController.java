
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AgentService;
import controllers.AbstractController;
import domain.Agent;
import forms.AgentForm;

@Controller
@RequestMapping("/profile/agent")
public class ProfileAgentController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private AgentService	agentService;


	//Constructor--------------------------------------------------------

	public ProfileAgentController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Agent agent;
		AgentForm agentForm;

		agent = this.agentService.findByPrincipal();
		Assert.notNull(agent);
		agentForm = new AgentForm(agent);

		result = this.createEditModelAndView(agentForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAgent(@ModelAttribute("agentForm") AgentForm agentForm, final BindingResult bindingResult) {
		ModelAndView result;

		agentForm = this.agentService.reconstruct(agentForm, bindingResult);

		if (bindingResult.hasErrors())
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
					result = this.createEditModelAndView(agentForm, "agentistrator.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(agentForm, "agentistrator.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(agentForm, "agentistrator.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(agentForm, "agentistrator.commit.error");
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
		result.addObject("agentForm", agentForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "agent/edit.do");

		return result;

	}

}
