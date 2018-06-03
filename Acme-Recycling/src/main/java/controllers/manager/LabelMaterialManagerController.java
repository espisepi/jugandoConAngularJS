
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LabelMaterialService;
import services.ManagerService;
import controllers.AbstractController;
import domain.LabelMaterial;
import domain.Manager;

@Controller
@RequestMapping("/labelMaterial/manager")
public class LabelMaterialManagerController extends AbstractController {

	//	Services --------------------------------------------------------

	@Autowired
	private LabelMaterialService	labelMaterialService;

	@Autowired
	private ManagerService			managerService;


	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Manager managerConnected;
		Collection<LabelMaterial> labelMaterials;

		managerConnected = this.managerService.findByPrincipal();

		Assert.isTrue(managerConnected instanceof Manager);

		labelMaterials = this.labelMaterialService.findAll();

		result = new ModelAndView("labelMaterial/list");
		result.addObject("labelMaterials", labelMaterials);
		result.addObject("requestURI", "labelMaterial/manager/list.do?d-16544-p=1");
		result.addObject("RequestURIedit", "messageFolder/manager/edit.do");
		result.addObject("RequestURImessages", "message/manager/list.do?d-16544-p=1");

		return result;
	}

	// Create-------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		LabelMaterial labelMaterial;

		labelMaterial = this.labelMaterialService.create();
		result = this.createEditModelAndView(labelMaterial);

		return result;

	}
	// Edit---------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int labelMaterialId) {
		ModelAndView result;
		LabelMaterial labelMaterial;

		labelMaterial = this.labelMaterialService.findOne(labelMaterialId);
		Assert.notNull(labelMaterial);
		result = this.createEditModelAndView(labelMaterial);

		return result;

	}

	//	Save-------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(LabelMaterial labelMaterial, final BindingResult bindingResult) {
		ModelAndView result;

		labelMaterial = this.labelMaterialService.reconstruct(labelMaterial, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(labelMaterial);
		else
			try {
				this.labelMaterialService.save(labelMaterial);
				result = new ModelAndView("redirect:/labelMaterial/manager/list.do?d-16544-p=1");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("This label is asociated with one material or more"))
					result = this.createEditModelAndView(labelMaterial, "labelMaterial.labelContainsMaterial.error");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(labelMaterial, "labelMaterial.nameUnique");
				else
					result = this.createEditModelAndView(labelMaterial, "labelMaterial.commit.error");

			}
		return result;
	}

	//Delete---------------------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int labelMaterialId) {
		ModelAndView result;
		LabelMaterial labelMaterial;

		labelMaterial = this.labelMaterialService.findOne(labelMaterialId);
		Assert.notNull(labelMaterial);
		try {
			this.labelMaterialService.delete(labelMaterial);
			result = new ModelAndView("redirect:list.do?d-16544-p=1");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This label is asociated with one material or more"))
				result = this.createDeleteModelAndView(labelMaterial, "labelMaterial.labelContainsMaterial.error");
			else
				result = this.createDeleteModelAndView(labelMaterial, "labelMaterial.commit.error");

		}
		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final LabelMaterial labelMaterial) {
		ModelAndView result;
		result = this.createEditModelAndView(labelMaterial, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final LabelMaterial labelMaterial, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("labelMaterial/edit");
		result.addObject("labelMaterial", labelMaterial);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "labelMaterial/manager/edit.do");

		return result;

	}

	protected ModelAndView createDeleteModelAndView(final LabelMaterial labelMaterial) {
		ModelAndView result;
		result = this.createEditModelAndView(labelMaterial, null);
		return result;
	}

	protected ModelAndView createDeleteModelAndView(final LabelMaterial labelMaterial, final String messageCode) {
		ModelAndView result;
		Collection<LabelMaterial> labelMaterials;
		labelMaterials = this.labelMaterialService.findAll();

		result = new ModelAndView("labelMaterial/list");
		result.addObject("labelMaterial", labelMaterial);
		result.addObject("labelMaterials", labelMaterials);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "labelMaterial/manager/list.do?d-16544-p=1");

		return result;

	}

}
