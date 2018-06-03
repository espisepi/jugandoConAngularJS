
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Material;

@Component
@Transactional
public class MaterialToStringConverter implements Converter<Material, String> {

	@Override
	public String convert(final Material Material) {
		String result;

		if (Material == null)
			result = null;
		else
			result = String.valueOf(Material.getId());
		return result;
	}
}
