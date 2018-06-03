
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CleanPointRepository;
import domain.CleanPoint;

@Component
@Transactional
public class StringToCleanPointConverter implements Converter<String, CleanPoint> {

	@Autowired
	private CleanPointRepository	cleanPointRepository;


	@Override
	public CleanPoint convert(final String text) {

		CleanPoint result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.cleanPointRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
