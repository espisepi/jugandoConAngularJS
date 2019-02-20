
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import services.FollowUpService;
import services.UserService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping(value = "/article/admin")
public class ArticleAdminController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private UserService				userService;

	@Autowired
	private FollowUpService			followUpService;

	@Autowired
	private AdvertisementService	advertisementService;


	//Constructor--------------------------------------------------------

	public ArticleAdminController() {
		super();
	}

	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findAllArticlesByAdmin(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "article/admin/search.do");
		return result;
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findArticlesFinalMode();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "article/admin/list.do?d-16544-p=1");
		return result;

	}

	@RequestMapping(value = "/listArticlesOfUserNotDraftMode", method = RequestMethod.GET)
	public ModelAndView listArticlesOfUserNotDraftMode(@RequestParam final int userId) {

		ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findArticlesOfUserWhatNotIsDraftMode(userId);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/admin/listArticlesOfUserNotDraftMode.do?d-16544-p=1");
		return result;

	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Article> articles;

		articles = this.articleService.findArticlesPublishedByUserIdAndNotPrivate(userId);
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/admin/listb.do?d-16544-p=1");
		return result;
	}

	//Listing taboo word

	@RequestMapping(value = "/listTabooWord", method = RequestMethod.GET)
	public ModelAndView listTabooWord() {

		ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.articleWithTabooWord();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showDelete", false);
		result.addObject("requestURI", "article/admin/listTabooWord.do?d-16544-p=1");
		return result;

	}

	//Delete---------------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int articleId) {
		ModelAndView result;
		Article article;

		article = this.articleService.findOne(articleId);
		Assert.notNull(article);
		try {
			this.articleService.delete(article);
			result = new ModelAndView("redirect:list.do?d-16544-p=1");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This article can't delete because his newspaper have subscriptions"))
				result = this.listWithMessage("article.commit.error.newspaper");
			else
				result = this.listWithMessage("article.commit.error");
		}

		return result;
	}

	//List Summary ---------------------------------------------------------------

	@RequestMapping(value = "/listSummary", method = RequestMethod.GET)
	public ModelAndView listSummary(@RequestParam final int articleId) {

		ModelAndView result;
		String summary;

		summary = this.articleService.findSummaryByArticleId(articleId);
		result = new ModelAndView("article/displaySummary");

		result.addObject("requestURI", "article/admin/listSummay.do?d-16544-p=1");
		result.addObject("article", summary);

		return result;

	}

	// Display ---------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article;
		Collection<FollowUp> followsUp;
		Advertisement advertisement;

		article = this.articleService.findOne(articleId);
		followsUp = this.followUpService.findFollowUpsByArticle(articleId);
		advertisement = this.advertisementService.randomAdvertisement(article.getNewspaper());

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followsUp", followsUp);
		result.addObject("advertisementrandom", advertisement);
		result.addObject("requestURI", "article/admin/display.do");

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
		result.addObject("requestURI", "article/admin/displayUser.do?d-16544-p=1");
		result.addObject("requestArticlesURL", "article/admin/listb.do?d-16544-p=1");
		result.addObject("requestChirpsURL", "chirp/admin/listb.do");

		return result;
	}

	//ancially methods---------------------------------------------------------------------------

	protected ModelAndView listWithMessage(final String message) {
		final ModelAndView result;
		Collection<Article> articles;
		articles = this.articleService.findAll();
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "article/admin/list.do?d-16544-p=1");
		result.addObject("message", message);

		return result;

	}

}
