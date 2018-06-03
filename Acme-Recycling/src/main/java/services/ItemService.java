
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ItemRepository;
import security.Authority;
import domain.Item;
import domain.Opinion;
import domain.Recycler;

@Service
@Transactional
public class ItemService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ItemRepository	itemRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private RecyclerService	recyclerService;

	@Autowired
	private Validator		validator;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ManagerService	managerService;
	
	@Autowired
	private RequestService requestService;


	// Constructors -----------------------------------------------------------
	public ItemService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Item create() {

		Item result;
		Recycler recyclerPrincipal;
		Collection<Opinion> opinions;
		Date publicationMoment;
		Integer value;

		recyclerPrincipal = this.recyclerService.findByPrincipal();
		opinions = new ArrayList<Opinion>();
		publicationMoment = new Date(System.currentTimeMillis() - 1000);
		value = 0;
		//No copiar la siguiente linea en el reconstruct
		result = new Item();

		result.setOpinions(opinions);
		result.setRecycler(recyclerPrincipal);
		result.setPublicationMoment(publicationMoment);
		result.setValue(value);

		return result;

	}

	public Item findOne(final int itemId) {
		Item result;

		result = this.itemRepository.findOne(itemId);

		return result;
	}

	public Collection<Item> findAll() {
		Collection<Item> result;

		result = this.itemRepository.findAll();

		return result;
	}

	public Item save(final Item item) {
		final Item result;

		Assert.notNull(item);

		if (item.getId() == 0) {
			Date publicationMoment;
			publicationMoment = new Date(System.currentTimeMillis() - 1000);
			item.setPublicationMoment(publicationMoment);
			Assert.isTrue(item.getQuantity()>0.0, "The quantity must be greater than");
		}
		
		
		
		//Si eres MANAGER Solo se puede añadir puntuation a los items de TUS requests
		final Authority authorityForManager = new Authority();
		authorityForManager.setAuthority("MANAGER");
		if (this.actorService.findPrincipal().getUserAccount().getAuthorities().contains(authorityForManager))
			Assert.isTrue(this.managerService.findByPrincipal().getRequests().contains(item.getRequest()), "Can not commit this operation because its illegal");

		result = this.itemRepository.save(item);

		return result;
	}
	public void delete(final Item item) {
		Assert.notNull(item);
		Recycler principal;
		
		principal = this.recyclerService.findByPrincipal();
		
		Assert.isTrue(principal.equals(item.getRecycler()));
		

		if (item.getRequest() != null) {
			Assert.isTrue(item.getRequest().getCarrier() == null || !item.getRequest().getStatus().equals("IN COLLECTION"), "This item is assigned a carrier");
			Assert.isTrue(item.getRequest().getStatus().equals("PENDING") || item.getRequest().getStatus().equals("CLEAN POINT"), "This item will collect it soon or it has finished its process");
			this.requestService.delete(item.getRequest());
		}
		
		
		this.itemRepository.delete(item);
	}

	//Other methods

	public Collection<Item> findItemsByRecycler(final int recyclerId) {

		Collection<Item> result;

		result = this.itemRepository.findItemsByRecycler(recyclerId);

		return result;

	}

	public Collection<Item> findItemsWithFinishedRequest(final int recyclerId) {

		Collection<Item> result;

		result = this.itemRepository.findItemsWithFinishedRequest(recyclerId);

		return result;

	}

	public Item reconstruct(final Item item, final BindingResult bindingResult) {
		Item result;
		Item itemBD;
		Recycler recyclerPrincipal;
		Collection<Opinion> opinions;
		Integer value;

		opinions = new ArrayList<>();
		value = 0;

		if (item.getId() == 0) {

			recyclerPrincipal = this.recyclerService.findByPrincipal();
			item.setPublicationMoment(new Date(System.currentTimeMillis() - 1000));
			item.setRecycler(recyclerPrincipal);
			item.setOpinions(opinions);
			item.setValue(value);

			result = item;
		} else {
			itemBD = this.itemRepository.findOne(item.getId());
			item.setId(itemBD.getId());
			item.setVersion(itemBD.getVersion());
			item.setRecycler(itemBD.getRecycler());
			item.setDescription(itemBD.getDescription());
			item.setLabelProduct(itemBD.getLabelProduct());
			item.setOpinions(itemBD.getOpinions());
			item.setPhoto(itemBD.getPhoto());
			item.setPublicationMoment(itemBD.getPublicationMoment());
			item.setQuantity(itemBD.getQuantity());
			item.setRequest(item.getRequest());
			item.setTitle(itemBD.getTitle());
			item.setValue(itemBD.getValue());

			result = item;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Item> findToOpineByActorId(final int actorId) {
		Collection<Item> result;

		result = this.findAll();
		result.removeAll(new ArrayList<Item>(this.itemRepository.findToOpineByActorId(actorId)));

		return result;
	}

}
