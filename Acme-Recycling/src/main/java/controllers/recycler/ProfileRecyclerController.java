
package controllers.recycler;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ItemService;
import services.RecyclerService;
import controllers.AbstractController;
import domain.Item;
import domain.Recycler;
import forms.RecyclerForm;

@Controller
@RequestMapping("/profile/recycler")
public class ProfileRecyclerController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private RecyclerService	recyclerService;

	@Autowired
	private ItemService		itemService;


	//Constructor--------------------------------------------------------

	public ProfileRecyclerController() {
		super();
	}

	//Edition------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final Recycler recycler;
		final RecyclerForm recyclerForm;

		recycler = this.recyclerService.findByPrincipal();
		Assert.notNull(recycler);
		recyclerForm = new RecyclerForm(recycler);

		result = this.createEditModelAndView(recyclerForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveAgent(@ModelAttribute("agentForm") RecyclerForm recyclerForm, final BindingResult bindingResult) {
		ModelAndView result;

		recyclerForm = this.recyclerService.reconstruct(recyclerForm, bindingResult);

		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(recyclerForm);
		else
			try {
				if ((recyclerForm.getRecycler().getId() == 0)) {
					Assert.isTrue(recyclerForm.getRecycler().getUserAccount().getPassword().equals(recyclerForm.getPasswordCheck()), "password does not match");
					Assert.isTrue(recyclerForm.getConditions(), "the conditions must be accepted");
				}
				this.recyclerService.save(recyclerForm.getRecycler());
				result = new ModelAndView("redirect:/profile/recycler/display.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("password does not match"))
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error.passwordDoesNotMatch");
				else if (oops.getMessage().equals("the conditions must be accepted"))
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error.conditions");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error.duplicateProfile");
				else
					result = this.createEditModelAndView(recyclerForm, "recycler.commit.error");
			}

		return result;
	}

	//Display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayRecycler() {
		ModelAndView result;
		Recycler recycler;
		final Collection<Item> items;
		Double puntuation;

		recycler = this.recyclerService.findByPrincipal();
		items = this.itemService.findItemsByRecycler(recycler.getId());
		puntuation = this.recyclerService.puntuationOfRecycler();

		result = new ModelAndView("recycler/display");
		result.addObject("recycler", recycler);
		result.addObject("items", items);
		result.addObject("puntuation", puntuation);
		result.addObject("showDelete", true);
		result.addObject("requestURI", "profile/recycler/display.do");
		result.addObject("requestItemsURL", "item/recycler/listb.do");

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
		result.addObject("recyclerForm", recyclerForm);
		result.addObject("message", message);

		return result;

	}
}
