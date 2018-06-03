
package controllers.manager;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CarrierService;
import services.CleanPointService;
import services.ItemService;
import services.ManagerService;
import services.RequestService;
import controllers.AbstractController;
import domain.Carrier;
import domain.CleanPoint;
import domain.Item;
import domain.Manager;
import domain.Request;
import forms.RequestForm;

@Controller
@RequestMapping("/request/manager")
public class RequestManagerController extends AbstractController {

	//	Services --------------------------------------------------------

	@Autowired
	private RequestService		requestService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private CarrierService		carrierService;

	@Autowired
	private CleanPointService	cleanPointService;

	@Autowired
	private ItemService			itemService;


	//	Constructors

	public RequestManagerController() {
		super();
	}

	//	Listing ItemNonRequest---------------------------------------------------------
	@RequestMapping(value = "/listItemsNonRequest", method = RequestMethod.GET)
	public ModelAndView listItemNonRequest() {
		ModelAndView result;
		Collection<Item> itemsNonRequest;

		itemsNonRequest = this.requestService.findItemsNonRequest();
		result = new ModelAndView("request/listItem");
		result.addObject("items", itemsNonRequest);
		result.addObject("showButtonRequest", true);
		result.addObject("requestURI", "request/manager/listItemNonRequest.do?d-16544-p=1");
		result.addObject("RequestUriDisplay", "item/display.do");
		return result;
	}

	//	List myRequest---------------------------------------------------------
	@RequestMapping(value = "/listMyRequest", method = RequestMethod.GET)
	public ModelAndView listMyRequest() {
		ModelAndView result;
		final Collection<Request> myRequests;
		Manager managerPrincipal;

		managerPrincipal = this.managerService.findByPrincipal();
		myRequests = managerPrincipal.getRequests();

		result = new ModelAndView("request/list");
		result.addObject("requests", myRequests);
		result.addObject("showButtonChangeStatus", true);
		result.addObject("showButtonAddPuntuation", true);
		result.addObject("requestURI", "request/manager/listMyRequest.do?d-16544-p=1");
		return result;
	}

	// Create Request-----------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int itemId) {
		final ModelAndView result;
		final Item item;
		final RequestForm requestForm;

		//No se puede crear request en un item que ya tiene request
		item = this.itemService.findOne(itemId);
		Assert.isTrue(this.requestService.findItemsNonRequest().contains(item));

		requestForm = this.requestService.create(itemId);
		result = this.createEditModelAndView(requestForm);

		return result;
	}
	//Save Request ---------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;
		Request request;

		request = requestForm.getRequest();
		//Si la lista de cleanPoint es null la ponemos vacia para que no de problemas en los checkeos que se realiza del request en el reconstruct
		if (request.getCleanPoints() == null)
			request.setCleanPoints(new ArrayList<CleanPoint>());
		//Se elimina de la lista el null por si se ha seleccionado en el formulario la opcion de " ------ " ademas de otros puntos limpios
		if (request.getCleanPoints().contains(null))
			request.getCleanPoints().remove(null);

		requestForm = this.requestService.reconstruct(requestForm, binding);
		request = requestForm.getRequest();
		if (binding.hasErrors())
			result = this.createEditModelAndView(requestForm);
		else
			try {
				//Devuelve el request con el status cambiado en funcion de tenga carrier o punto limpio
				request = this.requestService.checkCreateRequest(request);

				this.requestService.save(requestForm);
				result = new ModelAndView("redirect:listMyRequest.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("if you select a carrier,you can not select clean points"))
					result = this.createEditModelAndView(requestForm, "request.dontSelectCleanPoint.error");
				else if (oops.getMessage().equals("if you dont select a carrier, you have to select clean points"))
					result = this.createEditModelAndView(requestForm, "request.dontSelectCarrier.error");
				else
					result = this.createEditModelAndView(requestForm, "request.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndView(final RequestForm requestForm) {
		Assert.notNull(requestForm);
		ModelAndView result;
		result = this.createEditModelAndView(requestForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final RequestForm requestForm, final String messageCode) {
		assert requestForm != null;

		ModelAndView result;
		Collection<Carrier> carriers;
		Collection<CleanPoint> cleanPoints;

		result = new ModelAndView("request/edit");
		carriers = this.carrierService.findAll();
		cleanPoints = this.cleanPointService.findAll();

		result.addObject("requestForm", requestForm);
		result.addObject("carriers", carriers);
		result.addObject("cleanPoints", cleanPoints);
		result.addObject("message", messageCode);

		return result;

	}

	//Change Status
	@RequestMapping(value = "/changeStatus", method = RequestMethod.GET)
	public ModelAndView changeStatus(@RequestParam final int requestId) {
		ModelAndView result;
		Request request;
		final RequestForm requestForm;

		//Solo se puede cambiar el status de TUS requests
		request = this.requestService.findOne(requestId);
		Assert.isTrue(this.managerService.findByPrincipal().getRequests().contains(request), "Can not commit this operation because its illegal");

		//Solo se puede cambiar el status de las request cuyo status no este en finalizado o cancelado
		Assert.isTrue(!request.getStatus().equals("FINISHED") && !request.getStatus().equals("CANCELLED"));

		requestForm = new RequestForm();
		requestForm.setRequest(request);

		result = new ModelAndView("request/changeStatus");
		result.addObject("requestForm", requestForm);
		result.addObject("requestURI", "request/manager/edit.do");

		return result;
	}
	//Save Request ---------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveChangeStatus")
	public ModelAndView saveChangeStatus(RequestForm requestForm, final BindingResult binding) {
		ModelAndView result;

		requestForm = this.requestService.reconstruct(requestForm, binding);
		if (binding.hasErrors())
			result = new ModelAndView("redirect:changeStatus.do?requestId=" + requestForm.getItemId());
		else
			try {
				this.requestService.save(requestForm);
				result = new ModelAndView("redirect:listMyRequest.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:changeStatus.do?requestId=" + requestForm.getItemId());
			}
		return result;
	}

	//Add puntuation
	@RequestMapping(value = "/addPuntuation", method = RequestMethod.GET)
	public ModelAndView addPuntuation(@RequestParam final int requestId) {
		ModelAndView result;
		Item item;

		//Solo se puede añadir puntuation a los items de TUS requests
		item = this.requestService.findItemByRequestId(requestId);
		Assert.isTrue(this.managerService.findByPrincipal().getRequests().contains(item.getRequest()), "Can not commit this operation because its illegal");

		//Solo se puede añadir puntuation en los item cuyas request esten en status "FINISHED"
		Assert.isTrue(item.getRequest().getStatus().equals("FINISHED"));

		result = new ModelAndView("request/addPuntuation");
		result.addObject("item", item);

		return result;
	}
	//Save Request ---------------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAddPuntuation")
	public ModelAndView saveAddPuntutation(Item item, final BindingResult binding) {
		ModelAndView result;

		item = this.requestService.reconstructAddPuntuation(item, binding);

		//Solo se puede añadir puntuation en los item cuyas request esten en status "FINISHED"
		Assert.isTrue(item.getRequest().getStatus().equals("FINISHED"));

		if (binding.hasErrors())
			result = new ModelAndView("redirect:addPuntuation.do?requestId=" + item.getRequest().getId());
		else
			try {
				this.itemService.save(item);
				result = new ModelAndView("redirect:listMyRequest.do");
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:addPuntuation.do?requestId=" + item.getRequest().getId());
			}
		return result;
	}

}
