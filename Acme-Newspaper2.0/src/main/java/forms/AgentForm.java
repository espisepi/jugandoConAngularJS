
package forms;

import javax.validation.Valid;

import domain.Agent;

public class AgentForm {

	@Valid
	private Agent	agent;
	private String	passwordCheck;
	private Boolean	conditions;


	public AgentForm() {
		super();
	}

	public AgentForm(final Agent agent) {
		this.agent = agent;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(final Agent agent) {
		this.agent = agent;
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
