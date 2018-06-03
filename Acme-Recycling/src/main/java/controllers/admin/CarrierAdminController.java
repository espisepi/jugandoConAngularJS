
package controllers.admin;

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
@RequestMapping("/carrier/admin")
public class CarrierAdminController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private CarrierService	carrierService;


	//Constructor--------------------------------------------------------

	public CarrierAdminController() {
		super();
	}

	//Edition------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createCarrier() {
		ModelAndView result;
		Carrier carrier;

		carrier = this.carrierService.create();

		CarrierForm cf;
		cf = new CarrierForm(carrier);

		result = new ModelAndView("carrier/edit");
		result.addObject("carrierForm", cf);
		result.addObject("requestURI", "carrier/admin/edit.do");

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCarrier(@ModelAttribute("carrierForm") CarrierForm carrierForm, final BindingResult binding) {
		ModelAndView result;

		carrierForm = this.carrierService.reconstruct(carrierForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(carrierForm);
		else
			try {
				if ((carrierForm.getCarrier().getId() == 0)) {
					Assert.isTrue(carrierForm.getCarrier().getUserAccount().getPassword().equals(carrierForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(carrierForm.getConditions(), "the conditions must be accepted");
				}
				this.carrierService.save(carrierForm.getCarrier());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(carrierForm, "carrier.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(carrierForm, "carrier.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(carrierForm, "carrier.commit.error");
			}

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
		result.addObject("carrier", carrierForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "carrier/admin/edit.do");

		return result;
	}
}
