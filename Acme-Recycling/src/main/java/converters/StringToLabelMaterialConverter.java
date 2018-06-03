
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.LabelMaterialRepository;
import domain.LabelMaterial;

@Component
@Transactional
public class StringToLabelMaterialConverter implements Converter<String, LabelMaterial> {

	@Autowired
	private LabelMaterialRepository	labelMaterialRepository;


	@Override
	public LabelMaterial convert(final String text) {

		LabelMaterial result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.labelMaterialRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
