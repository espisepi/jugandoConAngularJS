
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.CarrierRepository;
import domain.Carrier;

@Component
@Transactional
public class StringToCarrierConverter implements Converter<String, Carrier> {

	@Autowired
	private CarrierRepository	carrierRepository;


	@Override
	public Carrier convert(final String text) {

		Carrier result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.carrierRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
