
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RequestRepository;
import security.Authority;
import domain.Carrier;
import domain.CleanPoint;
import domain.Item;
import domain.Manager;
import domain.Message;
import domain.Recycler;
import domain.Request;
import forms.RequestForm;

@Service
@Transactional
public class RequestService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private RequestRepository	requestRepository;

	@Autowired
	private ItemService			itemService;

	@Autowired
	private Validator			validator;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private CarrierService		carrierService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageService		messageService;

	@Autowired
	private RecyclerService		recyclerService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public RequestForm create(final int itemId) {
		RequestForm result;
		List<CleanPoint> cleanPoints;
		Item item;
		final Request request;

		cleanPoints = new ArrayList<CleanPoint>();

		//No copiar la siguiente linea en el reconstruct
		request = new Request();
		item = this.itemService.findOne(itemId);
		Assert.notNull(item, "item is null");

		request.setCode(this.generatedTicker());
		request.setStatus("PENDING");
		request.setCleanPoints(cleanPoints);

		result = new RequestForm();
		result.setRequest(request);
		result.setItemId(itemId);

		return result;
	}

	public Request findOne(final int requestId) {
		Request result;

		result = this.requestRepository.findOne(requestId);

		return result;
	}

	public Collection<Request> findAll() {
		Collection<Request> result;

		result = this.requestRepository.findAll();

		return result;
	}

	public Request save(final RequestForm requestForm) {
		final Request result;

		Assert.notNull(requestForm);

		result = this.requestRepository.save(requestForm.getRequest());
		if (requestForm.getRequest().getId() == 0) {
			Item item;

			item = this.itemService.findOne(requestForm.getItemId());

			//No se puede crear request en un item que ya tiene request
			Assert.isTrue(this.findItemsNonRequest().contains(item));

			//Añadimos el request al item`
			item.setRequest(result);

			//Añadimos el request al manager cuando lo creamos
			this.managerService.findByPrincipal().getRequests().add(result);
		} else {
			final Authority authorityForCarrier = new Authority();
			authorityForCarrier.setAuthority("CARRIER");
			if (this.actorService.findPrincipal().getUserAccount().getAuthorities().contains(authorityForCarrier))
				Assert.isTrue(this.carrierService.findByPrincipal().getRequests().contains(requestForm.getRequest()), "Can not commit this operation because its illegal");
			final Authority authorityForManager = new Authority();
			authorityForManager.setAuthority("MANAGER");
			if (this.actorService.findPrincipal().getUserAccount().getAuthorities().contains(authorityForManager))
				Assert.isTrue(this.managerService.findByPrincipal().getRequests().contains(requestForm.getRequest()), "Can not commit this operation because its illegal");
		}

		this.sendMessageToChangeStatus(result);

		return result;
	}
	public void delete(final Request request) {
		Assert.notNull(request);
		Manager manager;

		manager = this.managerService.findByRequestId(request.getId());

		manager.getRequests().remove(request);

		this.requestRepository.delete(request);
	}

	public Collection<Item> findItemsNonRequest() {
		Collection<Item> result;

		result = this.requestRepository.findItemsNonRequest();

		return result;
	}

	public Collection<Request> findByActorId(final int id) {
		Collection<Request> result;

		result = this.requestRepository.findByActorId(id);

		return result;
	}

	public Collection<Request> findByCarrierId(final int id) {
		Collection<Request> result;

		result = this.requestRepository.findByCarrierId(id);

		return result;
	}

	public String generatedTicker() {

		Calendar calendar;

		calendar = Calendar.getInstance();
		String ticker;
		String dias;
		String mes;
		ticker = "";

		//ticker = String.valueOf(calendar.get(Calendar.YEAR)).substring(2) + String.valueOf(calendar.get(Calendar.MONTH) + 1);
		dias = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		mes = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		if (dias.length() <= 1)
			ticker = ticker + "0" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
		else
			ticker = ticker + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

		if (mes.length() <= 1)
			ticker = ticker + "0" + String.valueOf(calendar.get(Calendar.MONTH) + 1);
		else
			ticker = ticker + String.valueOf(calendar.get(Calendar.MONTH) + 1);
		ticker = ticker + String.valueOf(calendar.get(Calendar.YEAR)).substring(2);

		final char[] arr = new char[] {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		};
		String cadenaAleatoria = "";
		for (Integer i = 0; i <= 1; i++) {
			final char elegido = arr[(int) (Math.random() * 62)];
			cadenaAleatoria = cadenaAleatoria + elegido;

		}

		final char[] arr2 = new char[] {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
		};
		String cadenaAleatoria2 = "";
		for (Integer i = 0; i <= 1; i++) {
			final char elegido = arr2[(int) (Math.random() * 62)];
			cadenaAleatoria2 = cadenaAleatoria2 + elegido;

		}

		ticker = cadenaAleatoria2 + ticker + cadenaAleatoria;

		return ticker;
	}

	public RequestForm reconstruct(final RequestForm requestForm, final BindingResult binding) {
		final RequestForm result;
		final Request requestBD;
		Request request;

		request = requestForm.getRequest();

		if (request.getId() == 0) {

			request.setStatus("PENDING");

			requestForm.setRequest(request);
			result = requestForm;
		} else {
			//Solo entra aqui cuando se cambia el status

			requestBD = this.findOne(request.getId());
			requestBD.setStatus(request.getStatus());
			requestForm.setRequest(requestBD);

			result = requestForm;
		}
		this.validator.validate(result, binding);
		return result;
	}

	private void sendMessageToChangeStatus(final Request requestWithNewStatus) {
		Message message;
		Message messageSend;
		Recycler recycler;
		Manager manager;
		final Carrier carrier;

		//Enviamos un mensaje al recycler

		recycler = this.recyclerService.findRecyclerByRequest(requestWithNewStatus.getId());
		message = this.messageService.create();
		message.setBody("The status of the request " + requestWithNewStatus.getTitle() + " with code: " + requestWithNewStatus.getCode() + " has been changed to " + requestWithNewStatus.getStatus());
		message.setPriority("HIGH");
		message.setRecipient(recycler);
		message.setSubject(requestWithNewStatus.getCode() + ": " + requestWithNewStatus.getTitle());
		messageSend = this.messageService.send(message);
		this.messageService.saveMessageInFolder(recycler, "Notification box", messageSend);

		//Enviamos un mensaje al manager

		if (this.managerService.findByPrincipal() != null)
			manager = this.managerService.findByPrincipal();
		else
			manager = this.managerService.findByRequestId(requestWithNewStatus.getId());

		message = this.messageService.create();
		message.setBody("The status of the request " + requestWithNewStatus.getTitle() + " with code: " + requestWithNewStatus.getCode() + " has been changed to " + requestWithNewStatus.getStatus());
		message.setPriority("HIGH");
		message.setRecipient(manager);
		message.setSubject(requestWithNewStatus.getCode() + ": " + requestWithNewStatus.getTitle());
		messageSend = this.messageService.send(message);
		this.messageService.saveMessageInFolder(manager, "Notification box", messageSend);

		//Enviamos un mensaje al carrier si tiene asignado un carrier
		if (requestWithNewStatus.getCarrier() != null) {
			carrier = requestWithNewStatus.getCarrier();
			message = this.messageService.create();
			message.setBody("The status of the request " + requestWithNewStatus.getTitle() + " with code: " + requestWithNewStatus.getCode() + " has been changed to " + requestWithNewStatus.getStatus());
			message.setPriority("HIGH");
			message.setRecipient(carrier);
			message.setSubject(requestWithNewStatus.getCode() + ": " + requestWithNewStatus.getTitle());
			messageSend = this.messageService.send(message);
			this.messageService.saveMessageInFolder(carrier, "Notification box", messageSend);
		}

	}

	//	private void sendMessageToChangeStatus(final Request requestWithNewStatus) {
	//
	//		Recycler recycler;
	//		Message message;
	//		Message messageSend;
	//
	//		recycler = this.recyclerService.findRecyclerByRequest(requestWithNewStatus.getId());
	//
	//		message = this.messageService.create();
	//		message.setBody("Your status request has been changed to " + requestWithNewStatus.getStatus());
	//		message.setPriority("HIGH");
	//		message.setRecipient(recycler);
	//		message.setSubject(requestWithNewStatus.getCode());
	//		messageSend = this.messageService.send(message);
	//
	//		this.messageService.saveMessageInFolder(recycler, "Notification box", messageSend);
	//
	//	}

	public Request checkCreateRequest(final Request request) {
		Assert.notNull(request);
		if (request.getCarrier() != null) {
			Assert.isTrue(request.getCleanPoints().isEmpty(), "if you select a carrier,you can not select clean points");
			//Si se ha seleccionado un carrier se pone el status a IN COLLECTION
			request.setStatus("IN COLLECTION");
		} else {
			Assert.isTrue(!request.getCleanPoints().isEmpty(), "if you dont select a carrier, you have to select clean points");
			//Si se ha seleccionado puntos limpios se pone el status a CLEAN POINT
			request.setStatus("CLEAN POINT");
		}

		return request;
	}

	public Item reconstructAddPuntuation(final Item item, final BindingResult binding) {
		Assert.notNull(item);
		Item itemDB;

		itemDB = null;
		if (item.getId() == 0)
			Assert.isTrue(false, "the item must be in the system");
		else {
			itemDB = this.itemService.findOne(item.getId());
			itemDB.setValue(item.getValue());
		}

		Assert.isTrue(this.managerService.findByPrincipal().getRequests().contains(itemDB.getRequest()), "Can not commit this operation because its illegal");
		return itemDB;
	}

	public Item findItemByRequestId(final int requestId) {
		Item result;

		result = this.requestRepository.findItemByRequestId(requestId);

		return result;
	}

	public Request findByAssessmentId(final int assessmentId) {
		Request result;

		result = this.requestRepository.findByAssessmentId(assessmentId);
		return result;
	}

}
