
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Advertisement;

@Component
@Transactional
public class AdvertisementToStringConverter implements Converter<Advertisement, String> {

	@Override
	public String convert(final Advertisement Advertisement) {
		String result;

		if (Advertisement == null)
			result = null;
		else
			result = String.valueOf(Advertisement.getId());
		return result;
	}
}
