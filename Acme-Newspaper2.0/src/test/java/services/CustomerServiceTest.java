
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CustomerServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	private CustomerService	customerService;

	@PersistenceContext
	EntityManager			entityManager;


	//Test caso de uso extra: Login customer
	@Test
	public void driverLoginCustomer() {

		final Object testingData[][] = {
			{
				//login customer registrado
				"customer1", null

			}, {
				//login customer no registrado
				"juanya", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	private void templateLogin(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			this.unauthenticate();
			this.customerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso 21.1:Register to the system as a customer
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar customer correctamente
				"customertest1", "passwordtest1", "miguel", "ternero", "calle Huertas nº 3", "662657322", "customer@email.com", null
			}, {
				//Registrar customer con name en blanco
				"customertest2", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar customer con surname en blanco
				"customertest3", "passwordtest3", "miguel", "", "calle Huertas nº 3", "662657322", "Email@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar customer con un email no válido
				"customertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar customer ya registrado
				"customertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@gmail.com", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar un customer con número de teléfono vacío
				"customertest9", "passwordtest9", "miguel", "ternero", "calle Huertas nº 3", "", "Email@gmail.com", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		Customer customer;
		UserAccount userAccount;

		caught = null;
		try {
			customer = this.customerService.create();
			customer.setName(name);
			customer.setSurname(surname);
			customer.setPostalAddress(postalAdress);
			customer.setPhone(phone);
			customer.setEmail(email);
			userAccount = customer.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			customer.setUserAccount(userAccount);
			customer = this.customerService.save(customer);
			this.customerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso extra: Edit personal data of customer
	@Test
	public void driverEditCustomer() {

		final Object testingData[][] = {
			{
				//Edito mi nombre
				"customer1", "customer1 name edited", "surname customer 1", "postal Adress customer 1", "617557203", "customer1@acmenewspaper.com", null
			}, {
				//Edito mis apellidos
				"customer1", "customer 1", "surname customer 1 edited", "postal Adress user 1", "617557203", "customer1@acmenewspaper.com", null
			}, {
				//Edito mi dirección
				"customer1", "customer 1", "surname customer 1", "postal Adress customer 1 edited", "617557203", "customer1@acmenewspaper.com", null
			}, {
				//Edito mi email
				"customer1", "customer 1", "surname customer 1", "postal Adress customer 1", "617557203", "customer1EDITED@acmenewspaper.com", null
			}, {
				//Edito mi nombre por uno en blanco
				"customer1", "", "surname customer 1", "postal Adress customer 1", "617557203", "customer1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito mis apellidos por uno en blanco
				"customer1", "customer 1", "", "postal Adress customer 1", "617557203", "customer1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito mi email por uno que no cumple el formato
				"customer1", "customer 1", "surname customer 1", "postal Adress customer 1", "617557203", "customer1", ConstraintViolationException.class
			}, {
				//Edito mi email por uno en blanco
				"customer1", "", "surname customer 1", "postal Adress customer 1", "617557203", "", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditCustomer(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateEditCustomer(final int userNameId, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		Customer customer;
		customer = this.customerService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(customer.getUserAccount().getUsername());
			customer.setName(name);
			customer.setSurname(surname);
			customer.setPostalAddress(postalAdress);
			customer.setPhone(phone);
			customer.setEmail(email);
			customer = this.customerService.save(customer);
			this.unauthenticate();
			this.customerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}
}
