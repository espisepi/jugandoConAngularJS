
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Newscast;

@Component
@Transactional
public class NewscastToStringConverter implements Converter<Newscast, String> {

	@Override
	public String convert(final Newscast newscast) {
		String result;

		if (newscast == null)
			result = null;
		else
			result = String.valueOf(newscast.getId());
		return result;
	}
}
