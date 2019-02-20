
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	//Relationships-----------------------------------------------------------------

	private Collection<Article>		articles;
	private Collection<Newspaper>	newspapers;
	private Collection<User>		followers;	// Seguidores
	private Collection<User>		followed;	// Seguidos
	private Collection<Chirp>		chirps;
	private Collection<Volume>		volumes;


	@OneToMany
	@Valid
	public Collection<Volume> getVolumes() {
		return this.volumes;
	}

	public void setVolumes(Collection<Volume> volumes) {
		this.volumes = volumes;
	}

	@OneToMany(mappedBy = "user")
	@Valid
	public Collection<Chirp> getChirps() {
		return this.chirps;
	}

	public void setChirps(final Collection<Chirp> chirps) {
		this.chirps = chirps;
	}

	@OneToMany(mappedBy = "publisher")
	@Valid
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

	@OneToMany(mappedBy = "writer")
	@Valid
	public Collection<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(final Collection<Article> articles) {
		this.articles = articles;
	}

	@ManyToMany(mappedBy = "followed")
	@Valid
	public Collection<User> getFollowers() {
		return this.followers;
	}

	public void setFollowers(final Collection<User> followers) {
		this.followers = followers;
	}

	@ManyToMany
	@Valid
	public Collection<User> getFollowed() {
		return this.followed;
	}

	public void setFollowed(final Collection<User> followed) {
		this.followed = followed;
	}

}
