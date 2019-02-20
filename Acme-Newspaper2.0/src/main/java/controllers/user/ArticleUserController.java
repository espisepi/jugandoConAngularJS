
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import services.FollowUpService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Article;
import domain.FollowUp;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/article/user")
public class ArticleUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ArticleService			articleService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;

	@Autowired
	private FollowUpService			followUpService;

	@Autowired
	private AdvertisementService	advertisementService;


	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Article> articles;

		articles = this.articleService.findArticlesForUser(keyword);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "article/user/search.do");
		//result.addObject("requestURI", "article/user/search.do&d-16544-p=1");
		//result.addObject("requestURISearchArticle", "article/user/search.do&d-16544-p=1");
		return result;
	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listArticlesUser() {
		User user;
		ModelAndView result;
		final Collection<Article> articles;
		user = this.userService.findByPrincipal();

		articles = this.articleService.findArticlesByUserId(user.getId());
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showCreate", false);
		result.addObject("showSearch", false);
		result.addObject("showListFollowUps", true);
		result.addObject("showCreateFollowUp", true);
		result.addObject("requestURI", "article/user/list.do");

		return result;

	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Article> articles;

		articles = this.articleService.findArticlesPublishedByUserId(userId);
		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("showCreate", false);
		result.addObject("requestURI", "article/user/listb.do");

		return result;

	}

	@RequestMapping(value = "listArticles", method = RequestMethod.GET)
	public ModelAndView displayArticles(@RequestParam final int userId) {
		ModelAndView result;
		final Collection<Article> articles;
		User principal;

		principal = this.userService.findByPrincipal();

		if (principal.getId() == userId)
			articles = this.articleService.findArticlesByUserId(principal.getId());
		else
			articles = this.articleService.findArticlesOfUserWhatIsOpen(userId);

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("requestURI", "article/user/listArticles.do");

		return result;
	}

	//List my articles-----------------------------------------------------------

	@RequestMapping(value = "/listMyArticles", method = RequestMethod.GET)
	public ModelAndView listMyArticles(@RequestParam final int newspaperId) {

		ModelAndView result;
		final Collection<Article> articles;
		Newspaper newspaper;
		User user;

		user = this.userService.findByPrincipal();
		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(newspaper.getPublisher().equals(user));
		articles = newspaper.getArticles();

		result = new ModelAndView("article/list");
		result.addObject("articles", articles);
		result.addObject("newspaper", newspaper);
		result.addObject("showCreate", true);
		result.addObject("showButtonEdit", true);
		result.addObject("requestURI", "article/user/listMyArticles.do");

		return result;

	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {
		ModelAndView result;
		Article article;

		article = this.articleService.create(newspaperId);

		result = this.createEditModelAndView(article);
		return result;
	}

	//Edition--------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int articleId) {
		ModelAndView result;
		Article article;
		User user;

		user = this.userService.findByPrincipal();
		article = this.articleService.findOne(articleId);
		Assert.isTrue(user.getArticles().contains(article), "Cannot commit this operation, because it's illegal");
		Assert.isTrue(article.getPublishedMoment() == null && article.isDraftMode() == true, "Tiene que estar en modo borrador y sin fecha de publicacion");
		Assert.notNull(article);
		// Esta restriccion esta en el save del servicio pero la jefa quiere que tambien este aqui
		if (article.getNewspaper() != null)
			Assert.isNull(article.getNewspaper().getPublicationDate(), "el periodico no puede estar publicado para introducir este articulo");
		result = this.createEditModelAndView(article);
		return result;
	}

	// Save -----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Article article, final BindingResult bindingResult) {
		ModelAndView result;

		article = this.articleService.reconstruct(article, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(article);
		else
			try {
				this.articleService.save(article);
				result = new ModelAndView("redirect:/article/user/listMyArticles.do?newspaperId=" + article.getNewspaper().getId());
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("El articulo a guardar no puede ser nulo"))
					result = this.createEditModelAndView(article, "article.notNull.error");
				else if (oops.getMessage().equals("el escritor del articulo debe ser el mismo que el usuario logueado"))
					result = this.createEditModelAndView(article, "article.writerEqualsLogged.error");
				else if (oops.getMessage().equals("el escritor del articulo debe ser el mismo que el publicador del periodico"))
					result = this.createEditModelAndView(article, "article.writerEqualsPublished.error");
				else if (oops.getMessage().equals("tiene que asignarse un periodico para poder guardar en modo final"))
					result = this.createEditModelAndView(article, "article.finalModeWithOneNewspaper.error");
				else if (oops.getMessage().equals("el periodico no puede estar publicado para introducir este articulo"))
					result = this.createEditModelAndView(article, "article.newspaperNotPublished.error");
				else
					result = this.createEditModelAndView(article, "article.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/listSummary", method = RequestMethod.GET)
	public ModelAndView listSummary(@RequestParam final int articleId) {

		ModelAndView result;
		String summary;
		Article article;

		summary = this.articleService.findSummaryByArticleId(articleId);
		article = this.articleService.findOne(articleId);
		if (!article.getNewspaper().isOpen())
			Assert.isTrue(this.userService.findByPrincipal().getArticles().contains(article), "This article belongs to a private newspaper that is not yours");

		result = new ModelAndView("article/displaySummary");

		result.addObject("requestURI", "article/user/listSummay.do");
		result.addObject("article", summary);

		return result;

	}

	// Display article summary----------------------------------------------------------------

	@RequestMapping(value = "/displaySummary", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int articleId) {
		final ModelAndView result;
		Article article = new Article();

		article = this.articleService.findOne(articleId);

		if (!article.getNewspaper().isOpen())
			Assert.isTrue(this.userService.findByPrincipal().getArticles().contains(article), "This article belongs to a private newspaper that is not yours");

		result = new ModelAndView("article/displaySummary");
		result.addObject("article", article);
		result.addObject("requestURI", "article/user/displaySummary.do");

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
		followsUp = this.followUpService.findFollowUpsByArticle(articleId);
		advertisement = this.advertisementService.randomAdvertisement(article.getNewspaper());

		if (!article.getNewspaper().isOpen())
			Assert.isTrue(this.userService.findByPrincipal().getArticles().contains(article), "This article belongs to a private newspaper that is not yours");

		result = new ModelAndView("article/display");
		result.addObject("article", article);
		result.addObject("followsUp", followsUp);
		result.addObject("advertisementrandom", advertisement);
		result.addObject("requestURI", "article/user/display.do");

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
		result.addObject("requestArticlesURL", "article/listb.do");
		result.addObject("requestChirpsURL", "chirp/listb.do");

		return result;
	}

	//Auxiliares ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Article article) {

		Assert.notNull(article);
		ModelAndView result;
		result = this.createEditModelAndView(article, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Article article, final String messageCode) {
		assert article != null;

		ModelAndView result;

		result = new ModelAndView("article/edit");
		result.addObject("article", article);
		result.addObject("message", messageCode);
		return result;

	}

}
