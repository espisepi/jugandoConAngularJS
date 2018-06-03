
package controllers.admin;

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
@RequestMapping("/manager_/admin")
public class ManagerAdminController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	//Constructor--------------------------------------------------------

	public ManagerAdminController() {
		super();
	}

	//Edition------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createManager() {
		ModelAndView result;
		Manager manager;

		manager = this.managerService.create();

		ManagerForm cf;
		cf = new ManagerForm(manager);

		result = new ModelAndView("manager/edit");
		result.addObject("managerForm", cf);
		result.addObject("requestURI", "manager_/admin/edit.do");

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveManager(@ModelAttribute("managerForm") ManagerForm managerForm, final BindingResult binding) {
		ModelAndView result;

		managerForm = this.managerService.reconstruct(managerForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(managerForm);
		else
			try {
				if ((managerForm.getManager().getId() == 0)) {
					Assert.isTrue(managerForm.getManager().getUserAccount().getPassword().equals(managerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(managerForm.getConditions(), "the conditions must be accepted");
				}
				this.managerService.save(managerForm.getManager());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(managerForm, "manager.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(managerForm, "manager.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(managerForm, "manager.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(managerForm, "manager.commit.error");
			}

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
		result.addObject("manager", managerForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "manager_/admin/edit.do");

		return result;
	}
}
