
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import controllers.AbstractController;
import domain.Actor;

@Controller
@RequestMapping("/actor/admin")
public class ActorAdminController extends AbstractController {

	//	Services --------------------------------------------------------

	@Autowired
	private ActorService	actorService;


	//	Constructors

	public ActorAdminController() {
		super();
	}

	//	Listing ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Actor> actors;

		actors = this.actorService.actorForBan();

		result = new ModelAndView("actor/listActor");
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/admin/list.do");
		result.addObject("RequestURIban", "actor/admin/ban.do");
		result.addObject("RequestURIunban", "actor/admin/unban.do");
		result.addObject("showbun", true);
		result.addObject("showunbun", true);
		result.addObject("RequestUriDisplay", "actor/admin/display.do");

		return result;

	}

	//Displaying--------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView Display(@RequestParam final int recyclerId) {
		final ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(recyclerId);

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("requestURI", "actor/admin/display.do");

		return result;
	}

	// Ban---------------------------------------------------------------

	@RequestMapping(value = "/ban", method = RequestMethod.GET)
	public ModelAndView ban(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);

		try {
			this.actorService.ban(actor.getUserAccount());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listWithMessage("actor.commit.error");
		}

		return result;

	}

	// Ban---------------------------------------------------------------

	@RequestMapping(value = "/unban", method = RequestMethod.GET)
	public ModelAndView unban(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		actor = this.actorService.findOne(actorId);
		Assert.notNull(actor);

		try {
			this.actorService.unban(actor.getUserAccount());
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listWithMessage("actor.commit.error");
		}

		return result;

	}

	//Other

	protected ModelAndView listWithMessage(final String message) {
		ModelAndView result;
		Collection<Actor> actors;

		actors = this.actorService.actorForBan();

		result = new ModelAndView("actor/list");
		result.addObject("actors", actors);
		result.addObject("requestURI", "actor/admin/list.do");
		result.addObject("message", message);
		return result;

	}

}
