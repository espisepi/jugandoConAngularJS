
package controllers.agent;

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
@RequestMapping("/article/agent")
public class ArticleAgentController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private FollowUpService			followUpService;

	@Autowired
	private UserService				userService;

	@Autowired
	private AdvertisementService	advertisementService;


	//Mostrar el resumen del artículo
	@RequestMapping(value = "/listSummary", method = RequestMethod.GET)
	public ModelAndView listSummary(@RequestParam final int articleId) {

		ModelAndView result;
		String summary;

		summary = this.articleService.findSummaryByArticleId(articleId);

		result = new ModelAndView("article/displaySummary");

		result.addObject("requestURI", "article/agent/listSummay.do");
		result.addObject("article", summary);

		return result;
	}

	//Mostrar los artículos del periódico que estamos mostrando

	// Display all article----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayArticle(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article = new Article();
		Collection<FollowUp> followsUp;
		Advertisement advertisement;

		article = this.articleService.findOne(articleId);
		followsUp = this.followUpService.findFollowUpsByArticle(articleId);
		advertisement = this.advertisementService.randomAdvertisement(article.getNewspaper());

		//El periódico debe de ser público
		Assert.isTrue(article.getNewspaper().isOpen() == true, "cannot commit this operation");

		//El artículo tiene que estar en modo final
		Assert.isTrue(article.isDraftMode() == false, "cannot commit this operation");

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followsUp", followsUp);
		result.addObject("advertisementrandom", advertisement);
		result.addObject("requestURI", "article/agent/display.do");

		return result;
	}

	//Mostrar el perfil del usuario que ha escrito ese artículo para el agent

	@RequestMapping(value = "/displayUser", method = RequestMethod.GET)
	public ModelAndView displayUser(@RequestParam final int userId) {
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("requestURI", "article/agent/displayUser.do");
		result.addObject("requestArticlesURL", "article/listb.do");
		result.addObject("requestChirpsURL", "chirp/listb.do");

		return result;
	}

	//Mostrar todos los artículos dado un usuario

	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Article> articles;

		articles = this.articleService.findArticlesFinalModeByWriter(userId);
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/agent/listb.do");

		return result;

	}
}
