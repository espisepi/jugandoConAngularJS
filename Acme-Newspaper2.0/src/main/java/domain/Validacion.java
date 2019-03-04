
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Access(AccessType.PROPERTY)
public class Validacion extends DomainEntity {

	private String	textMaxLength;
	private String	email;
	private Integer	numberMax;
	private Integer	numberMin;
	private String	textPattern;
	private String	url;


	@NotNull
	public String getTextMaxLength() {
		return this.textMaxLength;
	}

	public void setTextMaxLength(final String textMaxLength) {
		this.textMaxLength = textMaxLength;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotNull
	public Integer getNumberMax() {
		return this.numberMax;
	}

	public void setNumberMax(final Integer numberMax) {
		this.numberMax = numberMax;
	}

	@NotNull
	public Integer getNumberMin() {
		return this.numberMin;
	}

	public void setNumberMin(final Integer numberMin) {
		this.numberMin = numberMin;
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
	@JsonIgnore
	@Valid
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

}
