
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ValidacionService;
import domain.Validacion;

@Controller
@RequestMapping("/validacion")
public class ValidacionController extends AbstractController {

	// Services---------------------------------------------------------
	ValidacionService	validacionService;


	//Constructor--------------------------------------------------------
	public ValidacionController() {
		super();
	}

	// Create -----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Validacion validacion;

		validacion = this.validacionService.create();
		result = this.createEditModelAndView(validacion);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Validacion validacion) {
		Assert.notNull(validacion);
		ModelAndView result;

		result = this.createEditModelAndView(validacion, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Validacion validacion, final String messageCode) {
		assert validacion != null;
		ModelAndView result;

		result = new ModelAndView("validacion/edit");
		result.addObject("validacion", validacion);
		result.addObject("message", messageCode);

		return result;

	}

}
