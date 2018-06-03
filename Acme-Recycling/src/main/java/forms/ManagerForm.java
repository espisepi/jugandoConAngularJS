
package forms;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import domain.Manager;

public class ManagerForm {

	@Valid
	private Manager	manager;
	private String	passwordCheck;
	private Boolean	conditions;


	public ManagerForm() {
		super();
	}

	public ManagerForm(final Manager manager) {
		this.manager = manager;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	@Size(min = 5, max = 32)
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
