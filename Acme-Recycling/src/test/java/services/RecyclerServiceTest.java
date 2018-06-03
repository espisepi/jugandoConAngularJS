
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Recycler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RecyclerServiceTest extends AbstractTest {

	// Supporting services ---------------------------------------------------
	@Autowired
	private RecyclerService	recyclerService;

	@PersistenceContext
	EntityManager			entityManager;


	//Login 
	@Test
	public void driveLoginRecycler() {

		final Object testingData[][] = {
			//Admin is registered
			{
				"recycler1", null
			},
			//Admin isn't registered
			{
				"recyclerNoRegistrado", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateLoginRecycler((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	public void templateLoginRecycler(final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	//Test caso de uso 1.a:Register to the system as a recycler
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar recycler correctamente(poniendo teléfono!=null)
				"recyclertest1", "passwordtest1", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", null
			}, {
				//Registrar recycler incorrectamente con name en blanco
				"recyclertest2", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar recycler con surname en blanco
				"recyclertest3", "passwordtest3", "miguel", "", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar recycler incorrectamente con un email no válido
				"recyclertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar recycler incorrectamente ya que está registrado
				"recyclertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@gmail.com", "SEVILLA", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar recycler correctamente con número de teléfono vacío
				"recyclertest9", "passwordtest9", "name1", "surname1", "calle Huertas nº 3", "662657322", "maria@gmail.com", "SEVILLA", null

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Recycler recycler;
		UserAccount userAccount;

		caught = null;
		try {
			recycler = this.recyclerService.create();
			recycler.setName(name);
			recycler.setSurname(surname);
			recycler.setAddress(postalAdress);
			recycler.setPhone(phone);
			recycler.setEmail(email);
			recycler.setProvince(province);
			userAccount = recycler.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			recycler.setUserAccount(userAccount);
			recycler = this.recyclerService.save(recycler);
			this.recyclerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso extra: Edit personal data of recycler
	@Test
	public void driverEditrecycler() {

		final Object testingData[][] = {
			{
				//Edito mi nombre
				"recycler1", "recycler1 name edited", "surname recycler 1", "Adress recycler 1", "+34655555555", "recycler1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mis apellidos
				"recycler1", "recycler 1", "surname recycler 1 edited", "Adress user 1", "+34655555555", "recycler1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi dirección
				"recycler1", "recycler 1", "surname recycler 1", "Adress recycler 1 edited", "+34655555555", "recycler1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi email
				"recycler1", "recycler 1", "surname recycler 1", "Adress recycler 1", "+34655555555", "carrieEDITEDr1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi nombre por uno en blanco
				"recycler1", "", "surname recycler 1", "Adress recycler 1", "+34655555555", "recycler1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mis apellidos por uno en blanco
				"recycler1", "recycler 1", "", "Adress recycler 1", "+34655555555", "recycler1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno que no cumple el formato
				"recycler1", "recycler 1", "surname recycler 1", "Adress recycler 1", "+34655555555", "recycler1", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno en blanco
				"recycler1", "", "surname recycler 1", "Adress recycler 1", "+34655555555", "", "SEVILLA", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditrecycler(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateEditrecycler(final int userNameId, final String name, final String surname, final String Adress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Recycler recycler;
		recycler = this.recyclerService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(recycler.getUserAccount().getUsername());
			recycler.setName(name);
			recycler.setSurname(surname);
			recycler.setAddress(Adress);
			recycler.setPhone(phone);
			recycler.setEmail(email);
			recycler.setProvince(province);
			recycler = this.recyclerService.save(recycler);
			this.unauthenticate();
			this.recyclerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}

	//Test caso de uso 3.b:El reciclador tendrá un perfil personal en el que podrá ver un resumen de todo lo que ha reciclado y sus respectivos puntos.
	@Test
	public void driverScoreRecycler() {

		final Object testingData[][] = {
			{
				//Recycler con puntuación distinta de 0 al tener algun item
				"recycler1", 32.0, null
			}, {
				//Recycler con puntuación 0 al no tener ningún item
				"recycler4", 0.0, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateScoreRecycler(super.getEntityId((String) testingData[i][0]), (Double) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateScoreRecycler(final int recyclerId, final Double score, final Class<?> expected) {
		Class<?> caught;
		Recycler recycler;
		Double scoreOfRecycler;
		recycler = this.recyclerService.findOne(recyclerId);

		caught = null;
		try {
			super.authenticate(recycler.getUserAccount().getUsername());
			scoreOfRecycler = this.recyclerService.puntuationOfRecycler();
			if (recycler.getUserAccount().getUsername().equals("recycler1"))
				Assert.isTrue(scoreOfRecycler.equals(score));
			else
				Assert.isTrue(scoreOfRecycler.equals(score));

			this.unauthenticate();
			this.recyclerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}

}
