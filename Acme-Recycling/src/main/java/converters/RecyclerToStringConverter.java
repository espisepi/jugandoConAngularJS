
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Recycler;

@Component
@Transactional
public class RecyclerToStringConverter implements Converter<Recycler, String> {

	@Override
	public String convert(final Recycler recycler) {
		String result;

		if (recycler == null)
			result = null;
		else
			result = String.valueOf(recycler.getId());
		return result;
	}
}
