
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MaterialRepository;
import domain.Actor;
import domain.Admin;
import domain.Buy;
import domain.Buyer;
import domain.Material;

@Service
@Transactional
public class MaterialService {

	@Autowired
	MaterialRepository	materialRepository;

	@Autowired
	AdminService		adminService;

	@Autowired
	BuyService			buyService;

	@Autowired
	FinderService		finderService;

	@Autowired
	CourseService		courseService;

	@Autowired
	ActorService		actorService;

	@Autowired
	private Validator	validator;


	public MaterialService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Material create() {
		final Material result;
		Collection<Buy> buys;
		buys = new ArrayList<Buy>();
		result = new Material();
		result.setTotalPrice(0.0);
		result.setBuys(buys);
		return result;
	}

	public Collection<Material> findAll() {
		Collection<Material> res;
		res = this.materialRepository.findAll();
		return res;
	}

	public Material findOne(final int intBuy) {
		Material res;
		res = this.materialRepository.findOne(intBuy);
		return res;
	}

	public Material save(final Material material) {
		Material result;
		Assert.notNull(material);
		Actor actor;

		actor = this.actorService.findPrincipal();

		Assert.isTrue(actor instanceof Admin || actor instanceof Buyer);

		material.setTotalPrice(material.getQuantity() * material.getUnitPrice());

		result = this.materialRepository.save(material);

		return result;
	}

	//	public void delete(final Material material) {
	//		this.adminService.checkPrincipal();
	//		Assert.notNull(material);
	//		Assert.isTrue(material.getId() != 0);
	//		Collection<Buy> buysOfMaterial;
	//		Collection<Finder> findersOfMaterial;
	//		Collection<Course> coursesOfMaterial;
	//		buysOfMaterial = this.buyService.findBuysOfMaterial(material.getId());
	//		findersOfMaterial = this.finderService.findFindersOfMaterial(material.getId());
	//		coursesOfMaterial = this.courseService.findCoursesOfMaterial(material.getId());
	//
	//		for (final Buy b : buysOfMaterial)
	//			this.buyService.delete(b);
	//		for (final Finder f : findersOfMaterial)
	//			this.finderService.delete(f);
	//		for (final Course c : coursesOfMaterial)
	//			this.courseService.delete(c);
	//		this.materialRepository.delete(material);
	//	}

	public void flush() {
		this.materialRepository.flush();
	}

	//	RECONSTRUCTOR
	public Material reconstruct(final Material material, final BindingResult bindingResult) {
		Material result;
		Material materialBd;

		if (material.getId() == 0) {
			Collection<Buy> buys;

			result = material;

			buys = new ArrayList<Buy>();

			if (material.getBuys() == null || material.getBuys().size() == 0)
				material.setBuys(buys);

		}

		else {
			materialBd = this.materialRepository.findOne(material.getId());
			material.setId(materialBd.getId());
			material.setVersion(materialBd.getVersion());

			result = material;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Material> allMaterialWithoutQuantity() {
		Collection<Material> result;

		result = this.materialRepository.allMaterialWithoutQuantity();

		return result;
	}
}
