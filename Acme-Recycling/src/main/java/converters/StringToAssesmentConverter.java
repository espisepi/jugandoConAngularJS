
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.AssesmentRepository;
import domain.Assesment;

@Component
@Transactional
public class StringToAssesmentConverter implements Converter<String, Assesment> {

	@Autowired
	private AssesmentRepository	AssesmentRepository;


	@Override
	public Assesment convert(final String text) {

		Assesment result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.AssesmentRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
