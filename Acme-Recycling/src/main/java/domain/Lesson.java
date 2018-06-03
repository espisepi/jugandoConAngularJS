
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title,summary,course_id")
})
public class Lesson extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	title;
	private String	summary;
	private int		number;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	@Size(min = 10, max = 50)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(final int number) {
		this.number = number;
	}


	//relations
	private Course	course;


	@ManyToOne(cascade = CascadeType.REMOVE)
	@Valid
	public Course getCourse() {
		return this.course;
	}

	public void setCourse(final Course course) {
		this.course = course;
	}

}
