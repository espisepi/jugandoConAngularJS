
package controllers.customer;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import services.CustomerService;
import services.FollowUpService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.Customer;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/customer")
public class ArticleCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private CustomerService			customerService;

	@Autowired
	private UserService				userService;

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private FollowUpService			followUpService;


	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findNewsArticlesSearchForCustomer(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "article/customer/search.do");
		result.addObject("requestURISearchArticle", "article/customer/search.do");
		return result;
	}
	// List ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;

		result = this.listArticlesByUserSuscribed(userId);

		return result;
	}

	// Listb ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUserSuscribed(@RequestParam final int userId) {

		ModelAndView result;
		Collection<Article> articles;
		Customer customer;
		Set<Article> articlesAll;

		articlesAll = new HashSet<>();

		customer = this.customerService.findByPrincipal();

		articles = this.articleService.findArticlesByUserSuscribed(userId, customer.getId());
		articles.addAll(this.articleService.findArticlesByUserOpenAndFinalMode(userId));
		articlesAll.addAll(articles);
		result = new ModelAndView("article/list");
		result.addObject("articles", articlesAll);
		result.addObject("requestURI", "article/customer/listb.do");
		return result;
	}

	// Display ---------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article;
		Collection<Newspaper> myNewspapersSubscription;
		Collection<FollowUp> followsUp;
		Advertisement advertisement;

		article = this.articleService.findOne(articleId);
		advertisement = this.advertisementService.randomAdvertisement(article.getNewspaper());
		followsUp = this.followUpService.findFollowUpsByArticle(articleId);

		if (!article.getNewspaper().isOpen()) {
			myNewspapersSubscription = this.newspaperService.findNewspapersSubscribedByCustomerId(this.customerService.findByPrincipal().getId());
			Assert.isTrue(myNewspapersSubscription.contains(article.getNewspaper()), "you are not subscribed to the newspaper of this article");
		}

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followsUp", followsUp);
		result.addObject("advertisementrandom", advertisement);
		result.addObject("requestURI", "article/customer/display.do");

		return result;
	}

	// Display Article Subscripted----------------------------------------------------------------

	@RequestMapping(value = "/displayArticleSubscripted", method = RequestMethod.GET)
	public ModelAndView displayArticle(@RequestParam final int articleId) {
		final ModelAndView result;
		result = this.display(articleId);
		return result;
	}

	@RequestMapping(value = "/listSummary", method = RequestMethod.GET)
	public ModelAndView listSummary(@RequestParam final int articleId) {

		ModelAndView result;
		String summary;
		Article article;
		Collection<Newspaper> myNewspapersSubscription;

		summary = this.articleService.findSummaryByArticleId(articleId);
		article = this.articleService.findOne(articleId);
		if (!article.getNewspaper().isOpen()) {
			myNewspapersSubscription = this.newspaperService.findNewspapersSubscribedByCustomerId(this.customerService.findByPrincipal().getId());
			Assert.isTrue(myNewspapersSubscription.contains(article.getNewspaper()), "you are not subscribed to the newspaper of this article");
		}

		result = new ModelAndView("article/displaySummary");

		result.addObject("requestURI", "article/customer/listSummay.do");
		result.addObject("article", summary);

		return result;

	}

	//Displaying writer of an article----------------------

	@RequestMapping(value = "/displayUser", method = RequestMethod.GET)
	public ModelAndView displayUser(@RequestParam final int userId) {
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("requestURI", "article/user/displayUser.do");
		result.addObject("requestArticlesURL", "article/customer/listb.do");
		result.addObject("requestChirpsURL", "chirp/customer/list.do");

		return result;
	}

}
