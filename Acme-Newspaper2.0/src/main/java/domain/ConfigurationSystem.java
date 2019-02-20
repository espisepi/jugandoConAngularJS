
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Access(AccessType.PROPERTY)
public class ConfigurationSystem extends DomainEntity {
	
	// Relationships---------------------------------------------------------------

	private Collection<TabooWord>	tabooWords;
	
	@OneToMany
	@NotEmpty
	@NotNull
	@Valid
	public Collection<TabooWord> getTabooWords() {
		return this.tabooWords;
	}

	public void setTabooWords(final Collection<TabooWord> tabooWords) {
		this.tabooWords = tabooWords;
	}


	

}
