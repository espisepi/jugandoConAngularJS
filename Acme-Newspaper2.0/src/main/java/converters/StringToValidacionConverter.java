
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ValidacionRepository;
import domain.Validacion;

@Component
@Transactional
public class StringToValidacionConverter implements Converter<String, Validacion> {

	@Autowired
	ValidacionRepository	ValidacionRepository;


	@Override
	public Validacion convert(final String text) {
		Validacion result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.ValidacionRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
