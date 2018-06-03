
package controllers.buyer;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.BuyerService;
import services.ConfigurationSystemService;
import services.FinderService;
import controllers.AbstractController;
import domain.Buyer;
import domain.Finder;
import domain.Material;

@Controller
@RequestMapping("/finder/buyer")
public class FinderBuyerController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private BuyerService				buyerService;
	@Autowired
	private FinderService				finderService;
	@Autowired
	private ConfigurationSystemService	configurationSystemService;


	//Constructor--------------------------------------------------------

	public FinderBuyerController() {
		super();
	}
	// Search -----------------------------------------------------------------

	@RequestMapping(value = "/search", method = RequestMethod.POST, params = "search")
	public ModelAndView listByKeywordPriceDate(@Valid final Finder finder, final BindingResult bindingResult) {
		ModelAndView result;
		final Finder finderCache;
		Finder finderResult;
		Buyer buyerPrincipal;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				result = new ModelAndView("material/list");
				buyerPrincipal = this.buyerService.findByPrincipal();
				finderCache = buyerPrincipal.getFinder();
				final Date date = new Date();

				//Compruebo si el finder a modificar pertenece al buyer
				Assert.isTrue(finderCache.getId() == finder.getId());

				//Obtengo los milisegundos que el admin permite que se guarde en cache y compruebo si ha vencido el tiempo
				int hoursCacheMaxTime;
				final int milisecondsCacheMaxTime;
				hoursCacheMaxTime = this.configurationSystemService.findOne().getCacheMaxTime();
				milisecondsCacheMaxTime = hoursCacheMaxTime * 3600000;

				if (!(this.finderService.checkEquals(finderCache, finder) && (date.getTime() - finderCache.getStartCacheTime().getTime()) <= milisecondsCacheMaxTime)) {
					finderResult = this.finderService.search(finder);
					finderResult = this.finderService.save(finderResult);
					buyerPrincipal.setFinder(finderResult);
					this.buyerService.save(buyerPrincipal);
				}

				result = new ModelAndView("redirect:/finder/buyer/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(finder, "finder.commit.error");
			}

		return result;

	}
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView listMaterial() {
		ModelAndView result;
		Collection<Material> materials;
		Buyer buyer;
		Finder finder;

		buyer = this.buyerService.findByPrincipal();
		finder = buyer.getFinder();
		materials = finder.getMaterials();

		result = new ModelAndView("material/list");
		result.addObject("materials", materials);
		result.addObject("finder", finder);
		result.addObject("showSearch", true);
		result.addObject("requestURI", "finder/buyer/list.do");
		result.addObject("requestURISearch", "finder/buyer/search.do");

		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder) {
		assert finder != null;
		ModelAndView result;
		result = this.createEditModelAndView(finder, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Finder finder, final String message) {

		assert finder != null;
		ModelAndView result;

		result = new ModelAndView("material/list");
		result.addObject("finder", finder);
		result.addObject("showSearch", true);
		result.addObject("message", message);

		return result;

	}
}
