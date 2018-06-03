
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title,description")
})
public class Course extends Opinable {

	// Attributes -------------------------------------------------------------

	private String	title;
	private String	description;
	private GPS		location;
	private Date	realisedMoment;
	private String	picture;
	private boolean	draftMode;
	private Integer	minimumScore;


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
	@Valid
	public GPS getLocation() {
		return this.location;
	}

	public void setLocation(final GPS location) {
		this.location = location;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getRealisedMoment() {
		return this.realisedMoment;
	}

	public void setRealisedMoment(final Date realisedMoment) {
		this.realisedMoment = realisedMoment;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public boolean isDraftMode() {
		return this.draftMode;
	}

	public void setDraftMode(final boolean draftMode) {
		this.draftMode = draftMode;
	}

	@NotNull
	public Integer getMinimumScore() {
		return this.minimumScore;
	}

	public void setMinimumScore(final Integer minimumScore) {
		this.minimumScore = minimumScore;
	}


	// Relationships ---------------------------------------------------------------

	private Collection<Material>	materials;
	private Collection<Lesson>		lessons;


	@ManyToMany
	@Valid
	@NotNull
	public Collection<Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(final Collection<Material> materials) {
		this.materials = materials;
	}

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
	public Collection<Lesson> getLessons() {
		return this.lessons;
	}

	public void setLessons(final Collection<Lesson> lessons) {
		this.lessons = lessons;
	}

}
