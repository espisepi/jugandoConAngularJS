
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "keyWord,lowPrice,highPrice")
})
public class Finder extends DomainEntity {

	// Attributes -------------------------------------------------------------

	private String	keyWord;
	private Double	lowPrice;
	private Double	highPrice;
	private Date	startCacheTime;


	public String getKeyWord() {
		return this.keyWord;
	}

	public void setKeyWord(final String keyWord) {
		this.keyWord = keyWord;
	}

	@Min(0)
	public Double getLowPrice() {
		return this.lowPrice;
	}

	public void setLowPrice(final Double lowPrice) {
		this.lowPrice = lowPrice;
	}

	@Min(0)
	public Double getHighPrice() {
		return this.highPrice;
	}

	public void setHighPrice(final Double highPrice) {
		this.highPrice = highPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	public Date getStartCacheTime() {
		return this.startCacheTime;
	}

	public void setStartCacheTime(final Date startCacheTime) {
		this.startCacheTime = startCacheTime;
	}


	// Relationships ---------------------------------------------------------------

	private Collection<Material>	materials;


	//private Buyer					buyer;

	@ManyToMany
	@NotNull
	@Valid
	public Collection<Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(final Collection<Material> materials) {
		this.materials = materials;
	}

	//	@Valid
	//	@NotNull
	//	@OneToOne(mappedBy = "finder")
	//	public Buyer getBuyer() {
	//		return this.buyer;
	//	}
	//
	//	public void setBuyer(final Buyer buyer) {
	//		this.buyer = buyer;
	//	}

}
