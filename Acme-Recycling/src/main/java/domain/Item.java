
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "publicationMoment,value")
})
public class Item extends Opinable {

	// Attributes -------------------------------------------------------------

	private String	title;
	private Date	publicationMoment;
	private String	description;
	private Double	quantity;
	private String	photo;
	private int		value;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	@Past
	@NotNull
	public Date getPublicationMoment() {
		return this.publicationMoment;
	}

	public void setPublicationMoment(final Date publicationMoment) {
		this.publicationMoment = publicationMoment;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Min(0)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(final Double quantity) {
		this.quantity = quantity;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@Range(min = 0, max = 500)
	public int getValue() {
		return this.value;
	}

	public void setValue(final int value) {
		this.value = value;
	}


	// Relationships ---------------------------------------------------------------

	private LabelProduct	labelProduct;
	private Recycler		recycler;
	private Request			request;


	@ManyToOne(optional = false)
	@Valid
	@NotNull
	public LabelProduct getLabelProduct() {
		return this.labelProduct;
	}

	public void setLabelProduct(final LabelProduct labelProduct) {
		this.labelProduct = labelProduct;
	}

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Recycler getRecycler() {
		return this.recycler;
	}

	public void setRecycler(final Recycler recycler) {
		this.recycler = recycler;
	}

	@Valid
	@OneToOne(optional = true)
	public Request getRequest() {
		return this.request;
	}

	public void setRequest(final Request request) {
		this.request = request;
	}

}
