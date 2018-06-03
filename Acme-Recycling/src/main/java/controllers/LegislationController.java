
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.LegislationService;
import domain.Legislation;

@Controller
@RequestMapping("/legislation")
public class LegislationController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private LegislationService	legislationService;


	//Constructor--------------------------------------------------------
	public LegislationController() {
		super();
	}

	//Listing-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Legislation> laws;

		laws = this.legislationService.findAll();

		result = new ModelAndView("configurationSystem/listLegislation");
		result.addObject("laws", laws);
		result.addObject("requestURI", "legislation/list.do");
		result.addObject("message", messageCode);

		return result;

	}

}
