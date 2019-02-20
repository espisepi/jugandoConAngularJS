
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services ---------------------------------------------------------------
	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private UserService			userService;

	@Autowired
	private VolumeService		volumeService;


	//List my volumen-----------------------------------------------------------

	@RequestMapping(value = "/mylist", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Volume> volumes;
		volumes = this.volumeService.myVolumes();
		result = new ModelAndView("volume/mylist");
		result.addObject("volumes", volumes);
		result.addObject("requestURI", "volume/user/mylist.do");
		return result;

	}
	//Creation----------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Volume volume;

		volume = this.volumeService.create();

		result = this.createEditModelAndView(volume);
		return result;
	}

	//Edition--------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int volumeId) {
		ModelAndView result;
		Volume volume;
		User user;

		user = this.userService.findByPrincipal();
		volume = this.volumeService.findOne(volumeId);
		Assert.isTrue(user.getVolumes().contains(volume), "Cannot commit this operation, because it's illegal");
		Assert.notNull(volume);
		result = this.createEditModelAndView(volume);
		return result;
	}

	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Volume volume, final BindingResult bindingResult) {
		ModelAndView result;

		//Si entra aqui es que no se ha pulsado ninguna opcion en el formulario
		if (volume.getNewspapers() == null)
			volume.setNewspapers(new ArrayList<Newspaper>());
		//Si entra aqui es que se ha pulsado la opcion de "----" del formulario
		if (volume.getNewspapers().contains(null))
			volume.getNewspapers().remove(null);

		volume = this.volumeService.reconstruct(volume, bindingResult);
		if (bindingResult.hasErrors())
			result = this.createEditModelAndView(volume);
		else
			try {
				this.volumeService.save(volume);
				result = new ModelAndView("redirect:mylist.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("The volume must have at least one newspaper"))
					result = this.createEditModelAndView(volume, "volume.newspaperEmpty.error");
				else
					result = this.createEditModelAndView(volume, "volume.commit.error");
			}
		return result;
	}
	public ModelAndView createEditModelAndView(final Volume volume) {

		Assert.notNull(volume);
		ModelAndView result;
		result = this.createEditModelAndView(volume, null);
		return result;
	}

	public ModelAndView createEditModelAndView(final Volume volume, final String messageCode) {
		assert volume != null;
		Volume volumeBD;

		ModelAndView result;
		Collection<Newspaper> newspapers;
		Collection<Newspaper> newspapersprivate;

		if (volume.getId() == 0)
			//Cuando lo creamos que salgan todos los periódicos del user
			newspapers = this.newspaperService.findNewspapersCreatedByUserAndNotPublished();
		else {
			//Cuando lo editamos que salgan solo los periódicos publicos que no esten publicados
			newspapers = this.newspaperService.findAllNewspapersPublicByUserNotPublished();
			newspapersprivate = this.newspaperService.findAllNewspapersPrivateByUserNotPublished();
			volumeBD = this.volumeService.findOne(volume.getId());
			newspapersprivate.removeAll(volumeBD.getNewspapers());
			newspapers.addAll(newspapersprivate);
		}
		result = new ModelAndView("volume/edit");
		result.addObject("volume", volume);
		result.addObject("newspapers", newspapers);
		result.addObject("message", messageCode);
		return result;

	}
}
