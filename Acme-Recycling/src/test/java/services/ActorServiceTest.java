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
import domain.Actor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ActorServiceTest extends AbstractTest{
	
	// Supporting services ---------------------------------------------------
		@Autowired
		private ActorService		 actorService;

		@PersistenceContext
		EntityManager				entityManager;
	
	//8.b Listar los usuarios cuyas cuentas han sido baneadas o pueden serlo

	
		@Test
		public void driveEditNameAdministrator() {

			final Object testingData[][] = {
				//Manager5 está contenido en la lista para banear.
				{
					"admin", "manager5", 2, null
				},
				//Manager1 no está contenido en la lista para banear.
				{
					"admin", "manager1", 2, IllegalArgumentException.class
				}
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateListbanActor((String) testingData[i][0],(String) testingData[i][1], (int) testingData[i][2],
					(Class<?>) testingData[i][3]);

		}

		public void templateListbanActor(String username, String username1, int numBan, Class<?> expected) {

			Class<?> caught;
			caught = null;
			Collection<Actor> actors;
			Actor actor;

			try {
				this.authenticate(username);
				actor = this.actorService.findOne(super.getEntityId(username1));
				actors = this.actorService.actorForBan();
				Assert.isTrue(actors.size() == numBan);
				Assert.isTrue(actors.contains(actor));
				this.unauthenticate();
				this.actorService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);
			super.unauthenticate();
		}

}
