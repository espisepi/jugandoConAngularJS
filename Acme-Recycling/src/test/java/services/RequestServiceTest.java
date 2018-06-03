
package services;

import java.util.Collection;
import java.util.List;

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

import utilities.AbstractTest;
import domain.Carrier;
import domain.CleanPoint;
import domain.Item;
import domain.Manager;
import domain.Message;
import domain.MessageFolder;
import domain.Recycler;
import domain.Request;
import forms.RequestForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RequestServiceTest extends AbstractTest {

	@Autowired
	RequestService			requestService;

	@Autowired
	ItemService				itemService;

	@Autowired
	CarrierService			carrierService;

	@Autowired
	RecyclerService			recyclerService;

	@Autowired
	MessageFolderService	messageFolderService;

	@Autowired
	MessageService			messageService;

	@Autowired
	ManagerService			managerService;

	@PersistenceContext
	EntityManager			entityManager;


	//Caso de uso 3.a: El reciclador recibirá un mensaje cuando un transportista vaya a recoger su pedido.
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{

				//Se crea una request correctamente y se comprueba exitosamente que le llega el mensaje al recycler
				"manager1", "item2", "DG180231BB", "title test", "observation test", "IN COLLECTION", null, "carrier1", "recycler2", null
			}, {

				//Se intenta crear una request en un item que ya tiene request
				"manager1", "item1", "DG180231BC", "title test", "observation test", "IN COLLECTION", null, "carrier1", "recycler1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(List<CleanPoint>) testingData[i][6], super.getEntityId((String) testingData[i][7]), super.getEntityId((String) testingData[i][8]), (Class<?>) testingData[i][9]);
	}

	private void templateCreateAndSave(final String username, final int itemId, final String code, final String title, final String observation, final String status, final List<CleanPoint> cleanPoints, final int carrierId, final int recyclerId,
		final Class<?> expected) {
		Class<?> caught;
		RequestForm requestForm;
		final Carrier carrier;
		final Recycler recycler;

		caught = null;
		try {
			super.authenticate(username);

			carrier = this.carrierService.findOne(carrierId);
			recycler = this.recyclerService.findOne(recyclerId);

			requestForm = this.requestService.create(itemId);
			requestForm.getRequest().setCode(code);
			requestForm.getRequest().setTitle(title);
			requestForm.getRequest().setObservation(observation);
			requestForm.getRequest().setStatus(status);
			requestForm.getRequest().setCarrier(carrier);
			this.requestService.save(requestForm);
			this.entityManager.flush();

			//Comprobamos que el recycler de ese item recibe un mensaje de que se ha cambiado el status de su request
			final MessageFolder notificationBox = this.messageFolderService.findMessageFolderByNameAndActor("Notification Box", recycler.getId());
			boolean existsMessage = false;
			final Collection<Message> messages = this.messageService.findMessagesByMessageFolder(notificationBox.getId());
			for (final Message m : messages)
				if (m.getSubject().equals(requestForm.getRequest().getCode() + ": " + requestForm.getRequest().getTitle()))
					existsMessage = true;
			Assert.isTrue(existsMessage);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 4.c: El manager tendrá un listado de todos los productos del sistema(items) que aún no estén solicitadas, para así añadir la solicitud de recogida o de entrega a un punto limpio.
	@Test
	public void driverListToRequest() {
		final Object testingData[][] = {
			{

				//Listar los items sin request y comprobar que NO aparece el item1 ya que tiene request
				"item1", IllegalArgumentException.class
			}, {
				//Listar los items sin request y comprobar que aparece el item2 ya que NO tiene request
				"item2", null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListToRequest(super.getEntityId((String) testingData[i][0]), (Class<?>) testingData[i][1]);
	}

	private void templateListToRequest(final int itemId, final Class<?> expected) {
		Class<?> caught;
		Collection<Item> items;
		Item item;

		caught = null;
		try {
			items = this.requestService.findItemsNonRequest();
			item = this.itemService.findOne(itemId);

			Assert.isTrue(items.contains(item));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Caso de uso 4.b) Cancelar la solicitud o ponerla en finalizado en cualquier momento. Cada vez que se cambie el estado se enviará un mensaje a cada actor que intervenga en dicha solicitud. Una vez el estado esté en finalizado o cancelado no se puede volver a cambiar.
	//Caso de uso 7.c) Cambiar el status a "FINALIZADO" o "CANCELADO" de las solicitudes que tiene asignado. Una vez se cambie el estado a "FINALIZADO" o "CANCELADO" no se puede volver a cambiar.
	@Test
	public void driverChangeStatus() {
		final Object testingData[][] = {
			{

				//El manager5 cambia el status de la request5 a FINISHED y se envia el mensaje a todos los actores que intervienen correctamente
				"manager5", "manager5", "recycler5", "carrier5", "request5", "FINISHED", null
			}, {
				//El carrier5 cambia el status de la request5 a FINISHED y se envia el mensaje a todos los actores que intervienen correctamente
				"carrier5", "manager5", "recycler5", "carrier5", "request5", "FINISHED", null
			}, {
				//El manager1 cambia el status de la request6 a FINISHED pero falla porque no le pertenece esa request
				"manager1", "manager1", "recycler5", "carrier5", "request5", "FINISHED", IllegalArgumentException.class
			}, {
				//El carrier1 cambia el status de la request6 a FINISHED pero falla porque no le pertenece esa request
				"carrier1", "manager5", "recycler5", "carrier5", "request5", "FINISHED", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateChangeStatus((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), super.getEntityId((String) testingData[i][3]), super.getEntityId((String) testingData[i][4]),
				(String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateChangeStatus(final String username, final int managerId, final int recyclerId, final int carrierId, final int requestId, final String status, final Class<?> expected) {
		Class<?> caught;
		RequestForm requestForm;
		Request request;
		Recycler recycler;
		Carrier carrier;
		Manager manager;

		caught = null;
		try {
			super.authenticate(username);

			manager = this.managerService.findOne(managerId);
			carrier = this.carrierService.findOne(carrierId);
			recycler = this.recyclerService.findOne(recyclerId);

			request = this.requestService.findOne(requestId);
			request.setStatus(status);
			requestForm = new RequestForm();
			requestForm.setRequest(request);

			this.requestService.save(requestForm);
			this.entityManager.flush();

			//Comprobamos que el recycler de ese item recibe un mensaje de que se ha cambiado el status de su request
			MessageFolder notificationBox = this.messageFolderService.findMessageFolderByNameAndActor("Notification Box", recycler.getId());
			boolean existsMessage = false;
			Collection<Message> messages = this.messageService.findMessagesByMessageFolder(notificationBox.getId());
			for (final Message m : messages)
				if (m.getSubject().equals(requestForm.getRequest().getCode() + ": " + requestForm.getRequest().getTitle()))
					existsMessage = true;
			Assert.isTrue(existsMessage);

			//Comprobamos que el manager de ese item recibe un mensaje de que se ha cambiado el status de su request
			notificationBox = this.messageFolderService.findMessageFolderByNameAndActor("Notification Box", manager.getId());
			existsMessage = false;
			messages = this.messageService.findMessagesByMessageFolder(notificationBox.getId());
			for (final Message m : messages)
				if (m.getSubject().equals(requestForm.getRequest().getCode() + ": " + requestForm.getRequest().getTitle()))
					existsMessage = true;
			Assert.isTrue(existsMessage);

			//Comprobamos que el carrier de ese item recibe un mensaje de que se ha cambiado el status de su request
			notificationBox = this.messageFolderService.findMessageFolderByNameAndActor("Notification Box", carrier.getId());
			existsMessage = false;
			messages = this.messageService.findMessagesByMessageFolder(notificationBox.getId());
			for (final Message m : messages)
				if (m.getSubject().equals(requestForm.getRequest().getCode() + ": " + requestForm.getRequest().getTitle()))
					existsMessage = true;
			Assert.isTrue(existsMessage);

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 4.e: Puntuar los productos a los cuales ha añadido una solicitud. Un Manager no puede puntuar los productos que no le pertenecen a su solicitud. Solo se pueden puntuar los productos cuyo status de la solicitud esté en "Finalizado".
	@Test
	public void driverAddPuntuation() {
		final Object testingData[][] = {
			{
				//El manager1 añade la puntuacion al item1 correctamente
				"manager1", "item1", 200, null
			}, {
				//El manager2 añade la puntuacion al item1 incorrectamente porque no le pertenece ese item
				"manager2", "item1", 200, IllegalArgumentException.class
			}, {
				//El manager1 añade la puntuacion al item1 incorrectamente porque la puntuacion esta fuera del rango
				"manager1", "item1", 501, ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAddPuntuation((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateAddPuntuation(final String username, final int itemId, final int value, final Class<?> expected) {
		Class<?> caught;
		Item item;

		caught = null;
		try {
			super.authenticate(username);
			item = this.itemService.findOne(itemId);
			item.setValue(value);
			this.itemService.save(item);
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Caso de uso 7.b) Listar las solicitudes que le ha asignado el manager previamente.
	@Test
	public void driverListMyRequestAssigned() {
		final Object testingData[][] = {
			{
				//El carrier3 lista sus solicitudes y tiene la request3
				"carrier3", "request3", null
			}, {
				//El carrier3 lista sus solicitudes y no tiene la request4
				"carrier3", "request4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListMyRequestAssigned((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateListMyRequestAssigned(final String username, final int requestId, final Class<?> expected) {
		Class<?> caught;
		Collection<Request> requests;
		final Request request;

		caught = null;
		try {
			super.authenticate(username);
			requests = this.carrierService.findByPrincipal().getRequests();
			request = this.requestService.findOne(requestId);

			Assert.isTrue(requests.contains(request));
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}
}
