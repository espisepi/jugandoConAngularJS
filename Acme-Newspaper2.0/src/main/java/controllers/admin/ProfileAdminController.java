package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import controllers.AbstractController;
import domain.Admin;
import forms.AdminForm;

@Controller
@RequestMapping("/profile/admin")
public class ProfileAdminController extends AbstractController{
	
	// Services---------------------------------------------------------

		@Autowired
		private AdminService adminService;
		
	//Constructor--------------------------------------------------------

		public ProfileAdminController() {
			super();
		}
		
		
		//Create------------------------------------------------------------

		@RequestMapping(value = "/create", method = RequestMethod.GET)
		public ModelAndView create() {
			ModelAndView result;
			Admin admin;
			AdminForm adminForm;

			admin = this.adminService.create();
			Assert.notNull(admin);
			adminForm = new AdminForm(admin);
			result = this.createEditModelAndView(adminForm);
			return result;
		}

		//Edition------------------------------------------------------------

		@RequestMapping(value = "/edit", method = RequestMethod.GET)
		public ModelAndView edit() {
			ModelAndView result;
			AdminForm adminForm;
			Admin admin;

			admin = this.adminService.findByPrincipal();
			Assert.notNull(admin);
			adminForm = new AdminForm(admin);
			result = this.createEditModelAndView(adminForm);
			return result;
		}

		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView save(AdminForm adminForm, final BindingResult bindingResult) {
			ModelAndView result;

			adminForm = this.adminService.reconstruct(adminForm, bindingResult);
			if (bindingResult.hasErrors())
				result = this.createEditModelAndView(adminForm);
			else
				try {
					if ((adminForm.getAdmin().getId() == 0)) {
						Assert.isTrue(adminForm.getAdmin().getUserAccount().getPassword().equals(adminForm.getPasswordCheck()), "password does not match");
						Assert.isTrue(adminForm.getConditions(), "the conditions must be accepted");
					}
					this.adminService.save(adminForm.getAdmin());
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable oops) {
					if (oops.getMessage().equals("password does not match"))
						result = this.createEditModelAndView(adminForm, "administrator.commit.error.passwordDoesNotMatch");
					else if (oops.getMessage().equals("the conditions must be accepted"))
						result = this.createEditModelAndView(adminForm, "administrator.commit.error.conditions");
					else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
						result = this.createEditModelAndView(adminForm, "administrator.commit.error.duplicateProfile");
					else
						result = this.createEditModelAndView(adminForm, "administrator.commit.error");
				}

			return result;
		}
		// Ancillary methods ------------------------------------------------------

		protected ModelAndView createEditModelAndView(final AdminForm adminForm) {

			ModelAndView result;
			result = this.createEditModelAndView(adminForm, null);
			return result;
		}

		protected ModelAndView createEditModelAndView(final AdminForm adminForm, final String message) {
			ModelAndView result;

			result = new ModelAndView("administrator/edit");
			result.addObject("adminForm", adminForm);
			result.addObject("message", message);

			return result;

		}
	
	

}
