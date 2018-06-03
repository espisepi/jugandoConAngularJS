
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Opinion;

@Component
@Transactional
public class OpinionToStringConverter implements Converter<Opinion, String> {

	@Override
	public String convert(final Opinion Opinion) {
		String result;

		if (Opinion == null)
			result = null;
		else
			result = String.valueOf(Opinion.getId());
		return result;
	}
}
