
package forms;

import javax.validation.Valid;

import domain.Request;

public class RequestForm {

	private Request	request;
	private int		itemId;


	public RequestForm() {
		super();
	}

	@Valid
	public Request getRequest() {
		return this.request;
	}

	public void setRequest(final Request request) {
		this.request = request;
	}

	public int getItemId() {
		return this.itemId;
	}

	public void setItemId(final int itemId) {
		this.itemId = itemId;
	}

}
