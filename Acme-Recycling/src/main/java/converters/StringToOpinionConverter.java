
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.OpinionRepository;
import domain.Opinion;

@Component
@Transactional
public class StringToOpinionConverter implements Converter<String, Opinion> {

	@Autowired
	private OpinionRepository	OpinionRepository;


	@Override
	public Opinion convert(final String text) {

		Opinion result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.OpinionRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
