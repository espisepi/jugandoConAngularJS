package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class TabooWord extends DomainEntity{
	
	private String name;
	private boolean default_word;

	@NotBlank
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefault_word() {
		return default_word;
	}


	
	public void setDefault_word(boolean default_word) {
		this.default_word = default_word;
	}
	
	

}
