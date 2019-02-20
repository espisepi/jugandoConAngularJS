package services;

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
import domain.Message;
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest{
	
	// Supporting services ----------------------------------------------------
	
		@Autowired	
		private MessageService messageService;
		
		@Autowired
		private ActorService actorService;
		
		@Autowired
		private MessageFolderService messageFolderService;
		
		@PersistenceContext
		EntityManager			entityManager;
		
		
		//13.1. Send message
		@Test
		public void driveSendMessage() {
			
			final Object testingData[][] = {
				//user1 send message to customer, positive case
				{
					"user1", "user1", "customer1", "hola", "hola", "HIGH", null 
				},
				//user2 try that user1 send message to customer1, negative case
				{
					"user2", "user1", "customer1", "hola", "hola", "HIGH", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateSendMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
					(Class<?>) testingData[i][6]);
			
		}
		
		public void templateSendMessage(final String username, final String sender, String recipient, String subject, String body, String priority, final Class<?> expected) {

			Class<?> caught;
			Message result;
			MessageFolder messageFolderRecipient;
			
			
			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageService.create();
				result.setSender(this.actorService.findOne(super.getEntityId(sender)));
				result.setRecipient(this.actorService.findOne(super.getEntityId(recipient)));
				result.setSubject(subject);
				result.setBody(body);
				result.setPriority(priority);
				result = this.messageService.send(result);
				messageFolderRecipient = this.messageFolderService.findMessageFolderByNameAndActor("In box", result.getRecipient().getId());
				Assert.isTrue(messageFolderRecipient.equals(result.getMessageFolder()));
				this.messageService.flush();
				
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}
		
		//14.1. Send message to all actors of system
		@Test
		public void driveSendMessageBroadcast() {
			
			final Object testingData[][] = {
				//admin send message to all actors of system, positive case
				{
					"admin", "admin", "hola", "hola", "HIGH", null 
				},
				//user send message to all actor of system, negative case
				{
					"user1", "user1", "hola", "hola", "HIGH", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateSendMessageBroadcast((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4],
					(Class<?>) testingData[i][5]);
			
		}
		
		public void templateSendMessageBroadcast(final String username, final String sender, String subject, String body, String priority, final Class<?> expected) {

			Class<?> caught;
			Message result;

			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageService.create();
				result.setSender(this.actorService.findOne(super.getEntityId(sender)));
				result.setSubject(subject);
				result.setBody(body);
				result.setPriority(priority);
				this.messageService.sendBroadcast(result);
				this.messageService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}
		
		//13.1. Send spam message 
		@Test
		public void driveSendSpamMessage() {
			
			final Object testingData[][] = {
				//admin send message to all actors of system, positive case
				{
					"user1", "user1", "customer1", "hola sex", "hola", "HIGH", null
				},
				//user send message to all actor of system, negative case
				{
					"user1", "user1", "customer2", "hola", "hola", "HIGH", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateSendSpamMessage((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
					(Class<?>) testingData[i][6]);
			
		}
		
		public void templateSendSpamMessage(final String username, final String sender, String recipient, String subject, String body, String priority, final Class<?> expected) {

			Class<?> caught;
			Message result;
			Message messageSpam;
			MessageFolder messageFolder;

			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageService.create();
				result.setSender(this.actorService.findOne(super.getEntityId(sender)));
				result.setRecipient(this.actorService.findOne(super.getEntityId(recipient)));
				result.setSubject(subject);
				result.setBody(body);
				result.setPriority(priority);
				messageSpam =this.messageService.send(result);
				messageFolder = this.messageFolderService.findMessageFolderByNameAndActor("Spam box", messageSpam.getRecipient().getId());
				Assert.isTrue(messageFolder.equals(messageSpam.getMessageFolder()));
				this.messageService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}
		
		
		
		//13.1. Delete message 
		@Test
		public void driveDeleteMessage() {
			
			final Object testingData[][] = {
				//agente delete message, positive case
				{
					"agent1", "message2", null 
				},
				//user delete message that it isn't his, negative case
				{
					"user1", "message2", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateDeleteMessage((String) testingData[i][0], (String) testingData[i][1],
					(Class<?>) testingData[i][2]);
			
		}
		
		public void templateDeleteMessage(final String username, String message, final Class<?> expected) {

			Class<?> caught;
			Message result;

			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageService.findOne(super.getEntityId(message));
				this.messageService.delete(result);
				this.messageService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}
		
		//13.1. Change message 
		@Test
		public void driveChangeMessageToFolder() {
			
			final Object testingData[][] = {
				//agente change message from In box to notification box, positive case
				{
					"agent1", "message2", "NotificationBoxAgent1", null 
				},
				//user changer message from In box to notification box that it isn't his, negative case
				{
					"user1", "message2", "NotificationBoxAgent1", java.lang.IllegalArgumentException.class
				}
				
			};

			for (int i = 0; i < testingData.length; i++)
				this.templateChangeMessageToFolder((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],
					(Class<?>) testingData[i][3]);
			
		}
		
		public void templateChangeMessageToFolder(final String username, String message, String messageFolderNew, final Class<?> expected) {

			Class<?> caught;
			Message result;
			MessageFolder messageFolder;
			
			
			caught = null;
			

			try {
				super.authenticate(username);
				result = this.messageService.findOne(super.getEntityId(message));
				messageFolder = this.messageFolderService.findOne(super.getEntityId(messageFolderNew));
				this.messageService.saveMessageInFolder(messageFolder.getActor(), messageFolder.getName(), result);
				this.messageService.flush();
			} catch (final Throwable oops) {
				caught = oops.getClass();
				this.entityManager.clear();
			}

			this.checkExceptions(expected, caught);

		}

}
