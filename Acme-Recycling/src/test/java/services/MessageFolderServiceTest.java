package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageFolderServiceTest extends AbstractTest {
	
	// Supporting services ----------------------------------------------------
	
		@Autowired
		private MessageFolderService messageFolderService;

		
		@PersistenceContext
		EntityManager			entityManager;
		
		//Manage his or her message folder, except for the system folder (create and edit)
		@Test
		public void driveCreateAndEditMessageFolder() {
			
			final Object testingData[][] = {
				//recycler1 create message folder, positive case
				{
					"recycler1", "Prueba", null 
				},
				// 	Buyer create message folder with null name, negative case
				{
					"buyer4", null, javax.validation.ConstraintViolationException.class
				},
				// Editor create message folder with blank name, negative case
				{
					"editor2", "", javax.validation.ConstraintViolationException.class
				},
				//Manager create message folder but it exits, negative case
				{
					"manager1", "In box", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateCreateAndEditMessageFolder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			
		}
		
		public void templateCreateAndEditMessageFolder(final String username, final String name, final Class<?> expected) {

			Class<?> caught;
			MessageFolder result;

			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageFolderService.create();
				result.setName(name);
				this.messageFolderService.saveToPrincipal(result);
				this.messageFolderService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}
		
		// Manage his or her message folder, except for the system folder (delete)
		@Test
		public void driveDeleteMessageFolder() {
			
			final Object testingData[][] = {
				//Admin delete message folder, positive case
				{
					"recycler1", "messageFolder1", null
				},
				//Customer don't delete default folder, negative case
				{
					"editor2", "InBoxEditor2", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateDeleteMessageFolder((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
			
		}
		
		public void templateDeleteMessageFolder(final String username, final String name, final Class<?> expected) {

			Class<?> caught;
			MessageFolder result;

			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageFolderService.findOne(super.getEntityId(name));
				this.messageFolderService.delete(result);
				this.messageFolderService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}

}
