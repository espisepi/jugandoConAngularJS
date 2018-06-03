
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BuyerService;
import domain.Buyer;
import forms.BuyerForm;

@Controller
@RequestMapping("/buyer")
public class BuyerController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private BuyerService	buyerService;


	//Constructor--------------------------------------------------------
	public BuyerController() {
		super();
	}

	//Listing-----------------------------------------------------------

	//Displaying--------------------------------------------------------

	//Create------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createBuyer() {
		ModelAndView result;
		Buyer buyer;

		buyer = this.buyerService.create();

		BuyerForm cf;
		cf = new BuyerForm(buyer);

		result = new ModelAndView("buyer/edit");
		result.addObject("buyerForm", cf);

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBuyer(@ModelAttribute("buyerForm") BuyerForm buyerForm, final BindingResult binding) {
		ModelAndView result;

		buyerForm = this.buyerService.reconstruct(buyerForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(buyerForm);
		else
			try {
				if ((buyerForm.getBuyer().getId() == 0)) {
					Assert.isTrue(buyerForm.getBuyer().getUserAccount().getPassword().equals(buyerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(buyerForm.getConditions(), "the conditions must be accepted");
				}
				this.buyerService.save(buyerForm.getBuyer());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(buyerForm, "buyer.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(buyerForm, "buyer.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(buyerForm, "buyer.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(buyerForm, "buyer.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final BuyerForm buyerForm) {
		ModelAndView result;
		result = this.createEditModelAndView(buyerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final BuyerForm buyerForm, final String message) {
		ModelAndView result;
		result = new ModelAndView("buyer/edit");
		result.addObject("buyer", buyerForm);
		result.addObject("message", message);

		return result;
	}

}
