
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.MessageFolder;

@Component
@Transactional
public class MessageFolderToStringConverter implements Converter<MessageFolder, String> {

	@Override
	public String convert(final MessageFolder MessageFolder) {
		String result;

		if (MessageFolder == null)
			result = null;
		else
			result = String.valueOf(MessageFolder.getId());
		return result;
	}
}
