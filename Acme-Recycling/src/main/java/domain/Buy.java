
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Buy extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private CreditCard	creditCard;
	private Double		quantity;
	private String		comment;


	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotNull
	@Range(min = 0, max = 100000)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(final Double quantity) {
		this.quantity = quantity;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}


	// Relationships ---------------------------------------------------------------
	private Material	material;


	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Material getMaterial() {
		return this.material;
	}

	public void setMaterial(final Material material) {
		this.material = material;
	}

}
