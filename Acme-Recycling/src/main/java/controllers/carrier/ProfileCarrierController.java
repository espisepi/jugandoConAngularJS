
package controllers.carrier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.CarrierService;
import controllers.AbstractController;
import domain.Carrier;
import forms.CarrierForm;

@Controller
@RequestMapping("/profile/carrier")
public class ProfileCarrierController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private CarrierService	carrierService;


	//Constructor--------------------------------------------------------

	public ProfileCarrierController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Carrier carrier;
		final CarrierForm carrierForm;

		carrier = this.carrierService.findByPrincipal();
		Assert.notNull(carrier);
		carrierForm = new CarrierForm(carrier);

		result = this.createEditModelAndView(carrierForm);
		//Esto nuevo
		result.addObject("requestURI", "profile/carrier/edit.do");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCarrier(@ModelAttribute("carrierForm") CarrierForm carrierForm, final BindingResult bindingResult) {
		ModelAndView result;

		carrierForm = this.carrierService.reconstruct(carrierForm, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(carrierForm);
		else
			try {
				if ((carrierForm.getCarrier().getId() == 0)) {
					Assert.isTrue(carrierForm.getCarrier().getUserAccount().getPassword().equals(carrierForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(carrierForm.getConditions(), "the conditions must be accepted");
				}
				this.carrierService.save(carrierForm.getCarrier());
				result = new ModelAndView("redirect:/profile/carrier/display.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error");
			}

		return result;
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayUser() {
		ModelAndView result;
		Carrier carrier;

		carrier = this.carrierService.findByPrincipal();

		result = new ModelAndView("carrier/display");
		result.addObject("carrier", carrier);
		result.addObject("requestURI", "profile/carrier/display.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final CarrierForm carrierForm) {
		ModelAndView result;

		result = this.createEditModelAndView(carrierForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CarrierForm carrierForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("carrier/edit");
		result.addObject("carrierForm", carrierForm);
		result.addObject("message", message);
		//result.addObject("RequestURI", "buyer/edit.do");

		return result;

	}
}
