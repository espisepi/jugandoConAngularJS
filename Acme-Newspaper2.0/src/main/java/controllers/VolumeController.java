
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import services.VolumeService;
import domain.Newspaper;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	// Services---------------------------------------------------------

	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private NewspaperService	newspaperService;


	//Constructor--------------------------------------------------------

	public VolumeController() {
		super();
	}

	//Listing-----------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(final String messageCode) {
		ModelAndView result;
		Collection<Volume> volumes;

		volumes = this.volumeService.findAll();

		result = new ModelAndView("volume/list");
		result.addObject("volumes", volumes);
		result.addObject("requestURI", "volume/list.do?d-16544-p=1");
		result.addObject("message", messageCode);
		return result;
	}

	// Display ----------------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int volumeId) {
		ModelAndView result;

		Collection<Newspaper> publicNewspapers;

		publicNewspapers = this.newspaperService.findAllNewspapersPublicByVolumeId(volumeId);

		result = new ModelAndView("newspaper/display");
		result.addObject("newspaper", publicNewspapers);
		result.addObject("requestURI", "newspaper/display.do");

		return result;
	}
}
