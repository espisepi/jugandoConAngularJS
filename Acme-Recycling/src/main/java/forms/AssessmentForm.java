
package forms;

import javax.validation.Valid;

import domain.Assesment;

public class AssessmentForm {

	private Assesment	assessment;
	private int			requestId;


	public AssessmentForm() {
		super();
	}

	@Valid
	public Assesment getAssessment() {
		return this.assessment;
	}

	public void setAssessment(final Assesment assessment) {
		this.assessment = assessment;
	}

	public int getRequestId() {
		return this.requestId;
	}

	public void setRequestId(final int requestId) {
		this.requestId = requestId;
	}

}
