
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import services.CommentAngularService;

import com.google.gson.Gson;

import domain.CommentAngular;

@RestController
@RequestMapping(value = "/commentAngular")
public class CommentAngularController extends AbstractController {

	//Services--------------------------------------------
	@Autowired
	CommentAngularService	commentAngularService;


	//Constructor--------------------------------------------------------

	public CommentAngularController() {
		super();
	}

	//Methods---------------------------------------------------------

	//List------------------------------------------------
	@RequestMapping(value = "/listJSON", produces = {
		MediaType.APPLICATION_JSON_VALUE
	}, method = RequestMethod.GET)
	public String listJSON() {
		final String result;
		Collection<CommentAngular> commentsAngular;

		commentsAngular = this.commentAngularService.findAll();
		result = new Gson().toJson(commentsAngular);

		return result;
	}

	//List------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		final ModelAndView result;

		result = new ModelAndView("commentAngular/list");

		return result;
	}
}
