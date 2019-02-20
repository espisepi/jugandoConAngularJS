
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.UnderwriteService;
import controllers.AbstractController;
import domain.Underwrite;

@Controller
@RequestMapping("/underwrite/customer")
public class UnderwriteCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private UnderwriteService	underwriteService;


	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int volumeId) {
		ModelAndView result;
		Underwrite underwrite;

		underwrite = this.underwriteService.create(volumeId);

		result = this.createEditModelAndView(underwrite);
		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Underwrite underwrite, final BindingResult bindingResult) {
		ModelAndView result;

		underwrite = this.underwriteService.reconstruct(underwrite, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(underwrite);
		else
			try {
				this.underwriteService.save(underwrite);
				result = new ModelAndView("redirect:/volume/customer/list.do?d-16544-p=1");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("el cliente ya esta subscrito a este periodico"))
					result = this.createEditModelAndView(underwrite, "subscription.customerHasThisSubscription.error");
				else if (oops.getMessage().equals("El cliente de la subscripcion debe ser el mismo que el logueado"))
					result = this.createEditModelAndView(underwrite, "subscription.customerEqualsLogged.error");
				else if (oops.getMessage().equals("solo se pueden subscribir a los periodicos privados"))
					result = this.createEditModelAndView(underwrite, "subscription.onlyPrivateNewspaper.error");
				else if (oops.getMessage().equals("solo se pueden subscribir a los periodicos publicados"))
					result = this.createEditModelAndView(underwrite, "subscription.onlyPublishedNewspaper.error");
				else if (oops.getMessage().equals("Invalid credit card"))
					result = this.createEditModelAndView(underwrite, "subscription.creditCard.Error");
				else
					result = this.createEditModelAndView(underwrite, "subscription.commit.error");

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Underwrite underwrite) {

		Assert.notNull(underwrite);
		ModelAndView result;
		result = this.createEditModelAndView(underwrite, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Underwrite underwrite, final String messageCode) {
		assert underwrite != null;

		ModelAndView result;

		result = new ModelAndView("underwrite/edit");
		result.addObject("underwrite", underwrite);
		result.addObject("message", messageCode);
		return result;

	}

}
