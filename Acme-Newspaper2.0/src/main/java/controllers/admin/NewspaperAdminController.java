
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping(value = "/newspaper/admin")
public class NewspaperAdminController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;


	//Constructor--------------------------------------------------------

	public NewspaperAdminController() {
		super();
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		Newspaper newspaper = new Newspaper();
		final Collection<Article> articles;

		newspaper = this.newspaperService.findOne(newspaperId);

		//TODOS LOS ARTÍCULOS DE UN PERIÓDICO
		articles = this.articleService.findArticlesByNewspaperId(newspaperId);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles);
		result.addObject("requestURI", "newspaper/admin/display.do");

		return result;
	}

	//Search -----------------------------------------------------------
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView listNewspaperByKeyword(@RequestParam final String keyword) {
		final ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findAllNewspapersByAdmin(keyword);

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "newspaper/admin/search.do");
		result.addObject("requestURISearchArticle", "newspaper/admin/search.do");
		return result;
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findAll();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "newspaper/admin/list.do?d-16544-p=1");
		return result;
	}

	//Listing

	@RequestMapping(value = "/listTabooWord", method = RequestMethod.GET)
	public ModelAndView listTabooWord() {

		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.NewspaperWithTabooWord();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showDelete", false);
		result.addObject("requestURI", "newspaper/admin/listTabooWord.do?d-16544-p=1");
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.notNull(newspaper);
		try {
			this.newspaperService.delete(newspaper);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {

			if (oops.getMessage().equals("Se pueden eliminar los periodicos publicos y privado que aún no tengas suscripciones"))
				result = this.listWithMessage("newspaper.commit.error.private");
			else
				result = this.listWithMessage("newspaper.commit.error");
		}

		return result;
	}

	//ancially methods---------------------------------------------------------------------------

	protected ModelAndView listWithMessage(final String message) {
		final ModelAndView result;
		Collection<Newspaper> newspapers;
		newspapers = this.newspaperService.findAll();
		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "/newspaper/admin/list.do");
		result.addObject("message", message);
		return result;

	}

}
