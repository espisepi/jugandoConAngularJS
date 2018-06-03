
package forms;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import domain.Recycler;

public class RecyclerForm {

	@Valid
	private Recycler	recycler;
	private String		passwordCheck;
	private Boolean		conditions;


	public RecyclerForm() {
		super();
	}

	public RecyclerForm(final Recycler recycler) {
		this.recycler = recycler;
		this.passwordCheck = "";
		this.conditions = false;
	}

	public Recycler getRecycler() {
		return this.recycler;
	}

	public void setRecycler(final Recycler recycler) {
		this.recycler = recycler;
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
