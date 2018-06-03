
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ConfigurationSystemRepository;
import domain.ConfigurationSystem;
import domain.Legislation;
import domain.TabooWord;

@Service
@Transactional
public class ConfigurationSystemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ConfigurationSystemRepository	configurationSystemRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdminService					adminService;

	//Importar la que pertenece a Spring
	@Autowired
	private Validator						validator;


	// Constructors -----------------------------------------------------------

	public ConfigurationSystemService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Collection<ConfigurationSystem> findAll() {
		Collection<ConfigurationSystem> result;

		Assert.notNull(this.configurationSystemRepository);
		result = this.configurationSystemRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public ConfigurationSystem findOne() {
		ConfigurationSystem res;

		res = this.findAll().iterator().next();
		Assert.notNull(res);

		return res;

	}

	public ConfigurationSystem save(final ConfigurationSystem configurationSystem) {
		Assert.notNull(configurationSystem);

		this.adminService.checkPrincipal();

		ConfigurationSystem result;

		result = this.configurationSystemRepository.save(configurationSystem);

		return result;
	}

	// Other business methods -------------------------------------------------
	public void flush() {
		this.configurationSystemRepository.flush();
	}

	public ConfigurationSystem findConfigurationSystem() {

		ConfigurationSystem result;

		result = this.configurationSystemRepository.findConfigurationSystem();

		return result;
	}

	//RECONSTRUCTOR----
	public ConfigurationSystem reconstruct(final ConfigurationSystem conf, final BindingResult bindingResult) {
		ConfigurationSystem result;
		ConfigurationSystem confBD;
		if (conf.getId() == 0) {

			Collection<TabooWord> tabooWords;
			Collection<Legislation> laws;
			tabooWords = new ArrayList<TabooWord>();
			laws = new ArrayList<Legislation>();
			conf.setTabooWords(tabooWords);
			conf.setLaws(laws);
			result = conf;
		} else {
			confBD = this.configurationSystemRepository.findOne(conf.getId());
			conf.setId(confBD.getId());
			conf.setVersion(confBD.getVersion());
			conf.setTabooWords(confBD.getTabooWords());
			conf.setLaws(confBD.getLaws());

			result = conf;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

}
