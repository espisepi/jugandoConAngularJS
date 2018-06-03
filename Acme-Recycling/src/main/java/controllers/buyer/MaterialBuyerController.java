
package controllers.buyer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.MaterialService;
import controllers.AbstractController;
import domain.Material;

@Controller
@RequestMapping(value = "/material/buyer")
public class MaterialBuyerController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private MaterialService	materialService;


	//Constructor--------------------------------------------------------

	public MaterialBuyerController() {
		super();
	}

	//	Lista de los materiales que no has comprado aún------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Material> materials;

		materials = this.materialService.findAll();
		materials.removeAll(this.materialService.allMaterialWithoutQuantity());

		result = new ModelAndView("material/list");
		result.addObject("materials", materials);
		result.addObject("requestURI", "material/buyer/list.do");

		return result;
	}

}
