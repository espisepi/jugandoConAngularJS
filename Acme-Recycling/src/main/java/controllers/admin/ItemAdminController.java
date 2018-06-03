
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import controllers.AbstractController;
import domain.Item;

@Controller
@RequestMapping("/item/admin")
public class ItemAdminController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private ItemService	itemService;


	//Constructor--------------------------------------------------------
	public ItemAdminController() {
		super();
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayItem(@RequestParam final int itemId) {
		ModelAndView result;
		final Item item;

		item = this.itemService.findOne(itemId);

		result = new ModelAndView("item/display");
		result.addObject("item", item);
		result.addObject("requestURI", "item/admin/display.do");

		return result;
	}

}
