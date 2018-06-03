
package controllers.admin;

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
import controllers.AbstractController;
import domain.Comment;
import domain.Newscast;

@Controller
@RequestMapping("/newscast/admin")
public class NewscastAdminController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private NewscastService	newscastService;


	//Constructor--------------------------------------------------------

	public NewscastAdminController() {
		super();
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayNewscast(@RequestParam final int newscastId) {
		final ModelAndView result;
		Newscast newscast = new Newscast();
		Collection<Comment> comments;

		newscast = this.newscastService.findOne(newscastId);
		comments = new ArrayList<>();

		comments = this.newscastService.findCommentsByNew(newscastId);

		result = new ModelAndView("newscast/display");
		result.addObject("newscast", newscast);
		result.addObject("comments", comments);
		result.addObject("requestURI", "newscast/admin/display.do");

		return result;
	}

	//Listing-------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Newscast> newscasts;

		newscasts = this.newscastService.newWithTabooWord();

		result = new ModelAndView("newscast/list");
		result.addObject("newscast", newscasts);
		result.addObject("requestURI", "newscast/admin/list.do");
		result.addObject("message", messageCode);

		return result;

	}

	//Delete---------------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int newscastId) {
		ModelAndView result;
		Newscast newscast;

		newscast = this.newscastService.findOne(newscastId);
		Assert.notNull(newscast);
		try {
			this.newscastService.deleteAdmin(newscast);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listWithMessage("new.commit.error");
		}

		return result;
	}

	//ancially methods---------------------------------------------------------------------------

	protected ModelAndView listWithMessage(final String message) {
		final ModelAndView result;
		Collection<Newscast> newscasts;
		newscasts = this.newscastService.findAll();
		result = new ModelAndView("newscast/list");
		result.addObject("newscasts", newscasts);
		result.addObject("requestURI", "/newscast/admin/list.do");
		result.addObject("message", message);
		return result;

	}
}
