
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ConfigurationSystemRepository;
import domain.ConfigurationSystem;

@Component
@Transactional
public class StringToConfigurationSystemConverter implements Converter<String, ConfigurationSystem> {

	@Autowired
	ConfigurationSystemRepository	configurationSystemRepository;


	@Override
	public ConfigurationSystem convert(final String text) {
		ConfigurationSystem result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.configurationSystemRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
