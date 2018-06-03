
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.LabelProduct;

@Component
@Transactional
public class LabelProductToStringConverter implements Converter<LabelProduct, String> {

	@Override
	public String convert(final LabelProduct CategoryProduct) {
		String result;

		if (CategoryProduct == null)
			result = null;
		else
			result = String.valueOf(CategoryProduct.getId());
		return result;
	}
}
