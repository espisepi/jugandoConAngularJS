
package controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
		//ya no necesitamos traernos los objetos porque Angular lo hara en la vista

		//Collection<Validacion> validacions;

		//validacions = this.validacionService.findAll();

		result = new ModelAndView("validacion/list");
		//result.addObject("validacions", validacions);
		//result.addObject("requestURI", "validacion/list.do");

		return result;

	}

	//List REST --------------------------------------------------------------
	@RequestMapping(value = "/rest/list", method = RequestMethod.GET)
	public @ResponseBody
	Collection<Validacion> listRest() {
		Collection<Validacion> validacions;

		validacions = this.validacionService.findAll();

		//se puede eliminar este codigo gracias a la anotacion @JsonIgnore
		//		for (final Validacion v : validacions)
		//			v.setNewspaper(null);

		return validacions;
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
	public ResponseEntity<Object> save(@Valid final Validacion validacion, final BindingResult bindingResult) {
		ResponseEntity<Object> result;

		if (bindingResult.hasErrors()) {
			final List<String> errors = this.bindingErrorsToList(bindingResult);
			result = new ResponseEntity<Object>(errors, HttpStatus.OK);
		} else
			try {
				this.validacionService.save(validacion);
				result = new ResponseEntity<Object>(HttpStatus.CREATED);
			} catch (final Throwable oops) {
				result = new ResponseEntity<Object>(Collections.singletonList("cannot commit this operation"), HttpStatus.CONFLICT);
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
