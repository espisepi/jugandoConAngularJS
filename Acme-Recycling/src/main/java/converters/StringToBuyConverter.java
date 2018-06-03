
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.BuyRepository;
import domain.Buy;

@Component
@Transactional
public class StringToBuyConverter implements Converter<String, Buy> {

	@Autowired
	private BuyRepository	buyRepository;


	@Override
	public Buy convert(final String text) {

		Buy result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.buyRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
