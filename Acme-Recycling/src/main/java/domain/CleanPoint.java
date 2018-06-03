
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class CleanPoint extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	address;
	private String	province;
	private String	phone;
	private boolean	mobile;
	private GPS		location;


	@NotBlank
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	@NotBlank
	public String getProvince() {
		return this.province;
	}

	public void setProvince(final String province) {
		this.province = province;
	}

	@Pattern(regexp = "(^(\\+\\d{1,3})?\\s?(\\(\\d{3}\\))?\\s?\\d{4,9}$)|(^$)")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public boolean isMobile() {
		return this.mobile;
	}

	public void setMobile(final boolean mobile) {
		this.mobile = mobile;
	}
	@Valid
	public GPS getLocation() {
		return this.location;
	}

	public void setLocation(final GPS location) {
		this.location = location;
	}

	// Relationships ---------------------------------------------------------------

}
