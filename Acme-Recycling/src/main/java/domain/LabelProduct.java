
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
public class LabelProduct extends Label {

	// Relationships ---------------------------------------------------------------

	private Collection<Item>	items;


	@OneToMany(mappedBy = "labelProduct")
	@Valid
	@NotNull
	public Collection<Item> getItems() {
		return this.items;
	}

	public void setItems(final Collection<Item> items) {
		this.items = items;
	}
}
