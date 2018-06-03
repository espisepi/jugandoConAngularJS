
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import domain.Item;

@Controller
@RequestMapping("/item")
public class ItemController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private ItemService	itemService;


	//Constructor--------------------------------------------------------
	public ItemController() {
		super();
	}

	//Listing-----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int actorId) {
		ModelAndView result;
		Collection<Item> items;

		items = this.itemService.findItemsByRecycler(actorId);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("showScore", true);
		result.addObject("RequestUriDisplay", "item/display.do");
		result.addObject("requestURI", "item/list.do?d-16544-p=1");

		return result;
	}
	
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listb(@RequestParam final int recyclerId) {
		ModelAndView result;
		Collection<Item> items;

		items = this.itemService.findItemsByRecycler(recyclerId);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("showScore", true);
		result.addObject("RequestUriDisplay", "item/display.do");
		result.addObject("requestURI", "item/list.do?d-16544-p=1");

		return result;
	}

	//display-----------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int itemId) {
		ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);

		result = new ModelAndView("item/display");
		result.addObject("item", item);
		result.addObject("hiddenRequest", true);
		result.addObject("requestURI", "opinion/display.do?itemId=" + itemId);

		return result;
	}

}
