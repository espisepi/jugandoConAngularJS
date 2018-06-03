
package controllers.buyer;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewscastService;
import controllers.AbstractController;
import domain.Comment;
import domain.Newscast;

@Controller
@RequestMapping("/newscast/buyer")
public class NewscastBuyerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private NewscastService	newscastService;


	//	Lista las compras realizadas ------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Newscast> news;

		news = this.newscastService.findAll();

		result = new ModelAndView("newscast/list");
		result.addObject("newscast", news);
		result.addObject("requestURI", "newscast/buyer/list.do");

		return result;
	}

	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newscastId) {
		final ModelAndView result;
		Newscast newscast = new Newscast();
		Collection<Comment> comments;

		newscast = this.newscastService.findOne(newscastId);
		comments = new ArrayList<>();

		comments = this.newscastService.findCommentsByNew(newscastId);

		result = new ModelAndView("newscast/display");
		result.addObject("newscast", newscast);
		result.addObject("comments", comments);
		result.addObject("requestURI", "newscast/buyer/display.do");

		return result;
	}

}
