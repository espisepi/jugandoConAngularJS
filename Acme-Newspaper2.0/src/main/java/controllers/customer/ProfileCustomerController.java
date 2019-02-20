package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CustomerService;

import controllers.AbstractController;
import domain.Customer;
import forms.CustomerForm;

@Controller
@RequestMapping("/profile/customer")
public class ProfileCustomerController extends AbstractController{
	
	// Services---------------------------------------------------------

	@Autowired
	private CustomerService customerService;
	
	
	//Constructor--------------------------------------------------------

	public ProfileCustomerController() {
		super();
	}
	
	
	//Edition------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		CustomerForm customerForm;
		Customer customer;

		customer = this.customerService.findByPrincipal();
		Assert.notNull(customer);
		customerForm = new CustomerForm(customer);
		result = this.createEditModelAndView(customerForm);
		return result;
	}

		//Save	------------------------------------------------------------
		@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
		public ModelAndView saveCustomer(@ModelAttribute("customerForm") CustomerForm customerForm, final BindingResult binding) {
			ModelAndView result;

			customerForm = this.customerService.reconstruct(customerForm, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(customerForm);
			else
				try {
					if ((customerForm.getCustomer().getId() == 0)) {
						Assert.isTrue(customerForm.getCustomer().getUserAccount().getPassword().equals(customerForm.getPasswordCheck()), "password does not match");
						Assert.isTrue(customerForm.getConditions(), "the conditions must be accepted");
					}
					this.customerService.save(customerForm.getCustomer());
					result = new ModelAndView("redirect:/welcome/index.do");
				} catch (final Throwable oops) {
					if (oops.getMessage().equals("password does not match"))
						result = this.createEditModelAndView(customerForm, "customer.password.match");
					else if (oops.getMessage().equals("the conditions must be accepted"))
						result = this.createEditModelAndView(customerForm, "actor.conditions.accept");
					else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
						result = this.createEditModelAndView(customerForm, "cutomer.commit.error.duplicateProfile");
					else
						result = this.createEditModelAndView(customerForm, "customer.commit.error");
				}

			return result;
		}

		// Ancillary methods ------------------------------------------------------

		protected ModelAndView createEditModelAndView(final CustomerForm customerForm) {
			ModelAndView result;
			result = this.createEditModelAndView(customerForm, null);
			return result;
		}

		protected ModelAndView createEditModelAndView(final CustomerForm customerForm, final String message) {
			ModelAndView result;
			result = new ModelAndView("customer/edit");
			result.addObject("customerForm", customerForm);
			result.addObject("message", message);
			result.addObject("RequestURI", "customer/edit.do");

			return result;

		}
}
