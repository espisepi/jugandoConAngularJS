
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.BuyerRepository;
import domain.Buyer;

@Component
@Transactional
public class StringToBuyerConverter implements Converter<String, Buyer> {

	@Autowired
	private BuyerRepository	buyerRepository;


	@Override
	public Buyer convert(final String text) {

		Buyer result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.buyerRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
