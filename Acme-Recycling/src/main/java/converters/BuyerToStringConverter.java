
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Buyer;

@Component
@Transactional
public class BuyerToStringConverter implements Converter<Buyer, String> {

	@Override
	public String convert(final Buyer buyer) {
		String result;

		if (buyer == null)
			result = null;
		else
			result = String.valueOf(buyer.getId());
		return result;
	}
}
