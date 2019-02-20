package forms;

import javax.validation.Valid;

import domain.User;


public class UserForm {
	
	@Valid
	private User		user;
	private String		passwordCheck;
	private Boolean		conditions;

	
	public UserForm(){
		super();
	}
	
	public UserForm(final User user) {
		this.user = user;
		this.passwordCheck = "";
		this.conditions = false;
	}

	
	public User getUser() {
		return user;
	}

	
	public void setUser(User user) {
		this.user = user;
	}

	
	public String getPasswordCheck() {
		return passwordCheck;
	}

	
	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	
	public Boolean getConditions() {
		return conditions;
	}

	
	public void setConditions(Boolean conditions) {
		this.conditions = conditions;
	}
	
	
}
