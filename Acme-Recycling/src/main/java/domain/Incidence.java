
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "resolved")
})
public class Incidence extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	title;
	private String	reasonWhy;
	private Date	createdMoment;
	private String	comment;
	private boolean	resolved;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getReasonWhy() {
		return this.reasonWhy;
	}

	public void setReasonWhy(final String reasonWhy) {
		this.reasonWhy = reasonWhy;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	@Past
	@NotNull
	public Date getCreatedMoment() {
		return this.createdMoment;
	}

	public void setCreatedMoment(final Date createdMoment) {
		this.createdMoment = createdMoment;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public boolean isResolved() {
		return this.resolved;
	}

	public void setResolved(final boolean resolved) {
		this.resolved = resolved;
	}


	// Relationships ---------------------------------------------------------------

	private Recycler	recycler;


	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public Recycler getRecycler() {
		return this.recycler;
	}

	public void setRecycler(final Recycler recycler) {
		this.recycler = recycler;
	}

}
