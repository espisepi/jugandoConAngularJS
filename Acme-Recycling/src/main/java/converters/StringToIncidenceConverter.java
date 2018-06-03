
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.IncidenceRepository;
import domain.Incidence;

@Component
@Transactional
public class StringToIncidenceConverter implements Converter<String, Incidence> {

	@Autowired
	private IncidenceRepository	incidenceRepository;


	@Override
	public Incidence convert(final String text) {

		Incidence result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.incidenceRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
