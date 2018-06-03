
package controllers.buyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BuyerService;
import controllers.AbstractController;
import domain.Buyer;
import forms.BuyerForm;

@Controller
@RequestMapping("/profile/buyer")
public class ProfileBuyerController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private BuyerService	buyerService;


	//Constructor--------------------------------------------------------

	public ProfileBuyerController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Buyer buyer;
		final BuyerForm buyerForm;

		buyer = this.buyerService.findByPrincipal();
		Assert.notNull(buyer);
		buyerForm = new BuyerForm(buyer);

		result = this.createEditModelAndView(buyerForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAgent(@ModelAttribute("buyerForm") BuyerForm buyerForm, final BindingResult bindingResult) {
		ModelAndView result;

		buyerForm = this.buyerService.reconstruct(buyerForm, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(buyerForm);
		else
			try {
				if ((buyerForm.getBuyer().getId() == 0)) {
					Assert.isTrue(buyerForm.getBuyer().getUserAccount().getPassword().equals(buyerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(buyerForm.getConditions(), "the conditions must be accepted");
				}
				this.buyerService.save(buyerForm.getBuyer());
				result = new ModelAndView("redirect:/profile/buyer/display.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(buyerForm, "Buyer.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(buyerForm, "Buyer.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(buyerForm, "Buyer.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(buyerForm, "Buyer.commit.error");
			}

		return result;
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayUser() {
		ModelAndView result;
		Buyer buyer;

		buyer = this.buyerService.findByPrincipal();

		result = new ModelAndView("buyer/display");
		result.addObject("buyer", buyer);
		result.addObject("requestURI", "profile/buyer/display.do");

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
		result.addObject("buyerForm", buyerForm);
		result.addObject("message", message);
		//result.addObject("RequestURI", "buyer/edit.do");

		return result;

	}
}
