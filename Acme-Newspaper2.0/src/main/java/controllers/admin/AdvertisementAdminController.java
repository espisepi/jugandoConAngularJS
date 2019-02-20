
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import controllers.AbstractController;
import domain.Advertisement;

@Controller
@RequestMapping(value = "/advertisement/admin")
public class AdvertisementAdminController extends AbstractController {

	//Services--------------------------------------------
	@Autowired
	private AdvertisementService	advertisementService;


	//Constructor--------------------------------------------------------

	public AdvertisementAdminController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/listAdvertisementWithTabooWord", method = RequestMethod.GET)
	public ModelAndView listWithTabooWord() {

		ModelAndView result;
		Collection<Advertisement> advertisementWithTabooWord;

		advertisementWithTabooWord = this.advertisementService.advertisementWithTabooWord();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisementWithTabooWord);
		result.addObject("showDelete", false);
		result.addObject("requestURI", "advertisement/admin/listAdvertisementWithTabooWord.do?d-16544-p=1");
		return result;

	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Advertisement> advertisementWithTabooWord;

		advertisementWithTabooWord = this.advertisementService.findAll();

		result = new ModelAndView("advertisement/list");
		result.addObject("advertisements", advertisementWithTabooWord);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "advertisement/admin/list.do?d-16544-p=1");
		return result;

	}

	//Delete---------------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int advertisementId) {
		ModelAndView result;
		Advertisement advertisement;

		advertisement = this.advertisementService.findOne(advertisementId);
		Assert.notNull(advertisement);
		try {
			this.advertisementService.delete(advertisement);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.listWithMessage("article.commit.error");
		}

		return result;
	}

	protected ModelAndView listWithMessage(final String message) {
		final ModelAndView result;
		Collection<Advertisement> advertisements;
		advertisements = this.advertisementService.findAll();
		result = new ModelAndView("comment/list");
		result.addObject("advertisements", advertisements);
		result.addObject("requestURI", "advertisement/admin/list.do?d-16544-p=1");
		result.addObject("message", message);
		return result;

	}

}
