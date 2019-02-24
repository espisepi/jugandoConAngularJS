
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Validacion;

@Component
@Transactional
public class ValidacionToStringConverter implements Converter<Validacion, String> {

	@Override
	public String convert(final Validacion Validacion) {
		String result;

		if (Validacion == null)
			result = null;
		else
			result = String.valueOf(Validacion.getId());
		return result;
	}

}
