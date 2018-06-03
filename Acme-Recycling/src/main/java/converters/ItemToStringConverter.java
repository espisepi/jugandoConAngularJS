
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Item;

@Component
@Transactional
public class ItemToStringConverter implements Converter<Item, String> {

	@Override
	public String convert(final Item Item) {
		String result;

		if (Item == null)
			result = null;
		else
			result = String.valueOf(Item.getId());
		return result;
	}
}
