
package controllers.recycler;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ItemService;
import services.LabelProductService;
import services.RecyclerService;
import controllers.AbstractController;
import domain.Actor;
import domain.Item;
import domain.LabelProduct;
import domain.Recycler;

@Controller
@RequestMapping("/item/recycler")
public class ItemRecyclerController extends AbstractController {

	//	Services --------------------------------------------------------

	@Autowired
	private ItemService			itemService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private LabelProductService	labelProductService;

	@Autowired
	private RecyclerService		recyclerService;


	//	Constructors

	public ItemRecyclerController() {
		super();
	}

	//	Listing PERSONAL ---------------------------------------------------------
	@RequestMapping(value = "/listb", method = RequestMethod.GET)
	public ModelAndView listPersonal() {
		ModelAndView result;
		Collection<Item> items;
		Actor principal;

		principal = this.actorService.findPrincipal();
		items = this.itemService.findItemsByRecycler(principal.getId());

		Assert.isTrue(principal instanceof Recycler);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("showScore", true);
		result.addObject("showDelete", false);
		result.addObject("requestURI", "item/recycler/listb.do?d-16544-p=1");
		result.addObject("RequestUriDisplay", "item/recycler/display.do");

		return result;
	}

	//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Item> items;
		Actor principal;

		principal = this.actorService.findPrincipal();
		items = this.itemService.findItemsByRecycler(principal.getId());

		Assert.isTrue(principal instanceof Recycler);

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("showScore", false);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "item/recycler/list.do?d-16544-p=1");
		result.addObject("RequestUriDisplay", "item/recycler/display.do");

		return result;
	}
	
	//Display
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView Display(@RequestParam int itemId) {
		final ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);
	

		result = new ModelAndView("item/display");
		result.addObject("item", item);
		result.addObject("requestURI", "item/recycler/display.do");
		result.addObject("RequestURIedit", "/request/recycler/display.do");
		

		return result;
	}
	
	

	// Create-------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Item item;

		item = this.itemService.create();
		result = this.createEditModelAndView(item);

		return result;

	}

	//	Save-------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Item item, final BindingResult bindingResult) {
		ModelAndView result;
		Item itemReconstruct;

		itemReconstruct = this.itemService.reconstruct(item, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(itemReconstruct);
		else
			try {
				this.itemService.save(itemReconstruct);
				result = new ModelAndView("redirect:/item/recycler/list.do");
			} catch (final Throwable oops) {
				if(oops.getMessage().equals("The quantity must be greater than")){
					result = this.createEditModelAndView(itemReconstruct, "item.commit.error.quanty");
				}else{
					result = this.createEditModelAndView(itemReconstruct, "item.commit.error");
				}

				
			}

		return result;

	}

	//Delete --------------------------------------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int itemId) {
		ModelAndView result;
		Item item;

		item = this.itemService.findOne(itemId);
		Assert.notNull(item);
		try {
			this.itemService.delete(item);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops){
			if(oops.getMessage().equals("This item is assigned a carrier"))
				result = this.listWithMessage("item.commit.error.carrier");
			else if(oops.getMessage().equals("This item will collect it soon or it has finished its process"))
				result = this.listWithMessage("item.commit.error.status");
			else

				result = this.listWithMessage("item.commit.error");
		}

		return result;
	}

	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final Item item) {
		ModelAndView result;
		result = this.createEditModelAndView(item, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Item item, final String messageCode) {
		ModelAndView result;
		Collection<LabelProduct> labelsProduct;

		labelsProduct = this.labelProductService.findAll();

		result = new ModelAndView("item/edit");
		result.addObject("item", item);
		result.addObject("labelsProduct", labelsProduct);
		result.addObject("requestURI", "item/recycler/create.do");
		result.addObject("RequestURIcancel", "item/recycler/list.do");
		result.addObject("message", messageCode);

		return result;

	}

	protected ModelAndView listWithMessage(final String message) {
		ModelAndView result;
		Collection<Item> items;
		Recycler principal;

		principal = this.recyclerService.findByPrincipal();
		items = this.itemService.findItemsByRecycler(principal.getId());

		result = new ModelAndView("item/list");
		result.addObject("items", items);
		result.addObject("showScore", false);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "item/recycler/list.do");
		result.addObject("message", message);
		return result;

	}

}
