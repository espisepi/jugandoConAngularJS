
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.IncidenceRepository;
import domain.Actor;
import domain.Admin;
import domain.Incidence;
import domain.Manager;
import domain.Message;
import domain.Recycler;

@Service
@Transactional
public class IncidenceService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private IncidenceRepository	incidenceRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private RecyclerService		recyclerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private TabooWordService	tabooWordService;

	@Autowired
	private Validator			validator;

	@Autowired
	private MessageService		messageService;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private ItemService itemService;


	// Constructors -----------------------------------------------------------

	public IncidenceService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Incidence create() {

		Incidence result;
		Date createdMoment;
		boolean resolved;
		Recycler recycler;

		result = new Incidence();
		createdMoment = new Date(System.currentTimeMillis() - 1000);
		resolved = false;
		recycler = this.recyclerService.findByPrincipal();

		result.setCreatedMoment(createdMoment);
		result.setResolved(resolved);
		result.setRecycler(recycler);

		return result;
	}

	public Collection<Incidence> findAll() {

		Collection<Incidence> result;

		result = this.incidenceRepository.findAll();

		return result;
	}

	public Incidence findOne(final int incidenceId) {

		Assert.notNull(incidenceId);
		Assert.isTrue(incidenceId != 0);

		Incidence result;

		result = this.incidenceRepository.findOne(incidenceId);

		return result;
	}

	public Incidence save(final Incidence incidence) {

		Assert.notNull(incidence);
		Date createdMoment;
		Actor principal;
		Incidence result;
		Message message = null;
		Message messageSend = null;

		createdMoment = new Date(System.currentTimeMillis() - 1000);
		principal = this.actorService.findPrincipal();

		incidence.setCreatedMoment(createdMoment);

		
		Assert.isTrue(principal instanceof Manager || principal instanceof Recycler);
		
		if(principal instanceof Recycler){
			Assert.isTrue(principal.equals(incidence.getRecycler()));
			Assert.isTrue(!this.itemService.findItemsWithFinishedRequest(principal.getId()).isEmpty(), "You don't create incidence because you don't have any item with Finished request");

		}
		
		if (incidence.isResolved() && principal instanceof Manager) {

			message = this.messageService.create();
			message.setBody("Your incidence has been resolved");
			message.setPriority("HIGH");
			message.setRecipient(incidence.getRecycler());
			message.setSubject(incidence.getTitle());
			messageSend = this.messageService.send(message);

			this.messageService.saveMessageInFolder(incidence.getRecycler(), "Notification box", messageSend);
		}
		
		
		result = this.incidenceRepository.save(incidence);

		return result;

	}

	public void delete(final Incidence incidence){

		Actor principal;

		Assert.notNull(incidence);
		Assert.isTrue(incidence.getId() != 0);
		principal = this.actorService.findPrincipal();
		
		Assert.isTrue(principal.equals(incidence.getRecycler()) || principal instanceof Admin);
		Assert.isTrue(incidence.isResolved() == false);
		
		
		Manager manager;
		
		manager = this.managerService.findManagerByIncidence(incidence.getId());
		
		if(manager != null)
		
			manager.getIncidences().remove(incidence);

		this.incidenceRepository.delete(incidence);

	}

	//Other method

	public Collection<Incidence> findIncidencesByRecycler(final int recyclerId) {

		Collection<Incidence> result;

		result = this.incidenceRepository.findIncidencesByRecycler(recyclerId);

		return result;

	}

	public Incidence reconstruct(final Incidence incidence, final BindingResult bindingResult) {

		Incidence result;
		Incidence incedenceBD;
		Recycler recyclerPrincipal;

		if (incidence.getId() == 0) {

			recyclerPrincipal = this.recyclerService.findByPrincipal();
			incidence.setCreatedMoment(new Date(System.currentTimeMillis() - 1000));
			incidence.setRecycler(recyclerPrincipal);
			incidence.setResolved(false);

			result = incidence;
		} else {
			incedenceBD = this.incidenceRepository.findOne(incidence.getId());
			incidence.setId(incedenceBD.getId());
			incidence.setVersion(incedenceBD.getVersion());
			incidence.setRecycler(incedenceBD.getRecycler());
			incidence.setResolved(incedenceBD.isResolved());

			result = incidence;
		}
		this.validator.validate(result, bindingResult);
		return result;

	}

	public Collection<Incidence> findIncidencesWithTabooWord(final String tabooWord) {
		Collection<Incidence> result;

		result = this.incidenceRepository.findIncidencesWithTabooWord(tabooWord);

		return result;
	}

	//	//Para listar todas las incidencias con palabras tabú
	public Set<Incidence> incidencesWithTabooWord() {

		Set<Incidence> result;
		Collection<String> tabooWords;
		Iterator<String> it;

		result = new HashSet<>();
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		while (it.hasNext())
			result.addAll(this.findIncidencesWithTabooWord(it.next()));

		return result;

	}
	
	public Collection<Incidence> findIncidenceNoResolved(){
		
		Collection<Incidence> result;
		
		result = this.incidenceRepository.findIncidenceNoResolved();
		
		return result;
		
	}
	
	public Collection<Incidence> findIncidenceResolved(){
		
		Collection<Incidence> result;
		
		result = this.incidenceRepository.findIncidenceResolved();
		
		return result;
		
	}
	
	public void flush(){
		
		this.incidenceRepository.flush();
		
	}
}
