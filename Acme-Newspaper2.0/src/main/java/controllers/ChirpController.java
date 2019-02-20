
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import domain.Chirp;

@Controller
@RequestMapping("/chirp")
public class ChirpController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private ChirpService	chirpService;


	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Chirp> chirps;
		//final Newspaper newspaper;

		chirps = this.chirpService.findAllChirpsByUserId(userId);
		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		//result.addObject("newspaper", newspaper);
		//result.addObject("showButtonEdit", true);
		//result.addObject("requestURISearchArticle", "article/search.do");
		result.addObject("requestURI", "chirp/listb.do");

		return result;

	}

}
