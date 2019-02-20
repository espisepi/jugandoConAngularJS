
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.FollowUpRepository;
import domain.FollowUp;

@Component
@Transactional
public class StringToFollowUpConverter implements Converter<String, FollowUp> {

	@Autowired
	private FollowUpRepository	FollowUpRepository;


	@Override
	public FollowUp convert(final String text) {

		FollowUp result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.FollowUpRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
