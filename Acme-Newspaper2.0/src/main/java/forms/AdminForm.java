package forms;

import javax.validation.Valid;

import domain.Admin;


public class AdminForm {
	
	@Valid
	private Admin			admin;
	private String			passwordCheck;
	private Boolean			conditions;
	
	public AdminForm(){
		super();
	}
	
	public AdminForm(final Admin admin) {
		this.admin = admin;
		this.passwordCheck = "";
		this.conditions = false;
	}
	
	public Admin getAdmin() {
		return admin;
	}
	
	public void setAdmin(Admin admin) {
		this.admin = admin;
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
