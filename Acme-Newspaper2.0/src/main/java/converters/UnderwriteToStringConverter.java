
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Underwrite;

@Component
@Transactional
public class UnderwriteToStringConverter implements Converter<Underwrite, String> {

	@Override
	public String convert(final Underwrite Underwrite) {
		String result;

		if (Underwrite == null)
			result = null;
		else
			result = String.valueOf(Underwrite.getId());
		return result;
	}
}
