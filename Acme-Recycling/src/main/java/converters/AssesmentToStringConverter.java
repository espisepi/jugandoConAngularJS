
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Assesment;

@Component
@Transactional
public class AssesmentToStringConverter implements Converter<Assesment, String> {

	@Override
	public String convert(final Assesment Assesment) {
		String result;

		if (Assesment == null)
			result = null;
		else
			result = String.valueOf(Assesment.getId());
		return result;
	}
}
