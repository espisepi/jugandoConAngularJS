
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AssesmentRepository;
import domain.Assesment;
import domain.Request;
import forms.AssessmentForm;

@Service
@Transactional
public class AssesmentService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private AssesmentRepository	assesmentRepository;

	@Autowired
	private RequestService		requestService;

	@Autowired
	private CarrierService		carrierService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public AssesmentService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public AssessmentForm create(final int requestId) {
		AssessmentForm result;

		//No copiar la siguiente linea en el reconstruct
		result = new AssessmentForm();
		result.setAssessment(new Assesment());
		result.setRequestId(requestId);

		Date moment;
		moment = new Date(System.currentTimeMillis() - 1000);
		result.getAssessment().setMoment(moment);

		return result;

	}
	public Assesment findOne(final int assesmentId) {
		Assesment result;

		result = this.assesmentRepository.findOne(assesmentId);

		return result;
	}

	public Collection<Assesment> findAll() {
		Collection<Assesment> result;

		result = this.assesmentRepository.findAll();

		return result;
	}

	public Assesment save(final AssessmentForm assesmentForm) {
		final Assesment result;
		Assesment assesment;
		Request request;

		assesment = assesmentForm.getAssessment();

		Assert.notNull(assesment);

		//No se puede crear assessment en un request que no te pertenece
		request = this.requestService.findOne(assesmentForm.getRequestId());
		if (assesment.getId() == 0) {
			//Solo se puede crear assessment en los request con status finalizado o cancelado
			Assert.isTrue(request.getStatus().equals("FINISHED") || request.getStatus().equals("CANCELLED"));
			//No se puede crear un assessment en un request que ya tiene assessment
			Assert.isTrue(request.getAssesment() == null);
		}
		final Collection<Request> requests = this.requestService.findByCarrierId(this.carrierService.findByPrincipal().getId());
		Assert.isTrue(requests.contains(request));

		if (assesment.getId() == 0) {
			Date moment;
			moment = new Date(System.currentTimeMillis() - 1000);
			assesment.setMoment(moment);
		} else
			Assert.isTrue(this.findByCarrierId(this.carrierService.findByPrincipal().getId()).contains(assesmentForm.getAssessment()));

		result = this.assesmentRepository.save(assesment);

		//Añadimos el assessment al request que le pertenece
		this.requestService.findOne(assesmentForm.getRequestId()).setAssesment(result);

		return result;
	}

	public void delete(final Assesment assesment) {
		Assert.notNull(assesment);

		this.assesmentRepository.delete(assesment);
	}

	public Collection<Assesment> findByCarrierId(final int id) {
		Collection<Assesment> result;
		result = this.assesmentRepository.findByCarrierId(id);
		return result;
	}

}
