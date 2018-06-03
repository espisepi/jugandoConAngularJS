
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import services.LabelMaterialService;
import services.MaterialService;
import controllers.AbstractController;
import domain.Admin;
import domain.LabelMaterial;
import domain.Material;

@Controller
@RequestMapping(value = "/material/admin")
public class MaterialAdminController extends AbstractController {

	//Services--------------------------------------------
	@Autowired
	MaterialService			materialService;

	@Autowired
	AdminService			adminService;

	@Autowired
	LabelMaterialService	labelMaterialService;


	//Constructor--------------------------------------------------------

	public MaterialAdminController() {
		super();
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayMaterial(@RequestParam final int materialId) {
		ModelAndView result;
		Material material;

		material = this.materialService.findOne(materialId);

		result = new ModelAndView("material/display");
		result.addObject("material", material);
		result.addObject("buys", material.getBuys());
		result.addObject("requestURI", "material/admin/display.do");

		return result;
	}

	//Creating--------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		Material material;

		material = this.materialService.create();

		result = this.createEditModelAndView(material);
		result.addObject("requestURI", "material/admin/edit.do");
		return result;

	}

	//Edition---------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int materialId) {
		ModelAndView result;
		Material material;

		material = this.materialService.findOne(materialId);
		result = this.createEditModelAndView(material);
		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Material material, final BindingResult binding) {

		ModelAndView result;

		material = this.materialService.reconstruct(material, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(material);
		else
			try {
				this.materialService.save(material);
				result = new ModelAndView("redirect:/material/admin/list.do?d-16544-p=1");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(material, "material.commit.error");
			}

		return result;

	}

	//Delete-----------------
	//
	//	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	//	public ModelAndView delete(@Valid final Material material, final BindingResult binding) {
	//
	//		ModelAndView result;
	//
	//		if (binding.hasErrors())
	//			result = this.createEditModelAndView(material);
	//		else
	//			try {
	//
	//				this.materialService.delete(material);
	//				result = new ModelAndView("redirect:/material/admin/list.do?d-16544-p=1");
	//			} catch (final Throwable oops) {
	//				result = this.createEditModelAndView(material, "tabooWord.commit.error");
	//			}
	//
	//		return result;
	//
	//	}
	//
	//	//Delete---------------------------------------------------------------------
	//	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	//	public ModelAndView delete(final int materialId) {
	//		ModelAndView result;
	//		Material material;
	//
	//		material = this.materialService.findOne(materialId);
	//		Assert.notNull(material);
	//		try {
	//			this.materialService.delete(material);
	//			result = new ModelAndView("redirect:list.do");
	//		} catch (final Throwable oops) {
	//			if (oops.getMessage().equals("Existe un curso que no esta en modo final"))
	//				result = this.createDeleteModelAndView(material, "material.course.drafMode");
	//			else
	//				result = this.createDeleteModelAndView(material, "material.commit.error");
	//
	//		}
	//		return result;
	//	}

	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Material> materials;
		Admin adminConnectd;

		adminConnectd = this.adminService.findByPrincipal();
		Assert.notNull(adminConnectd);

		materials = this.materialService.findAll();
		result = new ModelAndView("material/list");
		result.addObject("materials", materials);
		result.addObject("requestURI", "material/admin/list.do");

		return result;
	}

	//Auxiliary-----------------------

	protected ModelAndView createEditModelAndView(final Material material) {

		Assert.notNull(material);
		ModelAndView result;
		result = this.createEditModelAndView(material, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Material material, final String messageCode) {
		assert material != null;

		ModelAndView result;
		Collection<LabelMaterial> labelsMaterials;

		labelsMaterials = this.labelMaterialService.findAll();

		result = new ModelAndView("material/edit");
		result.addObject("material", material);
		result.addObject("labelMaterials", labelsMaterials);
		result.addObject("message", messageCode);

		return result;

	}

	protected ModelAndView createDeleteModelAndView(final Material material) {
		ModelAndView result;
		result = this.createDeleteModelAndView(material, null);
		return result;
	}

	protected ModelAndView createDeleteModelAndView(final Material material, final String messageCode) {
		ModelAndView result;
		Collection<Material> materials;
		materials = this.materialService.findAll();

		result = new ModelAndView("material/list");
		result.addObject("material", material);
		result.addObject("materials", materials);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "material/admin/list.do");

		return result;

	}

}
