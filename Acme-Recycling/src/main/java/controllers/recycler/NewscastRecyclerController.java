
package controllers.recycler;

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
@RequestMapping(value = "/newscast/recycler")
public class NewscastRecyclerController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private NewscastService	newscastService;


	//Constructor--------------------------------------------------------

	public NewscastRecyclerController() {
		super();
	}

	//Listing----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Newscast> news;

		news = this.newscastService.findAll();

		result = new ModelAndView("newscast/list");
		result.addObject("newscast", news);
		result.addObject("requestURI", "newscast/recycler/list.do");
		result.addObject("message", messageCode);

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
		result.addObject("requestURI", "newscast/recycler/display.do");

		return result;
	}

}
