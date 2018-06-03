
package forms;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import domain.Carrier;

public class CarrierForm {

	@Valid
	private Carrier	carrier;
	private String	passwordCheck;
	private Boolean	conditions;


	public CarrierForm() {
		super();
	}

	public CarrierForm(final Carrier carrier) {
		this.carrier = carrier;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Carrier getCarrier() {
		return this.carrier;
	}

	public void setCarrier(final Carrier carrier) {
		this.carrier = carrier;
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
