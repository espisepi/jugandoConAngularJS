
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

import services.CustomerService;
import domain.Customer;
import forms.CustomerForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private CustomerService	customerService;


	//Constructor--------------------------------------------------------

	public CustomerController() {
		super();
	}

	//Listing-----------------------------------------------------------

	//Displaying----------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int customerId) {

		ModelAndView result;
		Customer customer;

		customer = this.customerService.findOne(customerId);

		result = new ModelAndView("customer/display");
		result.addObject("customer", customer);
		result.addObject("requestURI", "customer/display.do");

		return result;
	}

	//Create----------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createCustomer() {
		ModelAndView result;
		Customer customer;

		customer = this.customerService.create();

		CustomerForm cf;
		cf = new CustomerForm(customer);

		result = new ModelAndView("customer/edit");
		result.addObject("customerForm", cf);

		return result;
	}

	//Edition------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Customer customer;

		customer = this.customerService.findByPrincipal();
		CustomerForm customerForm;
		customerForm = new CustomerForm(customer);
		result = new ModelAndView("customer/edit");
		result.addObject("uscustomererForm", customerForm);

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
					result = this.createEditModelAndView(customerForm, "customer.commit.error.duplicateProfile");
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
		result.addObject("customer", customerForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "customer/edit.do");

		return result;

	}

}
