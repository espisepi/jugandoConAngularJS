
package controllers.admin;

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

import services.CleanPointService;
import controllers.AbstractController;
import domain.CleanPoint;

@Controller
@RequestMapping(value = "/cleanPoint/admin")
public class CleanPointAdminController extends AbstractController {

	//Services----------------------------------------------------------

	@Autowired
	private CleanPointService	cleanPointService;


	//Constructor--------------------------------------------------------

	public CleanPointAdminController() {
		super();
	}
	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int cleanPointId) {
		final ModelAndView result;
		CleanPoint cleanpoint;

		cleanpoint = this.cleanPointService.findOne(cleanPointId);

		result = new ModelAndView("cleanPoint/display");
		result.addObject("cleanpoint", cleanpoint);
		result.addObject("requestURI", "cleanPoint/admin/display.do");

		return result;
	}
	//Listing----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<CleanPoint> cleanPoints;

		cleanPoints = this.cleanPointService.findAll();

		result = new ModelAndView("cleanPoint/list");
		result.addObject("cleanPoints", cleanPoints);
		result.addObject("requestURI", "cleanPoint/admin/list.do");
		result.addObject("message", messageCode);

		return result;

	}
	//Creating------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		CleanPoint cleanPoint;

		cleanPoint = this.cleanPointService.create();

		result = this.createEditModelAndView(cleanPoint);
		return result;

	}

	//Edition-------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int cleanPointId) {
		ModelAndView result;
		CleanPoint cleanPoint;

		cleanPoint = this.cleanPointService.findOne(cleanPointId);
		Assert.isTrue(cleanPoint.isMobile() == true, "Cannot commit this operation, cleanPoint is not mobile");
		Assert.notNull(cleanPoint);
		result = this.createEditModelAndView(cleanPoint);
		return result;
	}

	//Saving--------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(CleanPoint cleanPoint, final BindingResult bindingResult) {
		ModelAndView result;
		cleanPoint = this.cleanPointService.reconstruct(cleanPoint, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(cleanPoint);
		else
			try {
				this.cleanPointService.save(cleanPoint);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(cleanPoint, "cleanPoint.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute final CleanPoint cleanPoint, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.cleanPointService.delete(cleanPoint);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(cleanPoint, "cleanPoint.commit.error");
		}

		return result;
	}

	//Auxiliary-----------------------

	protected ModelAndView createEditModelAndView(final CleanPoint cleanPoint) {

		Assert.notNull(cleanPoint);
		ModelAndView result;
		result = this.createEditModelAndView(cleanPoint, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final CleanPoint cleanPoint, final String messageCode) {
		assert cleanPoint != null;

		ModelAndView result;

		result = new ModelAndView("cleanPoint/edit");
		result.addObject("cleanPoint", cleanPoint);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "cleanPoint/admin/edit.do");

		return result;

	}

}
