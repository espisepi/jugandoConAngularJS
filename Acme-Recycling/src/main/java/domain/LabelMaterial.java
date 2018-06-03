
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
public class LabelMaterial extends Label {

	// Relationships ---------------------------------------------------------------

	private Collection<Material>	materials;


	@OneToMany(mappedBy = "labelMaterial")
	@Valid
	@NotNull
	public Collection<Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(final Collection<Material> materials) {
		this.materials = materials;
	}

}
