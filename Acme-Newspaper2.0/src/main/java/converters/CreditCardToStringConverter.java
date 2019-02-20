
package converters;

import java.net.URLEncoder;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(CreditCard creditCard) {
		String result;
		StringBuilder builder;

		if (creditCard == null)
			result = null;
		else
			try {
				builder = new StringBuilder();
				builder.append(URLEncoder.encode(creditCard.getHolderName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getBrandName(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getNumber(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getExpirationMonth(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getExpirationYear(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(Integer.toString(creditCard.getCvv()), "UTF-8"));
				result = builder.toString();
			} catch (final Throwable oops) {
				throw new IllegalArgumentException(oops);

			}
		return result;
	}
}
