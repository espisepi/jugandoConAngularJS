
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
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "actor_id,title,createdMoment,comment")
})
public class Opinion extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	title;
	private Date	createdMoment;
	private String	comment;
	private String	photo;
	private String	caption;


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
	public Date getCreatedMoment() {
		return this.createdMoment;
	}

	public void setCreatedMoment(final Date createdMoment) {
		this.createdMoment = createdMoment;
	}

	@NotBlank
	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	@URL
	@NotNull
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@NotNull
	public String getCaption() {
		return this.caption;
	}

	public void setCaption(final String caption) {
		this.caption = caption;
	}


	// Relationships ---------------------------------------------------------------

	private Actor	actor;


	@NotNull
	@Valid
	@ManyToOne
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}

}
