
package forms;

import javax.validation.Valid;

import domain.Admin;

public class AdminForm {

	@Valid
	private Admin	admin;
	private String	passwordCheck;
	private Boolean	conditions;


	public AdminForm() {
		super();
	}

	public AdminForm(final Admin admin) {
		this.admin = admin;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Admin getAdmin() {
		return this.admin;
	}

	public void setAdmin(final Admin admin) {
		this.admin = admin;
	}

	public String getPasswordCheck() {
		return this.passwordCheck;
	}

	public void setPasswordCheck(final String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	public Boolean getConditions() {
		return this.conditions;
	}

	public void setConditions(final Boolean conditions) {
		this.conditions = conditions;
	}
}
