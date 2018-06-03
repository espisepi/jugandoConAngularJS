
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Label;

@Component
@Transactional
public class LabelToStringConverter implements Converter<Label, String> {

	@Override
	public String convert(final Label label) {

		String result;

		if (label == null)
			result = null;
		else
			result = String.valueOf(label.getId());

		return result;
	}

}
