
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.LegislationRepository;
import domain.Legislation;

@Component
@Transactional
public class StringToLegislationConverter implements Converter<String, Legislation> {

	@Autowired
	private LegislationRepository	legislationRepository;


	@Override
	public Legislation convert(final String text) {

		Legislation result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.legislationRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
