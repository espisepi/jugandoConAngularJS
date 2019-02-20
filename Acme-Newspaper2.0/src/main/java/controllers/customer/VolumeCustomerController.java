
package controllers.customer;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.Volume;

@Controller
@RequestMapping("/volume/customer")
public class VolumeCustomerController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private VolumeService	volumeService;


	//List volumen con los periodicos abiertos y publicados -----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Volume> volumes;

		volumes = this.volumeService.findAll();
		Collection<Volume> volumeSubscribed;
		volumeSubscribed = this.volumeService.volumesWithUnderwriteOneCustomer();

		result = new ModelAndView("volume/list");
		result.addObject("volumes", volumes);
		result.addObject("volumeSubscribed", volumeSubscribed);
		result.addObject("requestURI", "volume/customer/list.do");
		result.addObject("showButtonSubscription", true);
		return result;

	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		final ModelAndView result;
		Volume volume = new Volume();
		Collection<Newspaper> newspapers;
		volume = this.volumeService.findOne(volumeId);
		newspapers = this.volumeService.volumesNewspaper(volumeId);

		result = new ModelAndView("volume/display");
		result.addObject("volume", volume);
		result.addObject("newspapers", newspapers);

		result.addObject("requestURI", "volume/customer/display.do");

		return result;
	}

}
