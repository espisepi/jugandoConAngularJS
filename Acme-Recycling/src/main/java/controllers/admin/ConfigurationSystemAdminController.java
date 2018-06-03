
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationSystemService;
import controllers.AbstractController;
import domain.ConfigurationSystem;
import domain.Legislation;
import domain.TabooWord;

@Controller
@RequestMapping(value = "/configurationSystem/admin")
public class ConfigurationSystemAdminController extends AbstractController {

	//Services--------------------------------------------

	@Autowired
	private ConfigurationSystemService	configurationSystemService;


	//Constructor--------------------------------------------------------

	public ConfigurationSystemAdminController() {
		super();
	}

	//Listing------------------------------------------------------------

	@RequestMapping(value = "tabooWord/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		ConfigurationSystem configurationSystem;
		Collection<TabooWord> tabooWords;

		configurationSystem = this.configurationSystemService.findConfigurationSystem();
		tabooWords = configurationSystem.getTabooWords();

		result = new ModelAndView("configurationSystem/listTabooWord");
		result.addObject("tabooWords", tabooWords);
		result.addObject("requestURI", "configurationSystem/admin/tabooWord/list.do?d-16544-p=1");
		return result;

	}

	//Listing------------------------------------------------------------

	@RequestMapping(value = "legislation/list", method = RequestMethod.GET)
	public ModelAndView listLegislation() {

		ModelAndView result;
		ConfigurationSystem configurationSystem;
		Collection<Legislation> laws;

		configurationSystem = this.configurationSystemService.findConfigurationSystem();
		laws = configurationSystem.getLaws();

		result = new ModelAndView("configurationSystem/listLegislation");
		result.addObject("laws", laws);
		result.addObject("requestURI", "configurationSystem/admin/legislation/list.do?d-16544-p=1");
		return result;

	}

	//Editing-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {

		ModelAndView result;
		ConfigurationSystem configurationSystem;

		configurationSystem = this.configurationSystemService.findOne();
		Assert.notNull(configurationSystem);

		result = this.createEditModelAndView(configurationSystem);

		return result;

	}

	//Saving-----------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(ConfigurationSystem configurationSystem, final BindingResult binding) {

		configurationSystem = this.configurationSystemService.reconstruct(configurationSystem, binding);
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(configurationSystem);
		else
			try {
				this.configurationSystemService.save(configurationSystem);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(configurationSystem, "configurationSystem.commit.error");
			}

		return result;

	}

	//auxiliary------------------

	protected ModelAndView createEditModelAndView(final ConfigurationSystem configurationSystem) {

		Assert.notNull(configurationSystem);
		ModelAndView result;
		result = this.createEditModelAndView(configurationSystem, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final ConfigurationSystem configurationSystem, final String messageCode) {
		Assert.notNull(configurationSystem);

		ModelAndView result;

		result = new ModelAndView("configurationSystem/edit");
		result.addObject("configurationSystem", configurationSystem);
		result.addObject("message", messageCode);
		result.addObject("RequestURI", "configurationSystem/admin/edit.do");

		return result;

	}

}
