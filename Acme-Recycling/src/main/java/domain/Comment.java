
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	body;
	private Date	createdMoment;


	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	@Past
	public Date getCreatedMoment() {
		return this.createdMoment;
	}

	public void setCreatedMoment(final Date createdMoment) {
		this.createdMoment = createdMoment;
	}


	// Relationships ---------------------------------------------------------------

	private Collection<Comment>	replys;
	private Comment				commentTo;


	@Valid
	@NotNull
	@OneToMany(mappedBy = "commentTo")
	public Collection<Comment> getReplys() {
		return this.replys;
	}

	public void setReplys(final Collection<Comment> replys) {
		this.replys = replys;
	}

	@Valid
	@ManyToOne(optional = true)
	public Comment getCommentTo() {
		return this.commentTo;
	}

	public void setCommentTo(final Comment commentTo) {
		this.commentTo = commentTo;
	}

}
