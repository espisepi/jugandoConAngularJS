
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LabelProductRepository;
import domain.Item;
import domain.LabelProduct;
import domain.Manager;

@Service
@Transactional
public class LabelProductService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LabelProductRepository	labelProductRepository;

	//Suporting services-------------------------------------------------------

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------

	public LabelProductService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public LabelProduct findOne(final int labelProductId) {
		Assert.isTrue(labelProductId != 0);

		LabelProduct result;

		result = this.labelProductRepository.findOne(labelProductId);
		Assert.notNull(result);

		return result;

	}

	public Collection<LabelProduct> findAll() {

		Collection<LabelProduct> result;

		result = this.labelProductRepository.findAll();

		return result;

	}

	public LabelProduct create() {
		LabelProduct result;
		Collection<Item> items;
		items = new ArrayList<Item>();

		result = new LabelProduct();
		result.setItems(items);
		result.setByDefault(false);

		return result;
	}

	public LabelProduct save(final LabelProduct labelProduct) {
		final Collection<LabelProduct> labelProductsWithProduct;
		labelProductsWithProduct = this.labelProductRepository.labelProductsOfAllProducts();
		LabelProduct labelProductBd;

		Assert.notNull(labelProduct);
		Assert.notNull(this.managerService.findByPrincipal());
		LabelProduct result;
		if (labelProduct.getId() != 0) {
			labelProductBd = this.labelProductRepository.findOne(labelProduct.getId());
			Assert.isTrue(labelProductBd.getByDefault() == false);
			Assert.isTrue(!labelProductsWithProduct.contains(labelProductBd), "This label is asociated with one product or more");
		}
		result = this.labelProductRepository.save(labelProduct);

		return result;

	}

	public void delete(final LabelProduct labelProduct) {
		final Manager manager;
		final Collection<LabelProduct> labelProductsWithProduct;

		labelProductsWithProduct = this.labelProductsOfAllProducts();

		manager = this.managerService.findByPrincipal();
		Assert.notNull(manager);
		Assert.notNull(labelProduct);

		Assert.isTrue(!labelProductsWithProduct.contains(labelProduct), "This label is asociated with one product or more");

		Assert.isTrue(labelProduct.getId() != 0);
		Assert.isTrue(labelProduct.getByDefault() == false);

		this.labelProductRepository.delete(labelProduct);
	}

	//	RECONSTRUCTOR

	public LabelProduct reconstruct(final LabelProduct labelProduct, final BindingResult bindingResult) {
		LabelProduct result;
		LabelProduct labelProductBD;
		if (labelProduct.getId() == 0)
			result = labelProduct;
		else {
			labelProductBD = this.labelProductRepository.findOne(labelProduct.getId());
			labelProduct.setId(labelProductBD.getId());
			labelProduct.setVersion(labelProductBD.getVersion());
			labelProduct.setItems(labelProductBD.getItems());

			result = labelProduct;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public void flush() {
		this.labelProductRepository.flush();
	}

	public Collection<LabelProduct> labelProductsOfAllProducts() {
		Collection<LabelProduct> labelProductsOfAllProducts;

		labelProductsOfAllProducts = this.labelProductRepository.labelProductsOfAllProducts();

		return labelProductsOfAllProducts;
	}
}
