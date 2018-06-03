package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.TabooWordRepository;
import domain.TabooWord;

@Component
@Transactional
public class StringToTabooWordConverter implements Converter<String, TabooWord>{
	
	@Autowired
	TabooWordRepository tabooWordRepository;
	
	@Override
	public TabooWord convert(final String text) {
		TabooWord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.tabooWordRepository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}

}
