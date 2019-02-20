
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class Customer extends Actor {

	//Relationships-------------------------------------------------------------

	private Collection<Subscription>	subcriptions;

	private Collection<Underwrite>		underwrites;


	@OneToMany(mappedBy = "customer")
	@Valid
	public Collection<Subscription> getSubcriptions() {
		return this.subcriptions;
	}

	public void setSubcriptions(final Collection<Subscription> subcriptions) {
		this.subcriptions = subcriptions;
	}

	@OneToMany
	@Valid
	public Collection<Underwrite> getUnderwrites() {
		return this.underwrites;
	}

	public void setUnderwrites(final Collection<Underwrite> underwrites) {
		this.underwrites = underwrites;
	}

}
