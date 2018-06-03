
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
public class Manager extends Actor {

	// Relationships ---------------------------------------------------------------

	private Collection<Incidence>	incidences;
	private Collection<Request>		requests;


	@OneToMany
	@Valid
	@NotNull
	public Collection<Incidence> getIncidences() {
		return this.incidences;
	}

	public void setIncidences(final Collection<Incidence> incidences) {
		this.incidences = incidences;
	}

	@OneToMany
	@Valid
	@NotNull
	public Collection<Request> getRequests() {
		return this.requests;
	}

	public void setRequests(final Collection<Request> requests) {
		this.requests = requests;
	}

}
