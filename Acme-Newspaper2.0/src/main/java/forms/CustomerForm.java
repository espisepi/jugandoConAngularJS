
package forms;

import javax.validation.Valid;

import domain.Customer;

public class CustomerForm {

	@Valid
	private Customer	customer;
	private String		passwordCheck;
	private Boolean		conditions;


	public CustomerForm() {
		super();
	}

	public CustomerForm(final Customer customer) {
		this.customer = customer;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
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
