
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Editor;

@Component
@Transactional
public class EditorToStringConverter implements Converter<Editor, String> {

	@Override
	public String convert(final Editor editor) {
		String result;

		if (editor == null)
			result = null;
		else
			result = String.valueOf(editor.getId());
		return result;
	}
}
