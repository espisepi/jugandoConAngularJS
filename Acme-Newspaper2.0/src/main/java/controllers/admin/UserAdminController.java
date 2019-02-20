
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ChirpService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/user/admin")
public class UserAdminController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private UserService		userService;
	@Autowired
	private ChirpService	chirpService;
	@Autowired
	private ArticleService	articleService;


	//Listing-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("requestURI", "user/admin/list.do");
		result.addObject("requestProfileURL", "user/admin/display.do");

		return result;

	}

	//Displaying----------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		Collection<Article> articles;
		final Collection<Chirp> chirps;

		articles = this.articleService.findArticlesPublishedByUserId(userId);
		chirps = this.chirpService.findAllChirpsByUserId(userId);
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("chirps", chirps);
		result.addObject("articles", articles);
		result.addObject("requestURI", "user/admin/display.do");
		result.addObject("requestArticlesURL", "article/admin/listb.do");
		result.addObject("requestChirpsURL", "chirp/admin/listb.do");

		return result;
	}

}
