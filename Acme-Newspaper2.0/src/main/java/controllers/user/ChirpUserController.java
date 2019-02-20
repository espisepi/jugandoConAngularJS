
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private UserService		userService;


	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.create();

		result = this.createEditModelAndView(chirp);
		return result;
	}

	//Edition--------------------------------------------------------------------------------
	//		@RequestMapping(value = "/edit", method = RequestMethod.GET)
	//		public ModelAndView edit(@RequestParam final int chirpId) {
	//			ModelAndView result;
	//			Chirp chirp;
	//
	//			chirp = this.chirpService.findOne(chirpId);
	//
	//			//Un usuario solo podrá editar sus anuncios.
	//			if (chirp.getId() != 0) {
	//				Collection<Announcement> announcementsOfUser;
	//				announcementsOfUser = this.announcementService.findAnnouncementByUserId();
	//				Assert.isTrue(announcementsOfUser.contains(chirp), "Cannot commit this operation, because it's illegal");
	//			}
	//
	//			Assert.notNull(chirp);
	//			result = this.createEditModelAndView(chirp);
	//			return result;
	//		}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chirp chirp, final BindingResult binding) {
		ModelAndView result;

		chirp = this.chirpService.reconstruct(chirp, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp);
		else
			try {
				this.chirpService.save(chirp);
				result = new ModelAndView("redirect:/chirp/user/listUser.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "chirp.commit.error");
			}
		return result;
	}

	// Listing ----------------------------------------------------------------
	//Lista para que me de las chirps de los usuarios que sigo
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		User userConnected;
		Collection<Chirp> chirp;

		userConnected = this.userService.findByPrincipal();
		chirp = new ArrayList<Chirp>(this.chirpService.getChirpsOfMyFollowers(userConnected.getId()));

		result = new ModelAndView("chirp/listFollows");
		result.addObject("chirps", chirp);
		result.addObject("requestURI", "chirp/user/list.do");

		return result;
	}

	// Listing ----------------------------------------------------------------
	//Lista para que me de las chirps del usuario logueado
	@RequestMapping(value = "/listUser", method = RequestMethod.GET)
	public ModelAndView listUser() {
		ModelAndView result;
		User userConnected;
		Collection<Chirp> chirps;

		userConnected = this.userService.findByPrincipal();
		chirps = this.chirpService.findAllChirpsByUserId(userConnected.getId());

		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		result.addObject("requestURI", "chirp/user/listUser.do");

		return result;
	}

	// List ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listArticlesByUser(@RequestParam final int userId) {

		ModelAndView result;
		final Collection<Chirp> chirps;
		//final Newspaper newspaper;

		chirps = this.chirpService.findAllChirpsByUserId(userId);
		result = new ModelAndView("chirp/list");
		result.addObject("chirps", chirps);
		result.addObject("requestURI", "chirp/user/listb.do");
		return result;
	}

	//Auxiliares ---------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Chirp chirp) {

		Assert.notNull(chirp);
		ModelAndView result;
		result = this.createEditModelAndView(chirp, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirp, final String messageCode) {
		assert chirp != null;

		ModelAndView result;

		result = new ModelAndView("chirp/edit");
		result.addObject("chirp", chirp);
		result.addObject("message", messageCode);
		return result;

	}

}
