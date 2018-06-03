
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CleanPointRepository;
import domain.CleanPoint;

@Service
@Transactional
public class CleanPointService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CleanPointRepository	cleanPointRepository;

	@Autowired
	private AdminService			adminService;

	// Supporting services ----------------------------------------------------

	//Importar la que pertenece a Spring
	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------
	public CleanPointService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public CleanPoint create() {
		CleanPoint result;
		this.adminService.checkPrincipal();

		result = new CleanPoint();

		return result;
	}

	public CleanPoint save(final CleanPoint cleanPoint) {
		CleanPoint result;
		Assert.notNull(cleanPoint);

		result = this.cleanPointRepository.save(cleanPoint);

		return result;
	}

	public Collection<CleanPoint> findAll() {
		Collection<CleanPoint> result;

		result = this.cleanPointRepository.findAll();
		return result;
	}

	public CleanPoint findOne(final int cleanPointId) {
		Assert.isTrue(cleanPointId != 0);

		CleanPoint result;

		result = this.cleanPointRepository.findOne(cleanPointId);
		return result;
	}

	public void delete(final CleanPoint cleanPoint) {
		Assert.notNull(cleanPoint);
		Assert.isTrue(cleanPoint.getId() != 0);
		Assert.isTrue(cleanPoint.isMobile() == true);
		this.cleanPointRepository.delete(cleanPoint);
	}

	//Other business methods---------------------------------------------------

	//	RECONSTRUCTOR

	public CleanPoint reconstruct(final CleanPoint cleanPoint, final BindingResult bindingResult) {
		CleanPoint result;
		final CleanPoint cleanPointBd;

		if (cleanPoint.getId() == 0)
			result = cleanPoint;
		else {
			cleanPointBd = this.cleanPointRepository.findOne(cleanPoint.getId());
			cleanPoint.setId(cleanPointBd.getId());
			cleanPoint.setVersion(cleanPointBd.getVersion());

			result = cleanPoint;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public void flush() {
		this.cleanPointRepository.flush();
	}
}
