
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.RecyclerRepository;
import domain.Recycler;

@Component
@Transactional
public class StringToRecyclerConverter implements Converter<String, Recycler> {

	@Autowired
	private RecyclerRepository	recyclerRepository;


	@Override
	public Recycler convert(final String text) {

		Recycler result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.recyclerRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
