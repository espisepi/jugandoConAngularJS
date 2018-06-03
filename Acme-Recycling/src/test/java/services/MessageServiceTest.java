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
	
	
	// Send message
	@Test
	public void driveSendMessage() {
		
		final Object testingData[][] = {
			//buyer1 send message to editor4, positive case
			{
				"buyer1", "buyer1", "editor4", "hola", "hola", "HIGH", null 
			},
			//buyer2 try that buyer3 send message to manager2, negative case
			{
				"buyer2", "buyer3", "manager2", "hola", "hola", "HIGH", java.lang.IllegalArgumentException.class
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
	
	
	// Send spam message 
	@Test
	public void driveSendSpamMessage() {
		
		final Object testingData[][] = {
			//admin send message with taboo word to manager1, positive case
			{
				"admin", "admin", "manager1", "hola sex", "hola", "HIGH", null
			},
			//editor send message without taboo word to manager2, negative case
			{
				"editor2", "editor2", "manager2", "hola", "hola", "HIGH", java.lang.IllegalArgumentException.class
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
	
	
	
	//Delete message 
	@Test
	public void driveDeleteMessage() {
		
		final Object testingData[][] = {
			//recycler1 delete message1, positive case
			{
				"recycler1", "message1", null 
			},
			//editor1 delete message1 that it isn't his, negative case
			{
				"edior1", "message1", java.lang.IllegalArgumentException.class
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
	
	//Change message to other folder
	@Test
	public void driveChangeMessageToFolder() {
		
		final Object testingData[][] = {
			//recycler change message from message folder 1 to notification box, positive case
			{
				"recycler1", "message1", "NotificationBoxRecycler1", null 
			},
			//recycler changer message from message folder to notification box that it isn't his, negative case
			{
				"recycler2", "message1", "NotificationBoxRecycler1", java.lang.IllegalArgumentException.class
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
