
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewscastService;
import domain.Comment;
import domain.Newscast;

@Controller
@RequestMapping("/newscast")
public class NewscastController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private NewscastService	newscastService;


	//Constructor--------------------------------------------------------
	public NewscastController() {
		super();
	}

	//Listing-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Newscast> news;

		news = this.newscastService.findAllNewsInDescOrder();

		result = new ModelAndView("newscast/list");
		result.addObject("newscast", news);
		result.addObject("requestURI", "newscast/list.do");
		result.addObject("message", messageCode);

		return result;

	}

	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newscastId) {
		final ModelAndView result;
		Newscast newscast = new Newscast();
		Collection<Comment> comments;
		Collection<Newscast> news;

		newscast = this.newscastService.findOne(newscastId);
		comments = new ArrayList<>();

		news = this.newscastService.findAllNewsInDescOrder();

		Assert.isTrue(news.contains(newscast), "You cannot see this one, sorry");
		comments = this.newscastService.findCommentsByNew(newscastId);

		result = new ModelAndView("newscast/display");
		result.addObject("newscast", newscast);
		result.addObject("comments", comments);
		result.addObject("requestURI", "newscast/display.do");

		return result;
	}
}
