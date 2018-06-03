package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Incidence;
import domain.Recycler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
	
})
@Transactional
public class IncidenceServiceTest extends AbstractTest{
	
	//Supporting services ----------------------------------------------------
	@Autowired
	private IncidenceService incidenceService;
	
	@Autowired
	private RecyclerService recyclerService;
	
	@PersistenceContext
	EntityManager		entityManager;
	
	
	// 3.f Los recicladores que hayan realizado operaciones con la empresa podrán crear, editar, borrar y listar las incidencia
	@Test
	public void driverList() {
		final Object testingData[][] = {
			
			//La incidencia1 está contenida en las incidencias del reciclador, caso positivo
			{

				"recycler1", "incidence1", null

			},
			//La incidencia1 no contenida en las incidencias del reciclador, caso negativo
			{

				"recycler1", "incidence3", IllegalArgumentException.class

			}
			
			
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateList((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	
	
	private void templateList(String username, String incidenceId, Class<?> expected) {
		Class<?> caught;
		Incidence incidence;
		Collection<Incidence> incidences;
		Recycler recycler;
		caught = null;

		try {
			super.authenticate(username);
			recycler = this.recyclerService.findOne(super.getEntityId(username));
			incidence = this.incidenceService.findOne(super.getEntityId(incidenceId));
			incidences = this.incidenceService.findIncidencesByRecycler(recycler.getId());
			Assert.isTrue(incidences.contains(incidence));
			this.incidenceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	// 3.f Los recicladores que hayan realizado operaciones con la empresa podrán crear, editar, borrar y listar las incidencias  
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			// Un reciclador crea una incidencia, caso positivo
			{

				"recycler1", "incidence prueba", "reason why prueba", "comment prueba", null

			}, 
			//Un reciclador crea una incidencia con el título en blanco, caso negativo 
			{

				"recycler1", "", "reason why prueba", "comment prueba", javax.validation.ConstraintViolationException.class

			},
			//Un reciclador crea una incidencia con la razón en blanco, caso negativo
			{

				"recycler1", "incidence prueba", "", "comment prueba", javax.validation.ConstraintViolationException.class

			},
			//Un reciclador crea una incidencia con la razón nula, caso negativo 
			{

				"recycler1", "incidence prueba", null, "comment prueba", javax.validation.ConstraintViolationException.class

			},
			// Un reciclador crea una incidencia con el comentario en blanco, caso positivo
			{

				"recycler1", "incidence prueba", "reason why prueba", "", null

			},
			// Un comprador intenta crear una incidencia, caso negativo
			{

				"buyer1", "incidence prueba", "reason why prueba", "", java.lang.IllegalArgumentException.class

			},
			//Un transportista intenta crear una incidencia, caso negativo
			{

				"carrier1", "incidence prueba", "reason why prueba", "", java.lang.IllegalArgumentException.class

			},
			//Un editor intenta crear una incidencia, caso negativo
			{

				"editor1", "incidence prueba", "reason why prueba", "", java.lang.IllegalArgumentException.class

			},
			//Un administrador intenta crear una incidencia, caso negativo
			{

				"admin", "incidence prueba", "reason why prueba", "", java.lang.IllegalArgumentException.class

			}
			
			
			
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3],(Class<?>) testingData[i][4]);
	}
	
	
	private void templateCreateAndSave(String username, String title, String reasonWhy, String comment, Class<?> expected) {
		Class<?> caught;
		Incidence incidence;
		caught = null;

		try {
			super.authenticate(username);
			incidence = this.incidenceService.create();

			incidence.setTitle(title);
			incidence.setReasonWhy(reasonWhy);
			incidence.setComment(comment);
			incidence = this.incidenceService.save(incidence);
			this.incidenceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	
	// 3.f Los recicladores que hayan realizado operaciones con la empresa podrán crear, editar, borrar y listar las incidencias
	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			
			//Reciclador cambia el título, caso positivo
			{

				"recycler1", "incidence1", "title changed", null

			}, 
			//Reciclador cambia el títutlo a blanco, caso negativo
			{

				"recycler1", "incidence1", "", javax.validation.ConstraintViolationException.class

			},
			//Reciclador cambia el título a nulo, caso negativo
			{

				"recycler1", "incidence1", null, javax.validation.ConstraintViolationException.class

			},
			//Reciclador intenta editar una incidencia que no es suya, caso negativo
			{

				"recycler2", "incidence1", "title changed", java.lang.IllegalArgumentException.class

			}
			
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	
	
	private void templateEdit(String username, String incidenceId, String title,  Class<?> expected) {
		Class<?> caught;
		Incidence incidence;
		caught = null;

		try {
			super.authenticate(username);
			incidence = this.incidenceService.findOne(super.getEntityId(incidenceId));

			incidence.setTitle(title);
			incidence = this.incidenceService.save(incidence);
			this.incidenceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	// 3.f Los recicladores que hayan realizado operaciones con la empresa podrán crear, editar, borrar y listar las incidencias
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			
			//Reciclador borra su incidencia, caso positivo
			{

				"recycler1", "incidence1", null

			}, 
			//Reciclador intenta borrar una indicencia que está finalizada, caso negativo
			{

				"recycler2", "incidence2", java.lang.IllegalArgumentException.class

			},
			//Reciclador intenta borrar una incidencia que no es suya, caso negativo
			{

				"recycler2", "incidence1", java.lang.IllegalArgumentException.class

			}
			
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	
	
	private void templateDelete(String username, String incidenceId, Class<?> expected) {
		Class<?> caught;
		Incidence incidence;
		caught = null;

		try {
			super.authenticate(username);
			incidence = this.incidenceService.findOne(super.getEntityId(incidenceId));

			this.incidenceService.delete(incidence);
			this.incidenceService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	// 4.d El manager resolverá las incidencias y una vez que estén resueltas, la incidencia pasará a estar en modo resuelta.
		@Test
		public void driverResolved() {
			final Object testingData[][] = {
				
				//La incidencia1 está contenida en las incidencias para resolver
				{

					"Manager1", "incidence1", null

				},
				//La incidencia1 está contenida en las incidencias para resolver
				{

					"Manager1", "incidence2", IllegalArgumentException.class

				}
				
				
			};
			for (int i = 0; i < testingData.length; i++)
				this.templateResolved((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
		}
		
		
		private void templateResolved(String username, String incidenceId, Class<?> expected) {
			Class<?> caught;
			Incidence incidence;
			Collection<Incidence> incidences;
			caught = null;

			try {
				super.authenticate(username);
				incidence = this.incidenceService.findOne(super.getEntityId(incidenceId));
				incidences = this.incidenceService.findIncidenceNoResolved();
				Assert.isTrue(incidences.contains(incidence));
				this.incidenceService.flush();

			} catch (final Throwable oops) {
				caught = oops.getClass();
				//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

			super.unauthenticate();
		}
}
