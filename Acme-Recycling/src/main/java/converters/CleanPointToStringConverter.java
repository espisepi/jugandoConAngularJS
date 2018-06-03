
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CleanPoint;

@Component
@Transactional
public class CleanPointToStringConverter implements Converter<CleanPoint, String> {

	@Override
	public String convert(final CleanPoint CleanPoint) {
		String result;

		if (CleanPoint == null)
			result = null;
		else
			result = String.valueOf(CleanPoint.getId());
		return result;
	}
}
