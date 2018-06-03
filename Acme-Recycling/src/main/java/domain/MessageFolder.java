
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "actor_id,name")
})
public class MessageFolder extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	name;
	private boolean	modifiable;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public boolean isModifiable() {
		return this.modifiable;
	}

	public void setModifiable(final boolean modifiable) {
		this.modifiable = modifiable;
	}


	// Relationships ---------------------------------------------------------------

	private Actor	actor;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
