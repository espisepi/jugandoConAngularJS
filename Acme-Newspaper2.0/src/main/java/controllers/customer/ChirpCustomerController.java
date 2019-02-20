
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import controllers.AbstractController;
import domain.Chirp;

@Controller
@RequestMapping("/chirp/customer")
public class ChirpCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ChirpService	chirpService;


	// List ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Chirp> chirps;
		//final Newspaper newspaper;

		chirps = this.chirpService.findAllChirpsByUserId(userId);
		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		result.addObject("requestURI", "chirp/customer/list.do");
		return result;
	}
}
