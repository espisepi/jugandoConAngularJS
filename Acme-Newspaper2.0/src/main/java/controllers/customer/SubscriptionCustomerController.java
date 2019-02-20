
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SubscriptionService;
import controllers.AbstractController;
import domain.Subscription;

@Controller
@RequestMapping("/subscription/customer")
public class SubscriptionCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private SubscriptionService	subscriptionService;


	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {
		ModelAndView result;
		Subscription subscription;

		subscription = this.subscriptionService.create(newspaperId);

		result = this.createEditModelAndView(subscription);
		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Subscription subscription, final BindingResult bindingResult) {
		ModelAndView result;

		subscription = this.subscriptionService.reconstruct(subscription, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(subscription);
		else
			try {
				this.subscriptionService.save(subscription);
				result = new ModelAndView("redirect:/newspaper/customer/listAllPrivate.do?d-16544-p=1");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("el cliente ya esta subscrito a este periodico"))
					result = this.createEditModelAndView(subscription, "subscription.customerHasThisSubscription.error");
				else if (oops.getMessage().equals("El cliente de la subscripcion debe ser el mismo que el logueado"))
					result = this.createEditModelAndView(subscription, "subscription.customerEqualsLogged.error");
				else if (oops.getMessage().equals("solo se pueden subscribir a los periodicos privados"))
					result = this.createEditModelAndView(subscription, "subscription.onlyPrivateNewspaper.error");
				else if (oops.getMessage().equals("solo se pueden subscribir a los periodicos publicados"))
					result = this.createEditModelAndView(subscription, "subscription.onlyPublishedNewspaper.error");
				else if (oops.getMessage().equals("Invalid credit card"))
					result = this.createEditModelAndView(subscription, "subscription.creditCard.Error");
				else
					result = this.createEditModelAndView(subscription, "subscription.commit.error");

			}
		return result;
	}

	//Auxiliares ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Subscription subscription) {

		Assert.notNull(subscription);
		ModelAndView result;
		result = this.createEditModelAndView(subscription, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Subscription subscription, final String messageCode) {
		assert subscription != null;

		ModelAndView result;

		result = new ModelAndView("subscription/edit");
		result.addObject("subscription", subscription);
		result.addObject("message", messageCode);
		return result;

	}

}
