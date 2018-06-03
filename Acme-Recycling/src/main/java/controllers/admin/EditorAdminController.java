
package controllers.admin;

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
import controllers.AbstractController;
import domain.Editor;
import forms.EditorForm;

@Controller
@RequestMapping("/editor/admin")
public class EditorAdminController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private EditorService	editorService;


	//Constructor--------------------------------------------------------

	public EditorAdminController() {
		super();
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayEditor(@RequestParam final int editorId) {
		ModelAndView result;
		Editor editor;

		editor = this.editorService.findOne(editorId);

		result = new ModelAndView("editor/display");
		result.addObject("editor", editor);
		result.addObject("requestURI", "editor/admin/display.do");

		return result;
	}

	//Edition------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createEditor() {
		ModelAndView result;
		Editor editor;

		editor = this.editorService.create();

		EditorForm cf;
		cf = new EditorForm(editor);

		result = new ModelAndView("editor/edit");
		result.addObject("editorForm", cf);
		result.addObject("requestURI", "editor/admin/edit.do");

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEditor(@ModelAttribute("editorForm") EditorForm editorForm, final BindingResult binding) {
		ModelAndView result;

		editorForm = this.editorService.reconstruct(editorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(editorForm);
		else
			try {
				if ((editorForm.getEditor().getId() == 0)) {
					Assert.isTrue(editorForm.getEditor().getUserAccount().getPassword().equals(editorForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(editorForm.getConditions(), "the conditions must be accepted");
				}
				this.editorService.save(editorForm.getEditor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(editorForm, "editor.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(editorForm, "editor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(editorForm, "editor.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(editorForm, "editor.commit.error");
			}

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
		result.addObject("editor", editorForm);
		result.addObject("message", message);
		result.addObject("RequestURI", "editor/admin/edit.do");

		return result;
	}
}
