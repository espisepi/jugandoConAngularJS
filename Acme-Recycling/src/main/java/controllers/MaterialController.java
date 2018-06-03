
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import services.LabelMaterialService;
import services.MaterialService;
import domain.Material;

@Controller
@RequestMapping(value = "/material")
public class MaterialController extends AbstractController {

	//Services--------------------------------------------
	@Autowired
	MaterialService			materialService;

	@Autowired
	AdminService			adminService;

	@Autowired
	LabelMaterialService	labelMaterialService;


	//Constructor--------------------------------------------------------

	public MaterialController() {
		super();
	}

	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Material> materials;

		materials = this.materialService.findAll();

		materials.removeAll(this.materialService.allMaterialWithoutQuantity());
		result = new ModelAndView("material/list");
		result.addObject("materials", materials);
		result.addObject("requestURI", "material/list.do");

		return result;
	}
}
