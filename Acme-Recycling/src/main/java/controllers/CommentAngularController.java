
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	@RequestMapping(value = "/list", produces = {
		MediaType.APPLICATION_JSON_VALUE
	}, method = RequestMethod.GET)
	public String list() {
		final String result;
		Collection<CommentAngular> commentsAngular;

		commentsAngular = this.commentAngularService.findAll();
		result = new Gson().toJson(commentsAngular);

		return result;
	}

	//save-----------------------------------------------
	@RequestMapping(value = "/list", produces = {
		MediaType.APPLICATION_JSON_VALUE
	}, method = RequestMethod.POST)
	public String save(final String commentJson) {
		CommentAngular result;
		String view;

		result = new Gson().fromJson(commentJson, CommentAngular.class);
		try {
			result = this.commentAngularService.save(result);
			view = this.list();
		} catch (final Throwable oops) {
			view = this.list();
		}

		return view;
	}

}
