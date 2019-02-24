
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Validacion extends DomainEntity {

	private String	email;
	private Integer	numberMax;
	private Integer	numberMin;
	private String	textMaxLength;
	private String	textPattern;
	private String	url;


	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public Integer getNumberMax() {
		return this.numberMax;
	}

	public void setNumberMax(final Integer numberMax) {
		this.numberMax = numberMax;
	}

	public Integer getNumberMin() {
		return this.numberMin;
	}

	public void setNumberMin(final Integer numberMin) {
		this.numberMin = numberMin;
	}

	public String getTextMaxLength() {
		return this.textMaxLength;
	}

	public void setTextMaxLength(final String textMaxLength) {
		this.textMaxLength = textMaxLength;
	}

	public String getTextPattern() {
		return this.textPattern;
	}

	public void setTextPattern(final String textPattern) {
		this.textPattern = textPattern;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}


	//	Relationships ---------------------------------------------------------------

	private Newspaper	newspaper;


	@ManyToOne(optional = true)
	@Valid
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
