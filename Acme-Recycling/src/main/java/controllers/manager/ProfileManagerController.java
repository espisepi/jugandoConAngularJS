
package controllers.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import controllers.AbstractController;
import domain.Manager;
import forms.ManagerForm;

@Controller
@RequestMapping("/profile/manager")
public class ProfileManagerController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	//Constructor--------------------------------------------------------

	public ProfileManagerController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Manager manager;
		final ManagerForm managerForm;

		manager = this.managerService.findByPrincipal();
		Assert.notNull(manager);
		managerForm = new ManagerForm(manager);

		result = this.createEditModelAndView(managerForm);
		//Esto nuevo
		result.addObject("requestURI", "profile/manager/edit.do");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveManager(@ModelAttribute("managerForm") ManagerForm managerForm, final BindingResult bindingResult) {
		ModelAndView result;

		managerForm = this.managerService.reconstruct(managerForm, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(managerForm);
		else
			try {
				if ((managerForm.getManager().getId() == 0)) {
					Assert.isTrue(managerForm.getManager().getUserAccount().getPassword().equals(managerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(managerForm.getConditions(), "the conditions must be accepted");
				}
				this.managerService.save(managerForm.getManager());
				result = new ModelAndView("redirect:/profile/manager/display.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(managerForm, "manager.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(managerForm, "manager.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(managerForm, "manager.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(managerForm, "manager.commit.error");
			}

		return result;
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayUser() {
		ModelAndView result;
		Manager manager;

		manager = this.managerService.findByPrincipal();

		result = new ModelAndView("manager/display");
		result.addObject("manager", manager);
		result.addObject("requestURI", "profile/manager/display.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm) {
		ModelAndView result;

		result = this.createEditModelAndView(managerForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ManagerForm managerForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("manager/edit");
		result.addObject("managerForm", managerForm);
		result.addObject("message", message);
		//result.addObject("RequestURI", "buyer/edit.do");

		return result;

	}
}
