
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Lesson;

@Component
@Transactional
public class LessonToStringConverter implements Converter<Lesson, String> {

	@Override
	public String convert(final Lesson lesson) {
		String result;

		if (lesson == null)
			result = null;
		else
			result = String.valueOf(lesson.getId());
		return result;
	}
}
