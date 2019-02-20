
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ChirpService;
import services.UserService;
import domain.Article;
import domain.Chirp;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private UserService		userService;
	@Autowired
	private ArticleService	articlesService;
	@Autowired
	private ChirpService	chirpsService;


	//Constructor--------------------------------------------------------

	public UserController() {
		super();
	}

	//Listing-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<User> users;

		users = this.userService.findAll();

		result = new ModelAndView("user/list");
		result.addObject("users", users);
		result.addObject("requestURI", "user/list.do");
		result.addObject("requestProfileURL", "user/display.do");

		return result;

	}

	//Displaying----------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int userId) {
		Collection<Article> articles;
		final Collection<Chirp> chirps;

		articles = this.articlesService.findArticlesPublishedByUserId(userId);
		chirps = this.chirpsService.findAllChirpsByUserId(userId);
		ModelAndView result;
		User user;

		user = this.userService.findOne(userId);

		result = new ModelAndView("user/display");
		result.addObject("user", user);
		result.addObject("chirps", chirps);
		result.addObject("articles", articles);
		result.addObject("requestURI", "user/display.do");
		result.addObject("requestArticlesURL", "article/listb.do");
		result.addObject("requestChirpsURL", "chirp/listb.do");

		return result;
	}
	//Listing-----------------------------------------------------------
	@RequestMapping(value = "/listFollowers", method = RequestMethod.GET)
	public ModelAndView listMyFollowers() {
		ModelAndView result;
		User userConnected;

		userConnected = this.userService.findByPrincipal();

		result = new ModelAndView("user/list");
		result.addObject("users", userConnected.getFollowers());
		result.addObject("requestProfileURL", "/user/display.do");
		result.addObject("requestURI", "/user/listFollowers.do");

		return result;

	}

	@RequestMapping(value = "/listFollowed", method = RequestMethod.GET)
	public ModelAndView listMyFollowed() {
		ModelAndView result;
		User userConnected;

		userConnected = this.userService.findByPrincipal();

		result = new ModelAndView("user/list");
		result.addObject("users", userConnected.getFollowed());
		result.addObject("seguidos", true);
		result.addObject("requestProfileURL", "/user/display.do");
		result.addObject("requestURI", "/user/listFollowed.do");

		return result;

	}

	@RequestMapping(value = "/listUserNotFollowed", method = RequestMethod.GET)
	public ModelAndView listUserNotFollowed() {
		ModelAndView result;
		User userConnected;
		Collection<User> userNotFollowed;

		userNotFollowed = this.userService.findAll();
		userConnected = this.userService.findByPrincipal();

		userNotFollowed.removeAll(userConnected.getFollowed());
		userNotFollowed.remove(userConnected);

		result = new ModelAndView("user/list");
		result.addObject("users", userNotFollowed);
		result.addObject("seguidos", false);
		result.addObject("requestProfileURL", "/user/display.do");
		result.addObject("requestURI", "/user/listUserNotFollowed.do");

		return result;

	}

	//Displaying----------------------

	//Create----------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createUser() {
		ModelAndView result;
		User user;

		user = this.userService.create();

		UserForm cf;
		cf = new UserForm(user);

		result = new ModelAndView("user/edit");
		result.addObject("userForm", cf);

		return result;
	}

	//Edition------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		User user;

		user = this.userService.findByPrincipal();
		UserForm userForm;
		userForm = new UserForm(user);
		result = new ModelAndView("user/edit");
		result.addObject("userForm", userForm);

		return result;

	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCustomer(@ModelAttribute("userForm") UserForm userForm, final BindingResult binding) {
		ModelAndView result;

		userForm = this.userService.reconstruct(userForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(userForm);
		else
			try {
				if ((userForm.getUser().getId() == 0)) {
					Assert.isTrue(userForm.getUser().getUserAccount().getPassword().equals(userForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(userForm.getConditions(), "the conditions must be accepted");
				}
				this.userService.save(userForm.getUser());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(userForm, "user.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(userForm, "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(userForm, "user.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(userForm, "user.commit.error");
			}

		return result;
	}

	//Follow
	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam final int userId) {
		ModelAndView result;

		try {
			User userToFollow;
			userToFollow = this.userService.findOne(userId);
			this.userService.followUser(userToFollow);
			result = this.listUserNotFollowed();
			result.addObject("message", "user.follow.commit.ok");
		} catch (final Throwable oops) {
			result = this.listUserNotFollowed();
			result.addObject("message", "user.follow.commit.error");
		}
		return result;

	}

	//Unfollow
	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam final int userId) {
		ModelAndView result;

		try {
			User userToUnFollow;
			userToUnFollow = this.userService.findOne(userId);
			this.userService.unFollowUser(userToUnFollow);
			result = this.listMyFollowed();
			result.addObject("message", "user.unfollow.commit.ok");
		} catch (final Throwable oops) {
			result = this.listMyFollowed();
			result.addObject("message", "user.unfollow.commit.error");
		}
		return result;

	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final UserForm userForm) {
		ModelAndView result;
		result = this.createEditModelAndView(userForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final UserForm userForm, final String message) {
		ModelAndView result;
		result = new ModelAndView("user/edit");
		result.addObject("user", userForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "user/edit.do");

		return result;

	}

}
