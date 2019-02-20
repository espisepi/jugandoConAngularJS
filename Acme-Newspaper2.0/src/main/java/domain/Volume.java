
package domain;

import java.beans.Transient;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class Volume extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	title;
	private String	description;
	private int		year;


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

	@Range(min = 1000, max = 3000)
	public int getYear() {
		return this.year;
	}

	public void setYear(final int year) {
		this.year = year;
	}
	@Transient
	public boolean isAllPublic(final Volume volume) {
		boolean result;
		result = true;
		for (final Newspaper n : volume.getNewspapers())
			if (n.isOpen() == false)
				result = false;
		return result;
	}


	//Relationships-------------------------------------------------------------

	private Collection<Newspaper>	newspapers;


	@ManyToMany
	@Valid
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

}
