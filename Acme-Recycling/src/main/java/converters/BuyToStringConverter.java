
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Buy;

@Component
@Transactional
public class BuyToStringConverter implements Converter<Buy, String> {

	@Override
	public String convert(final Buy Buy) {
		String result;

		if (Buy == null)
			result = null;
		else
			result = String.valueOf(Buy.getId());
		return result;
	}
}
