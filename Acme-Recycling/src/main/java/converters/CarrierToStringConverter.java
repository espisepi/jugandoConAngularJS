
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Carrier;

@Component
@Transactional
public class CarrierToStringConverter implements Converter<Carrier, String> {

	@Override
	public String convert(final Carrier carrier) {
		String result;

		if (carrier == null)
			result = null;
		else
			result = String.valueOf(carrier.getId());
		return result;
	}
}
