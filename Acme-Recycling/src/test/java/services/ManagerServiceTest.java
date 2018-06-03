
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

import security.UserAccount;
import utilities.AbstractTest;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// Supporting services ---------------------------------------------------
	@Autowired
	private ManagerService	managerService;

	@PersistenceContext
	EntityManager			entityManager;


	//Login 
	@Test
	public void driveLoginBuyer() {

		final Object testingData[][] = {
			//Manager is registered
			{
				"manager1", null
			},
			//Manager isn't registered
			{
				"managerNoRegistrado", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateLoginManager((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	public void templateLoginManager(final String username, final Class<?> expected) {

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

	//Test caso de uso 8.a: Crear cuentas para nuevos manager
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar manager correctamente(poniendo teléfono!=null)
				"managertest1", "passwordtest1", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", null
			}, {
				//Registrar manager incorrectamente con name en blanco
				"managertest2", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar manager con surname en blanco
				"managertest3", "passwordtest3", "miguel", "", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar manager incorrectamente con un email no válido
				"managertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar manager incorrectamente ya que está registrado
				"managertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@gmail.com", "SEVILLA", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar manager correctamente con número de teléfono vacío
				"managertest9", "passwordtest9", "name1", "surname1", "calle Huertas nº 3", "662657322", "maria@gmail.com", "SEVILLA", null

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Manager manager;
		UserAccount userAccount;

		caught = null;
		try {
			manager = this.managerService.create();
			manager.setName(name);
			manager.setSurname(surname);
			manager.setAddress(postalAdress);
			manager.setPhone(phone);
			manager.setEmail(email);
			manager.setProvince(province);
			userAccount = manager.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			manager.setUserAccount(userAccount);
			manager = this.managerService.save(manager);
			this.managerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso extra: Edit personal data of manager
	@Test
	public void driverEditmanager() {

		final Object testingData[][] = {
			{
				//Edito mi nombre
				"manager1", "manager1 name edited", "surname manager 1", "Adress manager 1", "+34644444444", "manager1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mis apellidos
				"manager1", "manager 1", "surname manager 1 edited", "Adress user 1", "+34644444444", "manager1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi dirección
				"manager1", "manager 1", "surname manager 1", "Adress manager 1 edited", "+34644444444", "manager1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi email
				"manager1", "manager 1", "surname manager 1", "Adress manager 1", "+34644444444", "carrieEDITEDr1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi nombre por uno en blanco
				"manager1", "", "surname manager 1", "Adress manager 1", "+34644444444", "manager1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mis apellidos por uno en blanco
				"manager1", "manager 1", "", "Adress manager 1", "+34644444444", "manager1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno que no cumple el formato
				"manager1", "manager 1", "surname manager 1", "Adress manager 1", "+34644444444", "manager1", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno en blanco
				"manager1", "", "surname manager 1", "Adress manager 1", "+34644444444", "", "SEVILLA", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditmanager(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateEditmanager(final int userNameId, final String name, final String surname, final String Adress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Manager manager;
		manager = this.managerService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(manager.getUserAccount().getUsername());
			manager.setName(name);
			manager.setSurname(surname);
			manager.setAddress(Adress);
			manager.setPhone(phone);
			manager.setEmail(email);
			manager.setProvince(province);
			manager = this.managerService.save(manager);
			this.unauthenticate();
			this.managerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}

}
