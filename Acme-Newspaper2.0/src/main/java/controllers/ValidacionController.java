
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ValidacionService;
import domain.Validacion;

@Controller
@RequestMapping("/validacion")
public class ValidacionController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	ValidacionService	validacionService;


	//Constructor--------------------------------------------------------
	public ValidacionController() {
		super();
	}

	//List --------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Validacion> validacions;

		validacions = this.validacionService.findAll();

		result = new ModelAndView("validacion/list");
		result.addObject("validacions", validacions);
		result.addObject("requestURI", "validacion/list.do");

		return result;

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

	//Edition--------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int validacionId) {
		ModelAndView result;
		Validacion validacion;

		validacion = this.validacionService.findOne(validacionId);
		result = this.createEditModelAndView(validacion);

		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Validacion validacion, final BindingResult bindingResult) {
		ModelAndView result;

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(validacion);
		else
			try {
				this.validacionService.save(validacion);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(validacion, "validacion.commit.error");
			}
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
