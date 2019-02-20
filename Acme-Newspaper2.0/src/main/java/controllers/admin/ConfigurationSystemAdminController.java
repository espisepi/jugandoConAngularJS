package controllers.admin;


import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationSystemService;
import controllers.AbstractController;
import domain.ConfigurationSystem;
import domain.TabooWord;

@Controller
@RequestMapping(value = "/configurationSystem/admin")
public class ConfigurationSystemAdminController extends AbstractController{
	
	//Services--------------------------------------------

	@Autowired
	private ConfigurationSystemService	configurationSystemService;
	
	
	//Constructor--------------------------------------------------------
	
	public ConfigurationSystemAdminController(){
		super();
	}
	
	//Listing
	
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

	

}
