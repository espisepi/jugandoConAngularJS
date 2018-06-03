
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import services.RecyclerService;
import domain.Item;
import domain.Recycler;
import forms.RecyclerForm;

@Controller
@RequestMapping("/recycler")
public class RecyclerController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private RecyclerService	recyclerService;
	
	@Autowired
	private ItemService itemService;


	//Constructor--------------------------------------------------------
	public RecyclerController() {
		super();
	}

	//Listing-----------------------------------------------------------
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Recycler> recyclers;
		
		
		recyclers = this.recyclerService.findAll();
		
		result = new ModelAndView("actor/list");
		result.addObject("actors", recyclers);
		result.addObject("requestURI", "recycler/list.do?d-16544-p=1");
		result.addObject("RequestURIitems", "/item/list.do");
		result.addObject("showbun", false);
		result.addObject("showunbun", false);
		result.addObject("RequestUriDisplay", "/recycler/display.do");


		return result;
	}

	//Displaying--------------------------------------------------------
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView Display(@RequestParam int recyclerId) {
		final ModelAndView result;
		Recycler recycler;
		Collection<Item> items;
		Double puntuation;

		recycler = this.recyclerService.findOne(recyclerId);
		items = this.itemService.findItemsByRecycler(recycler.getId());
		puntuation = this.recyclerService.puntuationOfRecycler1(recyclerId);


		result = new ModelAndView("recycler/display");
		result.addObject("recycler", recycler);
		result.addObject("items", items);
		result.addObject("puntuation", puntuation);
		result.addObject("requestURI", "/recycler/display.do");
		result.addObject("requestItemsURL", "/item/listb.do");

		return result;
	}

	//Create------------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView createRecycler() {
		ModelAndView result;
		Recycler recycler;

		recycler = this.recyclerService.create();

		RecyclerForm cf;
		cf = new RecyclerForm(recycler);

		result = new ModelAndView("recycler/edit");
		result.addObject("recyclerForm", cf);

		return result;
	}

	//Save	------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveRecycler(@ModelAttribute("recyclerForm") RecyclerForm recyclerForm, final BindingResult binding) {
		ModelAndView result;

		recyclerForm = this.recyclerService.reconstruct(recyclerForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(recyclerForm);
		else
			try {
				if ((recyclerForm.getRecycler().getId() == 0)) {
					Assert.isTrue(recyclerForm.getRecycler().getUserAccount().getPassword().equals(recyclerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(recyclerForm.getConditions(), "the conditions must be accepted");
				}
				this.recyclerService.save(recyclerForm.getRecycler());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(recyclerForm, "recycler.password.match");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(recyclerForm, "recycler.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error");
			}

		return result;
	}
	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final RecyclerForm recyclerForm) {
		ModelAndView result;
		result = this.createEditModelAndView(recyclerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RecyclerForm recyclerForm, final String message) {
		ModelAndView result;
		result = new ModelAndView("recycler/edit");
		result.addObject("recycler", recyclerForm);
		result.addObject("message", message);

		return result;
	}

}
