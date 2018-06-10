
package controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import services.AdminService;
import services.LabelMaterialService;
import services.MaterialService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import domain.Material;

@RestController
@RequestMapping(value = "/materialJSON")
public class MaterialJSONController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	AdminService			adminService;

	@Autowired
	LabelMaterialService	labelMaterialService;

	@Autowired
	MaterialService			materialService;


	//Constructor--------------------------------------------------------

	public MaterialJSONController() {
		super();
	}

	@RequestMapping(value = "/listJSON", produces = {
		MediaType.APPLICATION_JSON_VALUE
	}, method = RequestMethod.GET)
	public String listJSON() {

		final Collection<Material> materials = this.materialService.findAll();
		//Quitamos la bidireccionalidad para que no nos salga un stack overflow al intentar mapearlo a json
		for (final Material m : materials) {
			m.setBuys(null);
			m.getLabelMaterial().setMaterials(null);
		}
		final String serialize = new Gson().toJson(materials);
		return serialize;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;

		final RestTemplate restTemplate = new RestTemplate();
		//Si intento convertirlo a la clase Material.class no funcionaria, por lo tanto lo convertimos a una clase String y despues hacemos la conversion con Gson
		final String string = restTemplate.getForObject("http://localhost:8080/Acme-Recycling/materialJSON/listJSON.do", String.class);
		//Hacemos la conversion con Gson
		final Gson gson = new Gson();
		final Type listType = new TypeToken<ArrayList<Material>>() {
		}.getType();
		final List<Material> materials = gson.fromJson(string, listType);

		result = new ModelAndView("material/list");
		result.addObject("materials", materials);
		result.addObject("requestURI", "materialJSON/list.do");

		return result;
	}

}
