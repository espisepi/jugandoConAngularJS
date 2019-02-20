
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

import security.UserAccount;
import utilities.AbstractTest;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AgentServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	private AgentService			agentService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private AdvertisementService	advertisementService;

	@PersistenceContext
	EntityManager					entityManager;


	//Loguearte en el sistema como agente
	@Test
	public void driverLoginAgent() {

		final Object testingData[][] = {
			{
				//Loguearte como agent1 el cual ya está registrado registrado
				"agent1", null

			}, {
				//Loguearte incorrectamente como juanya el cual no está registrado en el sistema
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
			this.agentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}
		this.checkExceptions(expected, caught);
	}

	//Caso de uso 3.1) Registrarse en el sistema como agente
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar agente en el sistema correctamente
				"maria1", "passwordtest1", "maria", "ruiz", "calle Huertas nº 3", "662657322", "maria@email.com", null
			}, {
				//Registrar incorrectamente agente con el atributo nombre en blanco
				"miguel1", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "miguel@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar incorrectamente agente con el atributo surname en blanco
				"laura1", "passwordtest3", "laura", "", "calle Huertas nº 3", "662657322", "Email@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar incorrectamente agente con un email no válido
				"daniel1", "passwordtest4", "daniel", "lozano", "calle Huertas nº 3", "662657322", "Email", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar agente ya registrado previamente en el sistema
				"maria1", "passwordtest1", "maria", "ruiz", "calle Huertas nº 3", "662657322", "maria@gmail.com", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar un agente correctamente con número de teléfono vacío
				"joaquín", "passwordtest9", "joaquín", "perez", "calle Huertas nº 3", "", "joaquin@gmail.com", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		Agent agent;
		UserAccount userAccount;

		caught = null;
		try {
			agent = this.agentService.create();
			agent.setName(name);
			agent.setSurname(surname);
			agent.setPostalAddress(postalAdress);
			agent.setPhone(phone);
			agent.setEmail(email);
			userAccount = agent.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			agent.setUserAccount(userAccount);
			agent = this.agentService.save(agent);
			this.agentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);

	}

	//Test para editar los datos personales de un Agente
	@Test
	public void driverEditAgent() {

		final Object testingData[][] = {
			{
				//Edito correctamente mi nombre
				"agent1", "agent1 name edited", "surname agent 1", "postal Adress agent 1", "617557203", "agent1@acmenewspaper.com", null
			}, {
				//Edito correctamente mis apellidos
				"agent1", "agent 1", "surname agent 1 edited", "postal Adress user 1", "617557203", "agent1@acmenewspaper.com", null
			}, {
				//Edito correctamente mi dirección
				"agent1", "agent 1", "surname agent 1", "postal Adress agent 1 edited", "617557203", "agent1@acmenewspaper.com", null
			}, {
				//Edito correctamente mi email
				"agent1", "agent 1", "surname agent 1", "postal Adress agent 1", "617557203", "agent1EDITED@acmenewspaper.com", null
			}, {
				//Edito incorrectamente mi nombre por uno en blanco
				"agent1", "", "surname agent 1", "postal Adress agent 1", "617557203", "agent1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito incorrectamente mis apellidos por uno en blanco
				"agent1", "agent 1", "", "postal Adress agent 1", "617557203", "agent1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito incorrectamente mi email por uno que no cumple el formato
				"agent1", "agent 1", "surname agent 1", "postal Adress agent 1", "617557203", "agent1", ConstraintViolationException.class
			}, {
				//Edito incorrectamente mi email por uno en blanco
				"agent1", "", "surname agent 1", "postal Adress agent 1", "617557203", "", ConstraintViolationException.class
			}, {
				//Edito correctamente mi número de teléfono por uno vacío
				"agent1", "agent 1", "surname agent 1", "postal Adress agent 1", "", "agent1@acmenewspaper.com", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAgent(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateEditAgent(final int userNameId, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		Agent agent;
		agent = this.agentService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(agent.getUserAccount().getUsername());
			agent.setName(name);
			agent.setSurname(surname);
			agent.setPostalAddress(postalAdress);
			agent.setPhone(phone);
			agent.setEmail(email);
			agent = this.agentService.save(agent);
			this.unauthenticate();
			this.agentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}

	//Test para el caso de uso 4.3)List the newspapers in which they have placed and advertisement
	@Test
	public void driverListNewspaperWithAnAdvertisement() {
		final Object testingData[][] = {
			{
				//El agente 1 lista correctamente todos periódicos sobre los que el ha escrito un aviso
				//El 5 está entre ellos el cual contiene el aviso 3
				"agent1", "newspaper5", "advertisement3", null

			}, {
				//El agente 1 lista incorrectamente el periódico 6 ya que no ha escrito
				//ningún aviso sobre el
				"agent1", "newspaper6", "advertisement2", java.lang.IllegalArgumentException.class
			}, {
				//El agente 1 lista el periódico 3 sobre el cual ha escrito el aviso 
				//número2 no el aviso4
				"agent1", "newspaper3", "advertisement4", java.lang.IllegalArgumentException.class
			}, {
				//El agente 1 lista el periódico 3 sobre el cual ha escrito el aviso 
				//número2 
				"agent1", "newspaper3", "advertisement2", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNewspaperWithAnAdvertisement(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateListNewspaperWithAnAdvertisement(final int usernameIdLogin, final int newspaperId, final int advertisementId, final Class<?> expected) {
		Class<?> caught;
		Agent agentLogin;
		Collection<Newspaper> newspapers;
		Advertisement advertisement;
		Newspaper newspaper;

		agentLogin = this.agentService.findOne(usernameIdLogin);

		caught = null;
		try {
			super.authenticate(agentLogin.getUserAccount().getUsername());
			//Periódicos en los que el agente ha escrito un aviso
			newspapers = this.newspaperService.findAllNewspaperHavingAtLeastOneAdvertisement();
			advertisement = this.advertisementService.findOne(advertisementId);
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.isTrue(advertisement.getAgent().equals(agentLogin));
			Assert.isTrue(newspapers.contains(newspaper));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}

	//Test para el caso de uso 4.4)List the newspapers in which they have not placed and advertisement
	@Test
	public void driverListNewspaperWithCeroAdvertisement() {
		final Object testingData[][] = {
			{
				//El agente 1 lista correctamente todos periódicos sobre los que no ha escrito un aviso
				//El periódico 6 está entre ellos el cual no tiene el aviso2
				"agent1", "newspaper6", "advertisement2", null
			}, {
				//El agente 1 lista incorrectamente el periódico 2 ya que éste sí contiene
				//un aviso escrito por el agente
				"agent1", "newspaper5", "advertisement3", java.lang.IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNewspaperWithCeroAdvertisement(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateListNewspaperWithCeroAdvertisement(final int usernameIdLogin, final int newspaperId, final int advertisementId, final Class<?> expected) {
		Class<?> caught;
		Agent agentLogin;
		Collection<Newspaper> newspapers;
		Newspaper newspaper;
		Advertisement advertisement;

		agentLogin = this.agentService.findOne(usernameIdLogin);

		caught = null;
		try {
			super.authenticate(agentLogin.getUserAccount().getUsername());
			newspaper = this.newspaperService.findOne(newspaperId);
			newspapers = this.newspaperService.findAllNewspaperToWriteAnAdvertisement();
			newspapers.removeAll(this.newspaperService.findAllNewspaperHavingAtLeastOneAdvertisement());
			advertisement = this.advertisementService.findOne(advertisementId);
			Assert.isTrue(!newspaper.getAdvertisements().contains(advertisement));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}
}
