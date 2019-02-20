
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.FollowUpService;
import services.UserService;
import controllers.AbstractController;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping("/followUp/user")
public class FollowUpUserController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private FollowUpService	followUpService;

	@Autowired
	private ArticleService	articleService;

	@Autowired
	private UserService		userService;


	// Create -----------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int articleId) {
		ModelAndView result;
		final Article article;
		final FollowUp followUp;

		article = this.articleService.findOne(articleId);
		followUp = this.followUpService.create(article);

		result = this.createEditModelAndView(followUp);
		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(FollowUp followUp, final BindingResult binding) {
		ModelAndView result;

		followUp = this.followUpService.reconstruct(followUp, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(followUp);
		else
			try {

				this.followUpService.save(followUp);
				result = new ModelAndView("redirect:list.do?articleId=" + followUp.getArticle().getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(followUp, "followUp.commit.error");
			}
		return result;
	}

	// List
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int articleId) {
		ModelAndView result;
		Collection<FollowUp> followUps;
		User userConnected;
		Article article;

		article = this.articleService.findOne(articleId);
		userConnected = this.userService.findByPrincipal();
		Assert.isTrue(userConnected.getArticles().contains(article));
		followUps = this.followUpService.findFollowUpsByArticle(articleId);

		result = new ModelAndView("followUp/list");
		result.addObject("followUps", followUps);
		result.addObject("requestURI", "/followUp/user/list.do");

		return result;
	}
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int followUpId) {
		final ModelAndView result;
		final FollowUp followUp;
		User principal;
		principal = this.userService.findByPrincipal();
		followUp = this.followUpService.findOne(followUpId);
		Assert.isTrue(followUp.getArticle().getWriter().equals(principal), "This article is not show,because is not your");
		result = new ModelAndView("followUp/display");
		result.addObject("followUp", followUp);
		result.addObject("requestURI", "followUp/user/display.do");

		return result;
	}

	/*
	 * @RequestMapping(value = "/displayPictures", method = RequestMethod.GET)
	 * public ModelAndView display(@RequestParam final int followUpId) {
	 * final ModelAndView result;
	 * final Collection<String> pictures;
	 * FollowUp follow;
	 * //pictures = this.followUpService.findPicturesFollowUpByFollowIp(followUpId);
	 * follow = this.followUpService.findOne(followUpId);
	 * result = new ModelAndView("followUp/displayPictures");
	 * result.addObject("pictures", pictures);
	 * result.addObject("vacio", "");
	 * result.addObject("requestURI", "followUp/user/displayPictures.do");
	 * 
	 * return result;
	 * }
	 */
	protected ModelAndView createEditModelAndView(final FollowUp followUp) {
		Assert.notNull(followUp);
		ModelAndView result;
		result = this.createEditModelAndView(followUp, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final FollowUp followUp, final String messageCode) {
		assert followUp != null;

		ModelAndView result;

		result = new ModelAndView("followUp/edit");
		result.addObject("followUp", followUp);
		result.addObject("message", messageCode);
		return result;

	}

}
