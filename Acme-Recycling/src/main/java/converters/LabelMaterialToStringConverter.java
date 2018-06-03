
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.LabelMaterial;

@Component
@Transactional
public class LabelMaterialToStringConverter implements Converter<LabelMaterial, String> {

	@Override
	public String convert(final LabelMaterial CategoryMaterial) {
		String result;

		if (CategoryMaterial == null)
			result = null;
		else
			result = String.valueOf(CategoryMaterial.getId());
		return result;
	}
}
