
package controllers.editor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EditorService;
import controllers.AbstractController;
import domain.Editor;
import forms.EditorForm;

@Controller
@RequestMapping("/profile/editor")
public class ProfileEditorController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private EditorService	editorService;


	//Constructor--------------------------------------------------------

	public ProfileEditorController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Editor editor;
		final EditorForm editorForm;

		editor = this.editorService.findByPrincipal();
		Assert.notNull(editor);
		editorForm = new EditorForm(editor);

		result = this.createEditModelAndView(editorForm);
		//Esto nuevo
		result.addObject("requestURI", "profile/editor/edit.do");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEditor(@ModelAttribute("editorForm") EditorForm editorForm, final BindingResult bindingResult) {
		ModelAndView result;

		editorForm = this.editorService.reconstruct(editorForm, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(editorForm);
		else
			try {
				if ((editorForm.getEditor().getId() == 0)) {
					Assert.isTrue(editorForm.getEditor().getUserAccount().getPassword().equals(editorForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(editorForm.getConditions(), "the conditions must be accepted");
				}
				this.editorService.save(editorForm.getEditor());
				result = new ModelAndView("redirect:/profile/editor/display.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(editorForm, "editor.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(editorForm, "editor.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(editorForm, "editor.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(editorForm, "editor.commit.error");
			}

		return result;
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayUser() {
		ModelAndView result;
		Editor editor;

		editor = this.editorService.findByPrincipal();

		result = new ModelAndView("editor/display");
		result.addObject("editor", editor);
		result.addObject("requestURI", "profile/editor/display.do");

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final EditorForm editorForm) {
		ModelAndView result;

		result = this.createEditModelAndView(editorForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final EditorForm editorForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("editor/edit");
		result.addObject("editorForm", editorForm);
		result.addObject("message", message);
		//result.addObject("RequestURI", "buyer/edit.do");

		return result;

	}
}
