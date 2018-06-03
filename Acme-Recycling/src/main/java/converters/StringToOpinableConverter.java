
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.OpinableRepository;
import domain.Opinable;

@Component
@Transactional
public class StringToOpinableConverter implements Converter<String, Opinable> {

	@Autowired
	private OpinableRepository	OpinableRepository;


	@Override
	public Opinable convert(final String text) {

		Opinable result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.OpinableRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
