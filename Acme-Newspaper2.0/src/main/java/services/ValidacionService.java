
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ValidacionRepository;
import domain.Validacion;

@Service
@Transactional
public class ValidacionService {

	// Managed repository -----------------------------------------------------

	@Autowired
	ValidacionRepository	validacionRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public ValidacionService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	//CREATE---------------------------------------
	public Validacion create() {
		Validacion result;
		result = new Validacion();
		return result;
	}

	//SAVE-------------------------------------
	public Validacion save(final Validacion validacion) {
		final Validacion result;

		result = this.validacionRepository.save(validacion);

		return result;
	}

	//DELETE--------------------------------------
	public void delete(final Validacion validacion) {

		Assert.isTrue(validacion.getId() != 0);
		this.validacionRepository.delete(validacion);
	}

	//other Repository default----------------------
	public Validacion findOne(final int id) {
		Validacion result;
		result = this.validacionRepository.findOne(id);
		return result;
	}

	public Collection<Validacion> findAll() {
		Collection<Validacion> result;
		result = this.validacionRepository.findAll();
		return result;
	}

}
