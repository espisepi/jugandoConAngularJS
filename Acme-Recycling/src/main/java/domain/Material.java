
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title,description,unitPrice")
})
public class Material extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	title;
	private String	description;
	private Double	unitPrice;
	private Double	quantity;
	private Double	totalPrice;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	@NotNull
	@Min(0)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(final Double unitPrice) {
		this.unitPrice = unitPrice;
	}
	@NotNull
	@Range(min = 0, max = 100000)
	@Digits(integer = 99, fraction = 2)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(final Double quantity) {
		this.quantity = quantity;
	}

	@NotNull
	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(final Double totalPrice) {
		this.totalPrice = totalPrice;
	}


	// Relationships ---------------------------------------------------------------

	private LabelMaterial	labelMaterial;
	private Collection<Buy>	buys;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public LabelMaterial getLabelMaterial() {
		return this.labelMaterial;
	}

	public void setLabelMaterial(final LabelMaterial labelMaterial) {
		this.labelMaterial = labelMaterial;
	}

	@OneToMany(mappedBy = "material")
	@Valid
	@NotNull
	public Collection<Buy> getBuys() {
		return this.buys;
	}

	public void setBuys(final Collection<Buy> buys) {
		this.buys = buys;
	}

}
