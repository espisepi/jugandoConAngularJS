
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.ChirpRepository;
import domain.Chirp;

@Component
@Transactional
public class StringToChirpConverter implements Converter<String, Chirp> {

	@Autowired
	private ChirpRepository	ChirpRepository;


	@Override
	public Chirp convert(final String text) {

		Chirp result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.ChirpRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
