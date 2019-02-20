
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

import services.ArticleService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ArticleService		articleService;


	//List my newspapers-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Newspaper> newspapers;
		User user;

		user = this.userService.findByPrincipal();
		newspapers = this.newspaperService.findByUserId(user.getId());

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showMyArticles", true);
		result.addObject("showButtonPublish", true);
		result.addObject("showButtonEdit", true);
		result.addObject("showCreate", true);
		result.addObject("requestProfileURL", "/user/display.do");
		result.addObject("requestURISearchNewspaper", "newspaper/user/search.do");
		result.addObject("requestURI", "newspaper/user/list.do");
		result.addObject("message", messageCode);

		return result;

	}

	//Listing newspapers 5.2 ----------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listb(final String messageCode) {

		ModelAndView result;
		Collection<Newspaper> newspapers;

		//user = this.userService.findByPrincipal();
		newspapers = this.newspaperService.findNewspapersPublished();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("requestURI", "newspaper/user/listb.do");
		result.addObject("showCreate", false);
		result.addObject("showSearch", true);
		result.addObject("requestURISearchNewspaper", "newspaper/user/search.do");
		result.addObject("message", messageCode);
		return result;

	}

	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findNewspapersForUser(keyword);

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "newspaper/user/search.do");
		result.addObject("requestURISearchNewspaper", "newspaper/user/search.do");
		return result;
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);
		return result;
	}

	//Edition--------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		User user;

		user = this.userService.findByPrincipal();
		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(user.getNewspapers().contains(newspaper), "Cannot commit this operation, because it's illegal");
		Assert.isNull(newspaper.getPublicationDate(), "the newspaper is published");
		Assert.notNull(newspaper);
		result = this.createEditModelAndView(newspaper);
		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Newspaper newspaper, final BindingResult bindingResult) {
		ModelAndView result;

		newspaper = this.newspaperService.reconstruct(newspaper, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(newspaper);
		else
			try {
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}
		return result;
	}

	//Publish --------------------------------------------------------------------------------

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publish(@RequestParam final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		User user;

		user = this.userService.findByPrincipal();
		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(user.getNewspapers().contains(newspaper), "Cannot commit this operation, because it's illegal");
		Assert.notNull(newspaper);
		try {
			this.newspaperService.publish(newspaper);
			result = this.list("newspaper.publish.succesful");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("el publicador del periodico debe ser el mismo que el logueado"))
				result = this.list("newspaper.publishEqualsLoggin.error");
			else if (oops.getMessage().equals("todos sus articulos tienen que estar en modo final"))
				result = this.list("newspaper.allArticlesFinalMode.error");
			else if (oops.getMessage().equals("tiene que tener al menos un articulo para publicarse"))
				result = this.list("newspaper.atLeastOneArticle.error");
			else if (oops.getMessage().equals("la fecha de publicacion tiene que estar vacia"))
				result = this.list("newspaper.publicationDateNotNull.error");
			else
				result = this.list("newspaper.commit.error");
		}
		return result;
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		Newspaper newspaper = new Newspaper();
		final Collection<Article> articles;
		boolean isMyNewspaperPrivate;
		boolean hideAttributes;

		newspaper = this.newspaperService.findOne(newspaperId);

		//Si el periodico es privado y creado por el user se muestra completamente, si no, solo la tabla de contenido
		hideAttributes = false;
		if (!newspaper.isOpen()) {
			isMyNewspaperPrivate = newspaper.getPublisher().equals(this.userService.findByPrincipal());
			hideAttributes = !isMyNewspaperPrivate;
		}
		//TODOS LOS ARTÍCULOS DE UN PERIÓDICO
		articles = this.articleService.findArticlesByNewspaperId(newspaperId);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles);
		result.addObject("hideAttributes", hideAttributes);
		result.addObject("requestURI", "newspaper/user/display.do");

		return result;
	}
	//Auxiliares ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {

		Assert.notNull(newspaper);
		ModelAndView result;
		result = this.createEditModelAndView(newspaper, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String messageCode) {
		assert newspaper != null;

		ModelAndView result;

		result = new ModelAndView("newspaper/edit");
		result.addObject("newspaper", newspaper);
		result.addObject("message", messageCode);
		return result;

	}

}
