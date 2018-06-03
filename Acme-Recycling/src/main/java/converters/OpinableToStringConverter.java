
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Opinable;

@Component
@Transactional
public class OpinableToStringConverter implements Converter<Opinable, String> {

	@Override
	public String convert(final Opinable Opinable) {
		String result;

		if (Opinable == null)
			result = null;
		else
			result = String.valueOf(Opinable.getId());
		return result;
	}
}
