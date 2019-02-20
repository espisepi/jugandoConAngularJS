
package controllers.agent;

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

import services.ArticleService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping(value = "/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ArticleService		articleService;


	//Constructor--------------------------------------------------------

	public NewspaperAgentController() {
		super();
	}

	//Lista de los periodicos que estan en modo publico para escribir un aviso 
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Newspaper> newspapers;
		Collection<Newspaper> newspapersHavingAnAdvertisement;

		newspapers = this.newspaperService.findAllNewspaperToWriteAnAdvertisement();
		newspapersHavingAnAdvertisement = this.newspaperService.findAllNewspaperHavingAtLeastOneAdvertisement();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);

		result.addObject("newspapersHavingAnAdvertisement", newspapersHavingAnAdvertisement);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "newspaper/agent/list.do?d-16544-p=1");
		return result;
	}

	//4.3) Lista de los periodicos que tienen un aviso hecho por el agente logueado
	@RequestMapping(value = "/listNewspapersWithAdvertisement", method = RequestMethod.GET)
	public ModelAndView listNewspapersWithAdvertisement() {

		ModelAndView result;

		final Set<Newspaper> cjsp = new HashSet<Newspaper>((this.newspaperService.findAllNewspaperHavingAtLeastOneAdvertisement()));

		result = new ModelAndView("newspaper/listForAdvertisement");
		result.addObject("newspapers", cjsp);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "newspaper/agent/listNewspapersWithAdvertisement.do?d-16544-p=1");
		return result;
	}

	//4.4) Cogemos todos los periódicos a los que se les puede crear un aviso
	//y le restamos los que ya tienen un aviso hecho por el agente.
	@RequestMapping(value = "/listNewspapersWithCeroAdvertisement", method = RequestMethod.GET)
	public ModelAndView listNewspapersWithCeroAdvertisement() {

		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findAllNewspaperToWriteAnAdvertisement();

		newspapers.removeAll(this.newspaperService.findAllNewspaperHavingAtLeastOneAdvertisement());

		result = new ModelAndView("newspaper/listForAdvertisement");
		result.addObject("newspapers", newspapers);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "newspaper/agent/listNewspapersWithCeroAdvertisement.do?d-16544-p=1");
		return result;
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newspaperId) {
		final ModelAndView result;
		Newspaper newspaper = new Newspaper();
		final Collection<Article> articles;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(newspaper.isOpen() == true, "The newspaper must be public");
		//TODOS LOS ARTÍCULOS DE UN PERIÓDICO
		articles = this.articleService.findArticlesFinalModeByNewspaper(newspaperId);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", newspaper);
		result.addObject("articles", articles);

		result.addObject("requestURI", "newspaper/agent/display.do");

		return result;
	}

}
