
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Newspaper;

@Component
@Transactional
public class NewspaperToStringConverter implements Converter<Newspaper, String> {

	@Override
	public String convert(final Newspaper Newspaper) {
		String result;

		if (Newspaper == null)
			result = null;
		else
			result = String.valueOf(Newspaper.getId());
		return result;
	}
}
