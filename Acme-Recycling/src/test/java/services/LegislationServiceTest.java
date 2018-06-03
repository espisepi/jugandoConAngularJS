
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Admin;
import domain.ConfigurationSystem;
import domain.Legislation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LegislationServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	LegislationService			legislationService;

	@Autowired
	AdminService				adminService;

	@Autowired
	ConfigurationSystemService	configurationSystemService;

	@PersistenceContext
	EntityManager				entityManager;


	//Create a law
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea una ley correctamente
				"admin", "hola", "", "title1", null

			}, {
				//Se crea una ley incorrectamente con el body en blanco
				"admin", "", "", "title1", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}
	private void templateCreateAndSave(final String username, final String body, final String link, final String title, final Class<?> expected) {
		Class<?> caught;
		Legislation legislation;
		caught = null;

		try {
			super.authenticate(username);
			legislation = this.legislationService.create();

			legislation.setBody(body);
			legislation.setLink(link);
			legislation.setTitle(title);

			legislation = this.legislationService.save(legislation);
			this.legislationService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Listar todas las leyes del sistema
	@Test
	public void driverListLaws() {
		final Object testingData[][] = {
			{
				//El admin lista las 5 palabras tabú que hay en el sistema
				"admin", 4, null
			}, {
				//No hay un total de 3 si no de 5 
				"admin", 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListLaws(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateListLaws(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		Admin adminConnected;
		adminConnected = this.adminService.findOne(usernameId);
		Collection<Legislation> laws;

		caught = null;
		try {
			super.authenticate(adminConnected.getUserAccount().getUsername());
			laws = this.legislationService.findAll();
			Assert.isTrue(laws.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Edit a law
	@Test
	public void driveEditLaw() {

		final Object testingData[][] = {
			//Editar una ley correctamente
			{
				"law1", "admin", "body1", "", "title1", null
			}, {
				//Editar una ley incorrectamente dejando el body vacío
				"law2", "admin", "", "", "title1", javax.validation.ConstraintViolationException.class
			}, {
				//Editar una ley incorrectamebte poniendo una url incorrecta
				"law2", "admin", "body1", "incorrecta", "title1", javax.validation.ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditLaw((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	public void templateEditLaw(final String entity, final String username, final String body, final String link, final String title, final Class<?> expected) {

		Class<?> caught;
		Legislation law;

		caught = null;

		try {
			super.authenticate(username);
			law = this.legislationService.findOne(super.getEntityId(entity));
			law.setBody(body);
			law.setLink(link);
			law.setTitle(title);

			this.legislationService.save(law);
			this.legislationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}

	//Remove a law
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina una law correctamente
				"admin", "law1", null

			}, {
				//Se elimina una law incorrectamente por un recycler
				"recycler1", "law2", java.lang.IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int tabooWordId, final Class<?> expected) {
		Legislation legislation;
		final ConfigurationSystem configurationSystem;
		configurationSystem = this.configurationSystemService.findConfigurationSystem();
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			legislation = this.legislationService.findOne(tabooWordId);
			configurationSystem.getLaws().remove(legislation);
			this.configurationSystemService.save(configurationSystem);
			this.legislationService.delete(legislation);

			this.legislationService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
