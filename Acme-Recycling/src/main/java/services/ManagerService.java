
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Incidence;
import domain.Manager;
import domain.Opinion;
import domain.Request;
import forms.ManagerForm;

@Service
@Transactional
public class ManagerService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private ManagerRepository		managerRepository;
	@Autowired
	private Validator				validator;

	@Autowired
	private MessageFolderService	messageFolderService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public ManagerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Manager create() {
		Manager result;
		UserAccount userAccount;
		Authority authority;
		Collection<Incidence> incidences;
		Collection<Request> requests;
		Collection<Opinion> opinions;

		result = new Manager();
		userAccount = new UserAccount();
		authority = new Authority();
		incidences = new ArrayList<>();
		requests = new ArrayList<>();
		opinions = new ArrayList<>();

		authority.setAuthority(Authority.MANAGER);
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		userAccount.setActivated(true);
		result.setIncidences(incidences);
		result.setRequests(requests);
		result.setOpinions(opinions);
		return result;
	}

	public Collection<Manager> findAll() {
		Collection<Manager> result;
		result = this.managerRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Manager findOne(final int managerId) {
		Assert.isTrue(managerId != 0);
		Manager result;
		result = this.managerRepository.findOne(managerId);
		return result;
	}

	public Manager save(final Manager manager) {
		Assert.notNull(manager);
		Manager result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (manager.getId() == 0) {
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(manager.getUserAccount().getPassword(), null);
			manager.getUserAccount().setPassword(passwordHash);
		}
		result = this.managerRepository.save(manager);
		Assert.notNull(result);

		if (manager.getId() == 0)
			this.messageFolderService.createDefaultMessageFolder(result);

		return result;
	}

	//Delete	------------------------------------------------------------
	public void delete(final Manager manager) {
		Assert.notNull(manager);
		Assert.isTrue(manager.getId() != 0);
		this.managerRepository.delete(manager);
	}

	// Other business methods -------------------------------------------------
	public Manager findByPrincipal() {
		Manager result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.managerRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.MANAGER);

		Assert.isTrue(authorities.contains(auth));
	}

	public boolean checkPrincipalBoolean() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.MANAGER);

		return (authorities.contains(auth));
	}

	public ManagerForm reconstruct(final ManagerForm managerForm, final BindingResult binding) {

		ManagerForm result = null;
		Manager managerBD;
		managerBD = managerForm.getManager();

		if (managerBD.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			final Collection<Incidence> incidences;
			final Collection<Request> requests;
			final Collection<Opinion> opinions;

			userAccount = managerForm.getManager().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.MANAGER);
			userAccount.setActivated(true);
			userAccount.addAuthority(authority);
			managerForm.getManager().setUserAccount(userAccount);
			incidences = new ArrayList<>();
			requests = new ArrayList<>();
			opinions = new ArrayList<>();
			managerForm.getManager().setIncidences(incidences);
			managerForm.getManager().setRequests(requests);
			managerForm.getManager().setOpinions(opinions);
			result = managerForm;

		} else {

			managerBD = this.managerRepository.findOne(managerForm.getManager().getId());
			managerForm.getManager().setId(managerBD.getId());
			managerForm.getManager().setVersion(managerBD.getVersion());
			managerForm.getManager().setUserAccount(managerBD.getUserAccount());
			managerForm.getManager().getUserAccount().setActivated(managerBD.getUserAccount().isActivated());
			managerForm.getManager().setIncidences(managerBD.getIncidences());
			managerForm.getManager().setRequests(managerBD.getRequests());
			managerForm.getManager().setOpinions(managerBD.getOpinions());

			result = managerForm;

		}

		this.validator.validate(result, binding);

		return result;

	}

	public void flush() {
		this.managerRepository.flush();
	}

	public Manager findManagerByIncidence(final int incidenceId) {
		Manager result;

		result = this.managerRepository.findManagerByIncidence(incidenceId);

		return result;

	}

	public Manager findByRequestId(final int requestId) {
		Manager result;

		result = this.managerRepository.findByRequestId(requestId);

		return result;
	}
}
