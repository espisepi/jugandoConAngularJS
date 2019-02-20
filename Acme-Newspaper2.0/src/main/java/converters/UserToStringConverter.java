
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.User;

@Component
@Transactional
public class UserToStringConverter implements Converter<User, String> {

	@Override
	public String convert(final User User) {
		String result;

		if (User == null)
			result = null;
		else
			result = String.valueOf(User.getId());
		return result;
	}

}
