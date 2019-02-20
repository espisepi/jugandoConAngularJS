
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AgentRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Advertisement;
import domain.Agent;
import forms.AgentForm;

@Service
@Transactional
public class AgentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AgentRepository	agentRepository;

	// Supporting services ----------------------------------------------------
	
	@Autowired 
	private MessageFolderService messageFolderService;

	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------------------------

	public AgentService() {
		super();
	}

	public Agent create() {
		Agent result;
		UserAccount userAccount;
		Authority authority;
		Collection<Advertisement> advertisements;

		result = new Agent();
		userAccount = new UserAccount();
		authority = new Authority();
		advertisements = new ArrayList<Advertisement>();

		authority.setAuthority(Authority.AGENT);
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		result.setAdvertisements(advertisements);

		return result;
	}

	public Collection<Agent> findAll() {
		Collection<Agent> result;
		result = this.agentRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Agent findOne(final int agentId) {
		Assert.isTrue(agentId != 0);
		Agent result;
		result = this.agentRepository.findOne(agentId);
		return result;
	}

	public Agent save(final Agent agent) {

		Assert.notNull(agent);
		Agent result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (agent.getId() == 0) {
			final String password = agent.getUserAccount().getPassword();
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(password, null);
			agent.getUserAccount().setPassword(passwordHash);
		}
		result = this.agentRepository.save(agent);

		Assert.notNull(result);
		
		if(agent.getId() == 0)
			
			this.messageFolderService.createDefaultMessageFolder(result);

		return result;
	}

	// Other business methods -------------------------------------------------

	//Para encontrat al agente que esté logeado en ese momento
	public Agent findByPrincipal() {
		Agent result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();

		Assert.notNull(userAccount);
		result = this.agentRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	//Checkear que el que está logueado en ese momento es un agente
	public void checkPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.AGENT);

		Assert.isTrue(authorities.contains(auth));
	}

	//Reconstructor----------------------------------------------------------

	public AgentForm reconstruct(final AgentForm agentForm, final BindingResult binding) {

		AgentForm result = null;
		Agent agent;
		agent = agentForm.getAgent();

		if (agent.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			Collection<Advertisement> advertisements;

			userAccount = agentForm.getAgent().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.AGENT);
			userAccount.addAuthority(authority);
			agentForm.getAgent().setUserAccount(userAccount);
			advertisements = new ArrayList<>();

			agentForm.getAgent().setAdvertisements(advertisements);

			result = agentForm;

		} else {
			agent = this.agentRepository.findOne(agentForm.getAgent().getId());
			agentForm.getAgent().setId(agent.getId());
			agentForm.getAgent().setVersion(agent.getVersion());
			agentForm.getAgent().setUserAccount(agent.getUserAccount());
			agentForm.getAgent().setAdvertisements(agent.getAdvertisements());

			result = agentForm;
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.agentRepository.flush();
	}

}
