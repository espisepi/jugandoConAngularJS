
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Course;

@Component
@Transactional
public class CourseToStringConverter implements Converter<Course, String> {

	@Override
	public String convert(final Course Course) {
		String result;

		if (Course == null)
			result = null;
		else
			result = String.valueOf(Course.getId());
		return result;
	}
}
