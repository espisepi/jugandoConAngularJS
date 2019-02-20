
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import cz.jirutka.validator.collection.constraints.EachURL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "draftMode,publishedMoment,title,body,summary,publishedMoment,newspaper_id,writer_id")
})
public class Article extends DomainEntity {

	// Attributes -------------------------------------------------------------------

	private String				title;
	private Date				publishedMoment;
	private String				summary;
	private String				body;
	private Collection<String>	pictures;
	private boolean				draftMode;
	private String				preSummary;


	@NotBlank
	@Size(min = 9, max = 500)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public boolean isDraftMode() {
		return this.draftMode;
	}

	public void setDraftMode(final boolean draftMode) {
		this.draftMode = draftMode;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	public Date getPublishedMoment() {
		return this.publishedMoment;
	}

	public void setPublishedMoment(final Date publishedMoment) {
		this.publishedMoment = publishedMoment;
	}

	@NotBlank
	public String getBody() {
		return this.body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	@ElementCollection
	@EachURL
	public Collection<String> getPictures() {
		return this.pictures;
	}

	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}

	@Transient
	public String getPreSummary() {
		if (this.summary.length() < 25)
			this.preSummary = this.summary.substring(0, 9) + "...";
		else
			this.preSummary = this.summary.substring(0, 25) + "...";

		return this.preSummary;
	}

	public void setPreSummary(final String preSummary) {
		this.preSummary = preSummary;
	}


	// Relationships---------------------------------------------------------------

	private User					writer;
	private Newspaper				newspaper;
	private Collection<FollowUp>	followUps;


	@ManyToOne(optional = false)
	@NotNull
	@Valid
	public User getWriter() {
		return this.writer;
	}

	public void setWriter(final User writer) {
		this.writer = writer;
	}

	@ManyToOne(optional = false)
	@Valid
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(final Newspaper newspaper) {
		this.newspaper = newspaper;
	}

	@OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
	@Valid
	public Collection<FollowUp> getFollowUps() {
		return this.followUps;
	}

	public void setFollowUps(final Collection<FollowUp> followUps) {
		this.followUps = followUps;
	}

}
