
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Recycler extends Actor {

	// Relationships ---------------------------------------------------------------

	private Collection<Item>	items;
	private Collection<Comment>	comments;
	private Collection<Course>	courses;


	@OneToMany(mappedBy = "recycler")
	@Valid
	@NotNull
	public Collection<Item> getItems() {
		return this.items;
	}

	public void setItems(final Collection<Item> items) {
		this.items = items;
	}
	@OneToMany
	@Valid
	@NotNull
	public Collection<Comment> getComments() {
		return this.comments;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}
	@ManyToMany
	@Valid
	@NotNull
	public Collection<Course> getCourses() {
		return this.courses;
	}

	public void setCourses(final Collection<Course> courses) {
		this.courses = courses;
	}

}
