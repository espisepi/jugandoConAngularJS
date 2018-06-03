
package controllers.editor;

import java.util.ArrayList;
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

import services.EditorService;
import services.NewscastService;
import controllers.AbstractController;
import domain.Comment;
import domain.Editor;
import domain.Newscast;

@Controller
@RequestMapping(value = "/newscast/editor")
public class NewscastEditorController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private NewscastService	newscastService;

	@Autowired
	private EditorService	editorService;


	//Constructor--------------------------------------------------------

	public NewscastEditorController() {
		super();
	}

	//Display------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int newscastId) {
		final ModelAndView result;
		Newscast newscast = new Newscast();
		Collection<Comment> comments;

		newscast = this.newscastService.findOne(newscastId);
		comments = new ArrayList<>();

		comments = this.newscastService.findCommentsByNew(newscastId);

		result = new ModelAndView("newscast/display");
		result.addObject("newscast", newscast);
		result.addObject("comments", comments);
		result.addObject("requestURI", "newscast/editor/display.do");

		return result;
	}

	//Listing-------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {

		ModelAndView result;
		Collection<Newscast> news;

		news = this.editorService.findAllNewByEditor();

		result = new ModelAndView("newscast/list");
		result.addObject("newscast", news);
		result.addObject("requestURI", "newscast/editor/list.do");
		result.addObject("message", messageCode);

		return result;

	}

	//Creation-----------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Newscast newscast;

		newscast = this.newscastService.create();

		result = this.createEditModelAndView(newscast);
		return result;
	}

	// Edit---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int newscastId) {
		ModelAndView result;
		Newscast newscast;
		Editor principal;

		principal = this.editorService.findByPrincipal();

		newscast = this.newscastService.findOne(newscastId);

		Assert.notNull(newscast);
		Assert.isTrue(principal.getNews().contains(newscast), "This new is  not yours");
		result = this.createEditModelAndView(newscast);

		return result;

	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Newscast newscast, final BindingResult bindingResult) {
		ModelAndView result;
		newscast = this.newscastService.reconstruct(newscast, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(newscast);
		else
			try {
				this.newscastService.save(newscast);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newscast, "new.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute final Newscast newscast, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			this.newscastService.delete(newscast);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(newscast, "new.commit.error");
		}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Newscast newscast) {
		assert newscast != null;
		ModelAndView result;
		result = this.createEditModelAndView(newscast, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Newscast newscast, final String message) {

		assert newscast != null;
		ModelAndView result;

		result = new ModelAndView("newscast/edit");
		result.addObject("newscast", newscast);
		result.addObject("message", message);
		result.addObject("requestURI", "newscast/editor/edit.do");

		return result;

	}
}
