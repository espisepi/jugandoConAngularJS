
package forms;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Opinion;

public class OpinionForm {

	private Opinion	opinion;
	private Integer	opinableId;
	private boolean	opinableItem;


	public OpinionForm() {

	}

	@Valid
	public Opinion getOpinion() {
		return this.opinion;
	}

	public void setOpinion(final Opinion opinion) {
		this.opinion = opinion;
	}

	@NotNull
	public Integer getOpinableId() {
		return this.opinableId;
	}

	public void setOpinableId(final Integer opinableId) {
		this.opinableId = opinableId;
	}

	public boolean isOpinableItem() {
		return this.opinableItem;
	}

	public void setOpinableItem(final boolean opinableItem) {
		this.opinableItem = opinableItem;
	}

}
