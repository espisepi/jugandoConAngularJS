
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Legislation;

@Component
@Transactional
public class LegislationToStringConverter implements Converter<Legislation, String> {

	@Override
	public String convert(final Legislation law) {
		String result;

		if (law == null)
			result = null;
		else
			result = String.valueOf(law.getId());
		return result;
	}
}
