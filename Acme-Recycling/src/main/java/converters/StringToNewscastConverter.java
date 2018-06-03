
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.NewscastRepository;
import domain.Newscast;

@Component
@Transactional
public class StringToNewscastConverter implements Converter<String, Newscast> {

	@Autowired
	private NewscastRepository	newRepository;


	@Override
	public Newscast convert(final String text) {

		Newscast result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.newRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
