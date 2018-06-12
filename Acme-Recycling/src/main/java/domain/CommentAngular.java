
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class CommentAngular extends DomainEntity {

	//Attributes
	private String	comment;


	//Constructor

	public CommentAngular() {
		super();
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}
}
