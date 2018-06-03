
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.LabelProductRepository;
import domain.LabelProduct;

@Component
@Transactional
public class StringToLabelProductConverter implements Converter<String, LabelProduct> {

	@Autowired
	private LabelProductRepository	labelProductRepository;


	@Override
	public LabelProduct convert(final String text) {

		LabelProduct result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.labelProductRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
