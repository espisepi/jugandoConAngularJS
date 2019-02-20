
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
import domain.Chirp;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	ChirpService	chirpService;

	@Autowired
	UserService		userService;

	@Autowired
	ActorService	actorService;

	@Autowired
	AdminService	adminService;

	@PersistenceContext
	EntityManager	entityManager;


	//Caso de uso 16.1:Post a chirp. (Parte 1)
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea un chirp correctamenre estando logeado como user1
				"user1", "title chirp test", "desciption test", null

			}, {
				//Se crea un chirp incorrectamente ya que estamos logeados como admin
				"admin", "title chirp test", "description test", IllegalArgumentException.class
			}, {
				//Se crea un chirp incorrectamente ya que metemos el titulo vacio
				"user1", "", "description test", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String username, final String title, final String description, final Class<?> expected) {
		Class<?> caught;
		Chirp chirp;
		caught = null;

		try {
			super.authenticate(username);
			chirp = this.chirpService.create();

			chirp.setTitle(title);
			chirp.setDescription(description);
			chirp = this.chirpService.save(chirp);
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 16.1:Chirps may not be changed or deleted once they are posted.(Parte 2)
	// Se listan las los chirps creados por el user logueado y de ellos se coge el pasado por parametro para cambiarles los valores

	//17.5: Remove a chirp that he or she thinks is inappropriate.
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina el chirp1 correctamente estando logeado como admin
				"admin", "chirp1", null
			}, {
				//Se elimina incorrectamenre el chirp1 estando logeado como user1, por lo que no deberia dejar que se eliminase
				"user1", "chirp1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int chirpId, final Class<?> expected) {
		Chirp chirp;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			chirp = this.chirpService.findOne(chirpId);
			this.chirpService.delete(chirp);

			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Use Case 16.5  Display a stream with the chirps posted by all of the users that he or she follows.
	@Test
	public void driverListChirpsOfMyFollowers() {
		final Object testingData[][] = {
			{
				//El user 1 tiene 5 chirps en sus seguidores
				"user1", 5, null
			}, {
				//El user 2 no tiene un total de 4 chirps asociadas a sus seguidores.
				"user2", 4, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListChirpsOfMyFollowers(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateListChirpsOfMyFollowers(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		User userConnected;
		userConnected = this.userService.findOne(usernameId);
		Collection<Chirp> chirps;

		caught = null;
		try {
			super.authenticate(userConnected.getUserAccount().getUsername());
			chirps = this.chirpService.getChirpsOfMyFollowers(usernameId);
			Assert.isTrue(chirps.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
