
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import domain.Buyer;
import domain.Finder;
import domain.Material;

@Service
@Transactional
public class FinderService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private FinderRepository			finderRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ConfigurationSystemService	configurationSystemService;
	@Autowired
	private MaterialService				materialService;

	@Autowired
	private BuyerService				buyerService;


	// Constructors -----------------------------------------------------------

	public FinderService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Finder create() {
		Finder result;
		Collection<Material> materials;

		result = new Finder();
		materials = new ArrayList<Material>();

		materials = this.materialService.findAll();
		result.setStartCacheTime(new Date());
		result.setKeyWord("");

		result.setMaterials(materials);

		return result;
	}
	public Collection<Finder> findAll() {
		Collection<Finder> result;

		Assert.notNull(this.finderRepository);
		result = this.finderRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Finder findOne(final int finderId) {
		Finder result;

		result = this.finderRepository.findOne(finderId);
		Assert.notNull(result);

		return result;
	}

	public Finder save(final Finder finder) {
		Finder newFinder;
		Assert.notNull(finder);

		finder.setStartCacheTime(new Date());
		newFinder = this.finderRepository.save(finder);
		Assert.notNull(newFinder);

		return newFinder;
	}

	public void delete(final Finder finder) {
		Buyer buyer;
		buyer = this.buyerService.findBuyerOfFinder(finder.getId());
		buyer.setFinder(null);

		this.finderRepository.delete(finder);
	}

	// Other business methods -------------------------------------------------

	public Finder search(final Finder finder) {
		final Collection<Material> materials;
		final Page<Material> pages;
		int maxNumberFinder;

		maxNumberFinder = this.configurationSystemService.findOne().getMaxNumberFinder();
		final Pageable pageable = new PageRequest(0, maxNumberFinder);

		if (finder.getKeyWord() == null)
			finder.setKeyWord("");
		pages = this.finderRepository.findByKeyWord(finder.getKeyWord(), pageable);
		materials = new ArrayList<Material>(pages.getContent());

		if (finder.getLowPrice() != null) {
			final Page<Material> pageLowPrice;
			pageLowPrice = this.finderRepository.findByLowPrice(finder.getLowPrice(), pageable);
			final Collection<Material> materialsLowPrice = pageLowPrice.getContent();
			materials.retainAll(materialsLowPrice);
		}

		if (finder.getHighPrice() != null) {
			Page<Material> materialsHighPrice;
			materialsHighPrice = this.finderRepository.findByHighPrice(finder.getHighPrice(), pageable);
			materials.retainAll(materialsHighPrice.getContent());
		}

		finder.setMaterials(materials);
		return finder;
	}
	public boolean checkEquals(final Finder finderCache, final Finder finder) {
		boolean result;

		result = false;
		if (this.compareKeyWord(finderCache.getKeyWord(), finder.getKeyWord()) && (this.compareLowPrice(finderCache.getLowPrice(), finder.getLowPrice())) && (this.compareHighPrice(finderCache.getHighPrice(), finder.getHighPrice())))
			result = true;

		return result;
	}

	private boolean compareHighPrice(final Double highPrice, final Double highPrice2) {
		boolean result;

		result = false;
		if (highPrice == null && highPrice2 == null)
			result = true;
		else if ((highPrice == null && highPrice2 != null) || (highPrice != null && highPrice2 == null))
			result = false;
		else if (highPrice.equals(highPrice2))
			result = true;
		return result;
	}

	private boolean compareLowPrice(final Double lowPrice, final Double lowPrice2) {
		boolean result;

		result = false;
		if (lowPrice == null && lowPrice2 == null)
			result = true;
		else if ((lowPrice == null && lowPrice2 != null) || (lowPrice != null && lowPrice2 == null))
			result = false;
		else if (lowPrice.equals(lowPrice2))
			result = true;
		return result;
	}

	private boolean compareKeyWord(final String keyWord, final String keyWord2) {
		boolean result;

		result = false;
		if (keyWord == null && keyWord2 == null)
			result = true;
		else if ((keyWord == null && keyWord2 != null) || (keyWord != null && keyWord2 == null))
			result = false;
		else if (keyWord.equals(keyWord2))
			result = true;
		return result;
	}

	public Collection<Finder> findFindersOfMaterial(final int materialId) {
		Collection<Finder> finders;

		finders = this.finderRepository.findFindersOfMaterial(materialId);
		return finders;
	}
}
