
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public abstract class Opinable extends DomainEntity {

	public Opinable() {

	}


	//Relationships--------------------------------------------------------------
	private Collection<Opinion>	opinions;


	@Valid
	@NotNull
	@OneToMany
	public Collection<Opinion> getOpinions() {
		return this.opinions;
	}

	public void setOpinions(final Collection<Opinion> opinions) {
		this.opinions = opinions;
	}

}
