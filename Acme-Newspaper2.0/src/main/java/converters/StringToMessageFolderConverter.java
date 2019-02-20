
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import repositories.MessageFolderRepository;
import domain.MessageFolder;

@Component
@Transactional
public class StringToMessageFolderConverter implements Converter<String, MessageFolder> {

	@Autowired
	private MessageFolderRepository	MessageFolderRepository;


	@Override
	public MessageFolder convert(final String text) {

		MessageFolder result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.MessageFolderRepository.findOne(id);

			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
