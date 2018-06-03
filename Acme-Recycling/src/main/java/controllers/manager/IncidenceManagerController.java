package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.IncidenceService;

import controllers.AbstractController;
import domain.Incidence;

@Controller
@RequestMapping("/incidence/manager")
public class IncidenceManagerController extends AbstractController{
	
//	Services --------------------------------------------------------
	
	@Autowired
	private IncidenceService incidenceService;
	
	
//	Constructors

	public IncidenceManagerController() {
		super();
		
	}
		
//		Listing incidences no resolved---------------------------------------------------------
		@RequestMapping(value = "/list", method = RequestMethod.GET)
		public ModelAndView list() {
			ModelAndView result;
			Collection<Incidence> incidences;
	
			incidences = this.incidenceService.findIncidenceNoResolved();

			result = new ModelAndView("incidence/list");
			result.addObject("incidences", incidences);
			result.addObject("requestURI", "incidence/manager/list.do?d-16544-p=1");
			result.addObject("RequestURIresolve", "incidence/manager/resolve.do");
			result.addObject("RequestURIdisplay", "incidence/manager/display.do");


			return result;
		}
		
		
//		Listing incidences resolved ---------------------------------------------------------
		@RequestMapping(value = "/listResolved", method = RequestMethod.GET)
		public ModelAndView listResolved() {
			ModelAndView result;
			Collection<Incidence> incidences;
	
			incidences = this.incidenceService.findIncidenceResolved();

			result = new ModelAndView("incidence/list");
			result.addObject("incidences", incidences);
			result.addObject("requestURI", "incidence/manager/list.do?d-16544-p=1");
			result.addObject("RequestURIdisplay", "incidence/manager/display.do");


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
			result.addObject("requestURI", "incidence/manager/display.do");

			return result;
		}
		
		
		// Resolve---------------------------------------------------------------
		
		@RequestMapping(value = "/resolve", method = RequestMethod.GET)
		public ModelAndView edit(@RequestParam int incidenceId) {
			ModelAndView result;
			Incidence incidence;

			incidence = this.incidenceService.findOne(incidenceId);
			Assert.notNull(incidence);
			
			try{
				incidence.setResolved(true);
				this.incidenceService.save(incidence);
				result = new ModelAndView("redirect:list.do");
			}catch (Throwable oops) {
				result = this.listWithMessage("incidence.commit.error");
			}
			
			

			return result;

		}
		
		//Other
		
		protected ModelAndView listWithMessage(String message) {
			ModelAndView result;
			Collection<Incidence> incidences;

			
			incidences = this.incidenceService.findIncidenceNoResolved();

			result = new ModelAndView("incidence/list");
			result.addObject("incidences", incidences);
			result.addObject("requestURI", "incidences/manager/list.do");
			result.addObject("message", message);
			return result;

		}
		
		

}
