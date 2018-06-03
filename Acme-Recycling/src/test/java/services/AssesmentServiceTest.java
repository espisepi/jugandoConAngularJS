
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Assesment;
import domain.Carrier;
import forms.AssessmentForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AssesmentServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	AssesmentService	assesmentService;

	@PersistenceContext
	EntityManager		entityManager;

	@Autowired
	ItemService			itemService;

	@Autowired
	CarrierService		carrierService;


	//Caso de uso 7.a) El transportista creará una evaluación cada vez que recoja un producto(Item). Solo podrá crear una evaluación cuando el estado esté "FINALIZADO" o "CANCELADO". Una vez el estado esté en "FINALIZADO" o "CANCELADO" no se puede volver a cambiar.
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea un assessment correctamente
				"carrier5", "request6", "description test", 2, null
			}, {
				//Se edita un assessment incorrectamente porque ya tiene un assessment esa request
				"carrier3", "request3", "description test", 5, IllegalArgumentException.class
			}, {
				//Se edita un assessment incorrectamente porque ya la request no esta con status finished ni cancelled
				"carrier1", "request1", "description test", 5, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Integer) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	private void templateCreateAndSave(final String username, final int requestId, final String description, final Integer valuation, final Class<?> expected) {
		Class<?> caught;
		AssessmentForm assesmentForm;

		caught = null;
		try {
			super.authenticate(username);
			assesmentForm = this.assesmentService.create(requestId);
			assesmentForm.getAssessment().setDescription(description);
			assesmentForm.getAssessment().setValuation(valuation);
			this.assesmentService.save(assesmentForm);

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 7.d) Listar y editar las evaluaciones que ha creado para una solicitud que le pertenece. Las evaluaciones no se pueden borrar del sistema, solamente modificarlas. (I)
	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//Se edita un assessment correctamente
				"carrier3", "assesment2", "request2", "description test", 2, null
			}, {
				//Se edita un assessment incorrectamente porque el atributo valuation esta fuera del rango
				"carrier3", "assesment2", "request2", "description test", 6, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (String) testingData[i][3], (int) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void templateEdit(final String username, final int assesmentId, final int requestId, final String description, final int valuation, final Class<?> expected) {
		Class<?> caught;
		Assesment assessment;
		AssessmentForm assesmentForm;

		caught = null;
		try {
			super.authenticate(username);
			assessment = this.assesmentService.findOne(assesmentId);
			assesmentForm = new AssessmentForm();
			assesmentForm.setAssessment(assessment);
			assesmentForm.setRequestId(requestId);
			assesmentForm.getAssessment().setDescription(description);
			assesmentForm.getAssessment().setValuation(valuation);

			this.assesmentService.save(assesmentForm);

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 7.d) Listar y editar las evaluaciones que ha creado para una solicitud que le pertenece. Las evaluaciones no se pueden borrar del sistema, solamente modificarlas. (II)
	@Test
	public void driverList() {
		final Object testingData[][] = {
			{
				//Se listan las assessments correctamente porque la assessment2 pertenece al carrier3
				"carrier3", "assesment2", null
			}, {
				//Se listan las assessments incorrectamente porque la assessment2 no pertenece al carrier2
				"carrier2", "request2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateList((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateList(final String username, final int assesmentId, final Class<?> expected) {
		Class<?> caught;
		final Assesment assesment;
		Collection<Assesment> assesments;
		Carrier carrierConnected;

		caught = null;
		try {
			super.authenticate(username);
			carrierConnected = this.carrierService.findByPrincipal();
			assesments = this.assesmentService.findByCarrierId(carrierConnected.getId());
			assesment = this.assesmentService.findOne(assesmentId);
			Assert.isTrue(assesments.contains(assesment));

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
