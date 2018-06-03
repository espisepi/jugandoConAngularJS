
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.ConfigurationSystem;

@Component
@Transactional
public class ConfigurationSystemToStringConverter implements Converter<ConfigurationSystem, String> {

	@Override
	public String convert(final ConfigurationSystem configurationSystem) {
		String result;

		if (configurationSystem == null)
			result = null;
		else
			result = String.valueOf(configurationSystem.getId());
		return result;
	}

}
