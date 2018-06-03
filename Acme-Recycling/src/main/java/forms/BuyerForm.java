
package forms;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import domain.Buyer;

public class BuyerForm {

	@Valid
	private Buyer	buyer;
	private String	passwordCheck;
	private Boolean	conditions;


	public BuyerForm() {
		super();
	}

	public BuyerForm(final Buyer buyer) {
		this.buyer = buyer;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Buyer getBuyer() {
		return this.buyer;
	}

	public void setBuyer(final Buyer buyer) {
		this.buyer = buyer;
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
