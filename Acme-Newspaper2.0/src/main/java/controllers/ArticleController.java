
package controllers;

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
import services.NewspaperService;
import domain.Advertisement;
import domain.Article;
import domain.FollowUp;
import domain.Newspaper;

@Controller
@RequestMapping("/article")
public class ArticleController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private ArticleService			articleService;

	@Autowired
	private FollowUpService			followUpService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private AdvertisementService	advertisementService;


	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findArticleByKeyword(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showSearch", true);
		result.addObject("showButtonSearchNotAuthenticated", true);
		result.addObject("requestURI", "article/search.do");
		result.addObject("requestURISearchArticle", "article/search.do");
		return result;
	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listMyArticles(@RequestParam final int newspaperId) {

		ModelAndView result;
		final Collection<Article> articles;
		final Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(newspaper.isOpen() == true, "Cannot display a private newspaper");
		Assert.isTrue(newspaper.getPublicationDate() != null, "Cannot display a not publicated newspaper ");
		articles = this.articleService.findArticlesByNewspaperId(newspaperId);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		//result.addObject("newspaper", newspaper);
		//result.addObject("showButtonEdit", true);
		result.addObject("requestURISearchArticle", "article/search.do");
		result.addObject("requestURI", "article/list.do");

		return result;

	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Article> articles;

		articles = this.articleService.findArticlesIfNewspaperPublishedByUserId(userId);
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showSearch", false);
		result.addObject("requestURI", "article/listb.do");

		return result;

	}

	// Display all article----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayArticle(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article = new Article();
		Collection<FollowUp> followsUp;
		Advertisement advertisement;

		article = this.articleService.findOne(articleId);
		advertisement = this.advertisementService.randomAdvertisement(article.getNewspaper());
		Assert.isTrue(article.getNewspaper().isOpen() == true, "the article belong to a private newspaper");
		Assert.isTrue(!article.isDraftMode(), "The article is in DraftMode");
		Assert.isTrue(article.getNewspaper().getPublicationDate() != null, "Cannot display a not publicated newspaper ");
		followsUp = this.followUpService.findFollowUpsByArticle(articleId);
		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followsUp", followsUp);
		result.addObject("advertisementrandom", advertisement);
		result.addObject("requestURI", "article/display.do");

		return result;
	}

}
