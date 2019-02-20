
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Newspaper;

@Controller
@RequestMapping(value = "/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;


	//Constructor--------------------------------------------------------

	public AdvertisementAgentController() {
		super();
	}

	//Creation-----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int newspaperId) {
		ModelAndView result;
		final Advertisement advertisement;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);

		//El periódico sobre el cual se esta creando el aviso debe de estar en modo abierto
		Assert.isTrue(newspaper.isOpen() == true, "Cannot commit this operation");

		advertisement = this.advertisementService.create();

		result = this.createEditModelAndView(advertisement);
		result.addObject("requestURI", "advertisement/agent/addNewspaper.do?newspaperId=" + newspaperId);
		return result;

	}

	@RequestMapping(value = "/addNewspaper", method = RequestMethod.POST, params = "save")
	public ModelAndView addNewspaper(Advertisement advertisement, final BindingResult bindingResult, @RequestParam final int newspaperId) {
		ModelAndView result;
		advertisement = this.advertisementService.reconstruct(advertisement, bindingResult);
		Newspaper newspaper;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(advertisement);
		else
			try {
				advertisement = this.advertisementService.save(advertisement);
				newspaper = this.newspaperService.findOne(newspaperId);
				newspaper.getAdvertisements().add(advertisement);
				//Creamos un nuevo save para que el agent pueda salvar un periódico
				this.newspaperService.saveA(newspaper);

				result = new ModelAndView("redirect:/newspaper/agent/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Advertisement advertisement, final BindingResult bindingResult) {
		ModelAndView result;
		advertisement = this.advertisementService.reconstruct(advertisement, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(advertisement);
		else
			try {
				this.advertisementService.save(advertisement);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		assert advertisement != null;
		ModelAndView result;
		result = this.createEditModelAndView(advertisement, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String message) {

		assert advertisement != null;
		ModelAndView result;

		result = new ModelAndView("advertisement/edit");
		result.addObject("advertisement", advertisement);
		result.addObject("message", message);

		return result;

	}

}
