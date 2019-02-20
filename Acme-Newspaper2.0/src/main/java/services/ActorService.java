
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services ----------------------------------------------------

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

}
