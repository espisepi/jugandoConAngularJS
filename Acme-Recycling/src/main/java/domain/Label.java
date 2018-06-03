
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Label extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	name;
	private boolean	byDefault;


	@Column(name = "name", unique = true)
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@NotNull
	public Boolean getByDefault() {
		return this.byDefault;
	}

	public void setByDefault(final Boolean byDefault) {
		this.byDefault = byDefault;
	}

}
