
package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/angular")
public class HelloWorldAngularController extends AbstractController {

	// Services---------------------------------------------------------

	//Constructor--------------------------------------------------------
	public HelloWorldAngularController() {
		super();
	}

	//display ------------------------------------------------------------
	@RequestMapping(value = "/helloWorld", method = RequestMethod.GET)
	public ModelAndView helloWorld() {
		ModelAndView result;

		result = new ModelAndView("angular/helloWorld");

		return result;

	}

}
