
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Admin;
import domain.Message;
import domain.MessageFolder;
import domain.TabooWord;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services ----------------------------------------------------
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private TabooWordService tabooWordService;
	
	@Autowired
	private MessageFolderService messageFolderService;
	
	@Autowired
	private AdminService adminService;

	// Constructors -----------------------------------------------------------
	public ActorService() {
		super();
	}
	// Simple CRUD methods ----------------------------------------------------

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;

	}
	
	public Collection<Actor> findAll(){
		
		Collection<Actor> result;
		
		result = this.actorRepository.findAll();
		
		return result;
		
	}
	
	// Other business methods -------------------------------------------------

	public boolean isAuthenticated() {
		boolean result = false;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		if (userAccount != null)
			result = true;
		return result;
	}
	
	public Actor findPrincipal() {
		Actor result;
		int userAccountId;
		userAccountId = LoginService.getPrincipal().getId();
		result = this.actorRepository.findActorByUseraccount(userAccountId);
		Assert.notNull(result);

		return result;
	} 

	
	
	
	public Collection<Actor> actorForBan(){
		
		Collection<Actor> result;
		Set<Message> messages;
		Collection<TabooWord> tabooWords;
		Collection<Actor> actors;
		MessageFolder messageFolder;
		Admin princial;
		
		tabooWords = this.tabooWordService.findAll();
		result = new ArrayList<>();
		actors = this.actorRepository.findAll();
		messageFolder = null;
		princial = this.adminService.findByPrincipal();
		
		Assert.isTrue(princial instanceof Admin);
		
		actors.remove(princial);
		
		
		
		for(Actor a: actors){
			messages = new HashSet<>();
			
			for(TabooWord t: tabooWords){
				
				messageFolder = this.messageFolderService.findMessageFolderByNameAndActor("Out box", a.getId());
								
				messages.addAll(this.messageService.findMessageWithTabooWord(messageFolder.getId(), t.getName()));
				
				
				
			}
			if(messages.size() > 5){
				result.add(a);
				
			}
		}
		
		return result;
	}
	
	public boolean ban(UserAccount userAcount) {

		boolean result;

		userAcount.setActivated(false);

		result = userAcount.isActivated();

		return result;

	}

	public boolean unban(UserAccount userAccount) {

		boolean result;

		userAccount.setActivated(true);

		result = userAccount.isActivated();

		return result;

	}
	
	public void flush(){
		this.actorRepository.flush();
	}
}
