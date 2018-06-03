
package controllers.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationSystemService;
import services.LegislationService;
import controllers.AbstractController;
import domain.ConfigurationSystem;
import domain.Legislation;

@Controller
@RequestMapping(value = "/legislation/admin")
public class LegislationAdminController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private LegislationService			legislationService;

	@Autowired
	private ConfigurationSystemService	configurationSystemService;


	//Constructor--------------------------------------------------------

	public LegislationAdminController() {
		super();
	}

	//Creating--------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {

		ModelAndView result;
		Legislation law;

		law = this.legislationService.create();

		result = this.createEditModelAndView(law);
		return result;

	}

	//Edition---------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int lawId) {
		ModelAndView result;
		Legislation law;

		law = this.legislationService.findOne(lawId);

		Assert.notNull(law);
		result = this.createEditModelAndView(law);
		return result;
	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Legislation legislation, final BindingResult binding) {

		ModelAndView result;
		ConfigurationSystem configurationSystem;
		Legislation legislationSaved;

		configurationSystem = this.configurationSystemService.findConfigurationSystem();
		legislation = this.legislationService.reconstruct(legislation, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(legislation);
		else
			try {

				if (legislation.getId() == 0) {

					legislationSaved = this.legislationService.save(legislation);
					configurationSystem.getLaws().add(legislationSaved);
					this.configurationSystemService.save(configurationSystem);
				} else
					legislationSaved = this.legislationService.save(legislation);

				result = new ModelAndView("redirect:/configurationSystem/admin/legislation/list.do?d-16544-p=1");
			} catch (final Throwable oops) {

				if (oops.getMessage().equals("palabra tabu en el titulo"))
					result = this.createEditModelAndView(legislation, "legislation.error.title");
				else if (oops.getMessage().equals("palabra tabu en el cuerpo"))
					result = this.createEditModelAndView(legislation, "legislation.error.title");

				else

					result = this.createEditModelAndView(legislation, "legislation.commit.error");
			}

		return result;

	}

	//Delete-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@Valid final Legislation law, final BindingResult binding) {

		ModelAndView result;
		ConfigurationSystem configurationSystem;

		configurationSystem = this.configurationSystemService.findConfigurationSystem();

		if (binding.hasErrors())
			result = this.createEditModelAndView(law);
		else
			try {
				configurationSystem.getLaws().remove(law);
				this.configurationSystemService.save(configurationSystem);
				this.legislationService.delete(law);
				result = new ModelAndView("redirect:/configurationSystem/admin/legislation/list.do?d-16544-p=1");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(law, "legislation.commit.error");
			}

		return result;

	}

	//Auxiliary-----------------------

	protected ModelAndView createEditModelAndView(final Legislation law) {

		Assert.notNull(law);
		ModelAndView result;
		result = this.createEditModelAndView(law, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Legislation law, final String messageCode) {
		assert law != null;

		ModelAndView result;

		result = new ModelAndView("legislation/edit");
		result.addObject("legislation", law);
		result.addObject("message", messageCode);

		return result;

	}

}
