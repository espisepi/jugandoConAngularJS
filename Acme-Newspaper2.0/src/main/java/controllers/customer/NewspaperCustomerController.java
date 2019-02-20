
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.CustomerService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Customer;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private ArticleService		articleService;

	@Autowired
	private CustomerService		customerService;


	//Listing newspapers 5.2 ----------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listb(final String messageCode) {

		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findNewspapersPublished();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("requestURI", "newspaper/customer/list.do");
		result.addObject("requestURISearchNewspaper", "newspaper/customer/search.do");
		result.addObject("message", messageCode);
		result.addObject("showSearch", true);

		return result;
	}

	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findNewsPapersSearchForCustomer(keyword);

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "newspaper/customer/search.do");
		result.addObject("requestURISearchNewspaper", "newspaper/customer/search.do");
		return result;
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		Newspaper newspaper = new Newspaper();
		final Collection<Article> articles;
		Collection<Newspaper> myNewspapersSubscription;
		boolean hideAttributes;
		boolean isSubscribed;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(newspaper.getPublicationDate() != null, "You can't display this newspaper");
		myNewspapersSubscription = this.newspaperService.findNewspapersSubscribedByCustomerId(this.customerService.findByPrincipal().getId());
		hideAttributes = false;
		if (!newspaper.isOpen()) {
			isSubscribed = myNewspapersSubscription.contains(newspaper);
			//Si no esta suscrito, escondo sus atributos
			hideAttributes = !isSubscribed;
		}

		//TODOS LOS ARTÍCULOS DE UN PERIÓDICO
		articles = this.articleService.findArticlesByNewspaperId(newspaperId);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles);
		result.addObject("showButtonDisplayArticle", true);
		result.addObject("hideAttributes", hideAttributes);
		result.addObject("requestURI", "newspaper/customer/display.do");

		return result;
	}
	//-----------------------CODIGO QUE ANTES ESTABA EN SubscriptionCustomerController

	//List private newspapers-----------------------------------------------------------

	@RequestMapping(value = "/listAllPrivate", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Newspaper> newspapersToSubscribe;
		Collection<Newspaper> newspapersSubscribed;
		Customer customer;

		customer = this.customerService.findByPrincipal();
		newspapersToSubscribe = this.newspaperService.findPrivateAndPublicatedNewspapersToSubscribe();
		newspapersSubscribed = this.newspaperService.findNewspapersSubscribedByCustomerId(customer.getId());

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapersToSubscribe);
		result.addObject("newspapersSubscribed", newspapersSubscribed);
		result.addObject("showButtonSubscription", true);
		result.addObject("requestURI", "newspaper/customer/listAllPrivate.do");

		return result;

	}

	//-----------------------FIN DE CODIGO QUE ANTES ESTABA EN SubscriptionCustomerController
}
