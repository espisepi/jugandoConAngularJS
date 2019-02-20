
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.UnderwriteRepository;
import domain.Underwrite;

@Component
@Transactional
public class StringToUnderwriteConverter implements Converter<String, Underwrite> {

	@Autowired
	private UnderwriteRepository	UnderwriteRepository;


	@Override
	public Underwrite convert(final String text) {

		Underwrite result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.UnderwriteRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
