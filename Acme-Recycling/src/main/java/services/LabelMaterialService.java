
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LabelMaterialRepository;
import domain.LabelMaterial;
import domain.Manager;
import domain.Material;

@Service
@Transactional
public class LabelMaterialService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private LabelMaterialRepository	labelMaterialRepository;

	//Suporting services

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------

	public LabelMaterialService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public LabelMaterial findOne(final int labelMaterialId) {
		Assert.isTrue(labelMaterialId != 0);

		LabelMaterial result;

		result = this.labelMaterialRepository.findOne(labelMaterialId);
		Assert.notNull(result);

		return result;

	}

	public Collection<LabelMaterial> findAll() {

		Collection<LabelMaterial> result;

		result = this.labelMaterialRepository.findAll();

		return result;

	}

	public LabelMaterial create() {
		LabelMaterial result;
		Collection<Material> materials;
		materials = new ArrayList<Material>();

		result = new LabelMaterial();
		result.setByDefault(false);
		result.setMaterials(materials);

		return result;
	}

	public LabelMaterial save(final LabelMaterial labelMaterial) {
		final Collection<LabelMaterial> labelMaterialsWithMaterial;
		labelMaterialsWithMaterial = this.labelMaterialRepository.labelMaterialsOfAllMaterials();
		LabelMaterial labelMaterialBD;

		Assert.notNull(labelMaterial);
		Assert.notNull(this.managerService.findByPrincipal());
		LabelMaterial result;
		if (labelMaterial.getId() != 0) {
			labelMaterialBD = this.labelMaterialRepository.findOne(labelMaterial.getId());
			Assert.isTrue(labelMaterialBD.getByDefault() == false);
			Assert.isTrue(!labelMaterialsWithMaterial.contains(labelMaterialBD), "This label is asociated with one material or more");
		}
		result = this.labelMaterialRepository.save(labelMaterial);

		return result;

	}

	public void delete(final LabelMaterial labelMaterial) {
		final Manager manager;
		final Collection<LabelMaterial> labelMaterialsWithMaterial;

		labelMaterialsWithMaterial = this.labelMaterialRepository.labelMaterialsOfAllMaterials();

		manager = this.managerService.findByPrincipal();
		Assert.notNull(manager);
		Assert.notNull(labelMaterial);

		Assert.isTrue(!labelMaterialsWithMaterial.contains(labelMaterial), "This label is asociated with one material or more");

		Assert.isTrue(labelMaterial.getId() != 0);
		Assert.isTrue(labelMaterial.getByDefault() == false);

		this.labelMaterialRepository.delete(labelMaterial);
	}

	public Collection<LabelMaterial> labelMaterialsOfAllMaterials() {
		Collection<LabelMaterial> labelMaterialsOfAllMaterials;

		labelMaterialsOfAllMaterials = this.labelMaterialRepository.labelMaterialsOfAllMaterials();
		return labelMaterialsOfAllMaterials;
	}

	//	RECONSTRUCTOR

	public LabelMaterial reconstruct(final LabelMaterial labelMaterial, final BindingResult bindingResult) {
		LabelMaterial result;
		LabelMaterial labelMaterialBD;
		if (labelMaterial.getId() == 0)
			result = labelMaterial;
		else {
			labelMaterialBD = this.labelMaterialRepository.findOne(labelMaterial.getId());
			labelMaterial.setId(labelMaterialBD.getId());
			labelMaterial.setVersion(labelMaterialBD.getVersion());

			result = labelMaterial;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}
	public void flush() {
		this.labelMaterialRepository.flush();
	}
}
