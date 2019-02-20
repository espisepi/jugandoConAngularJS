
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.VolumeRepository;
import domain.Volume;

@Component
@Transactional
public class StringToVolumeConverter implements Converter<String, Volume> {

	@Autowired
	private VolumeRepository	VolumeRepository;


	@Override
	public Volume convert(final String text) {

		Volume result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.VolumeRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
