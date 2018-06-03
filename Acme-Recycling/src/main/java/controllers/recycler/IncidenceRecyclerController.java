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
import services.IncidenceService;
import services.RecyclerService;
import controllers.AbstractController;
import domain.Actor;
import domain.Incidence;

@Controller
@RequestMapping("/incidence/recycler")
public class IncidenceRecyclerController extends AbstractController{
	
//	Services --------------------------------------------------------
	
	@Autowired
	private IncidenceService incidenceService;
	
	@Autowired
	private RecyclerService recyclerService;
	
	@Autowired
	private ActorService actorService;
	
//	Constructors

	public IncidenceRecyclerController() {
		super();
	}
	
//	Listing ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Incidence> incidences;
		Actor principal;
		
		principal = this.recyclerService.findByPrincipal();
		incidences = this.incidenceService.findIncidencesByRecycler(principal.getId());

		result = new ModelAndView("incidence/list");
		result.addObject("incidences", incidences);
		result.addObject("requestURI", "incidence/recycler/list.do?d-16544-p=1");
		result.addObject("RequestURIedit", "incidence/recycler/edit.do");
		result.addObject("RequestURIdisplay", "incidence/recycler/display.do");


		return result;
	}
	
	//Display
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView Display(@RequestParam int incidenceId) {
		final ModelAndView result;
		Incidence incidence;

		incidence = this.incidenceService.findOne(incidenceId);

		result = new ModelAndView("incidence/display");
		result.addObject("incidence", incidence);
		result.addObject("requestURI", "incidence/recycler/display.do");

		return result;
	}
	
	// Create-------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Incidence incidence;

		incidence = this.incidenceService.create();
		result = this.createEditModelAndView(incidence);

		return result;

	}
	
	// Edit---------------------------------------------------------------
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int incidenceId) {
		ModelAndView result;
		Incidence incidence;
		Actor principal;

		incidence = this.incidenceService.findOne(incidenceId);
		principal = this.actorService.findPrincipal();
		Assert.notNull(incidence);
		Assert.isTrue(incidence.isResolved() == false);
		Assert.isTrue(principal.equals(incidence.getRecycler()));
		result = this.createEditModelAndView(incidence);

		return result;

	}
	
	
//	Save-------------------------------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Incidence incidence, BindingResult bindingResult) {
		ModelAndView result;
		Incidence incidenceReconstruct;
		
		
		incidenceReconstruct = this.incidenceService.reconstruct(incidence, bindingResult);
		
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(incidenceReconstruct);
		else
			try {
				this.incidenceService.save(incidenceReconstruct);
				result = new ModelAndView("redirect:/incidence/recycler/list.do");
			} catch (final Throwable oops) {
				
				if(oops.getMessage().equals("You don't create incidence because you don't have any item with Finished request")){
					result = this.createEditModelAndView(incidenceReconstruct, "incidence.commit.donItem");
				}else{
				result = this.createEditModelAndView(incidenceReconstruct, "incidence.commit.error");
				}
				
			}

		return result;
		
	}
	
	//Delete --------------------------------------------------------------------------
	
		@RequestMapping(value = "/create", method = RequestMethod.POST, params = "delete")
		public ModelAndView delete(Incidence incidence, BindingResult bindingResult) {
			ModelAndView result;
			incidence = this.incidenceService.reconstruct(incidence, bindingResult);
			try {
				this.incidenceService.delete(incidence);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(incidence, "incidence.commit.error");
			}

			return result;
		}
	
	// Ancillary methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(Incidence incidence) {
		ModelAndView result;
		result = this.createEditModelAndView(incidence, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(Incidence incidence, String messageCode) {
		ModelAndView result;
	
		result = new ModelAndView("incidence/edit");
		result.addObject("incidence", incidence);
		result.addObject("message", messageCode);
		result.addObject("requestURI", "incidence/recycler/create.do");
		result.addObject("RequestURIcancel", "incidence/recycler/list.do");

		return result;

	}
	
	

}
