
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ChirpRepository		chirpRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService			userService;

	@Autowired
	private Validator			validator;

	@Autowired
	private AdminService		adminService;

	@Autowired
	private TabooWordService	tabooWordService;


	// Constructors -----------------------------------------------------------
	public ChirpService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Chirp create() {
		Date postedMoment;
		Chirp chirp;
		User userConnected;

		postedMoment = new Date();
		userConnected = this.userService.findByPrincipal();
		chirp = new Chirp();
		chirp.setPostedMoment(postedMoment);
		chirp.setUser(userConnected);

		return chirp;
	}

	public Chirp save(final Chirp chirp) {
		Chirp result;
		Assert.notNull(chirp);
		if (chirp.getId() == 0) {
			Date postedMoment;
			postedMoment = new Date(System.currentTimeMillis() - 1000);
			chirp.setPostedMoment(postedMoment);
		}

		if (chirp.getId() != 0) {
			final Date now = new Date();
			Assert.isTrue(chirp.getPostedMoment().after(now));
		}
		result = this.chirpRepository.save(chirp);
		return result;

	}

	public void delete(final Chirp chirp) {
		Assert.notNull(chirp);
		Assert.isTrue(chirp.getId() != 0);

		this.adminService.checkPrincipal();
		this.chirpRepository.delete(chirp);
	}

	public Chirp findOne(final int chirpId) {

		Assert.isTrue(chirpId != 0);
		Chirp result;
		result = this.chirpRepository.findOne(chirpId);

		return result;

	}

	public Collection<Chirp> findAll() {

		Collection<Chirp> result;

		result = this.chirpRepository.findAll();

		return result;

	}

	// Other business methods -------------------------------------------------
	public Collection<Chirp> getChirpsOfMyFollowers(final int userId) {
		Collection<Chirp> result;
		result = this.chirpRepository.getChirpsOfMyFollowers(userId);
		return result;
	}

	public Collection<Chirp> findAllChirpsByUserId(final int userId) {
		Collection<Chirp> result;
		result = this.chirpRepository.findAllChirpsByUserId(userId);
		return result;
	}

	public Collection<Chirp> findChirpWithTabooWord(final String tabooWord) {

		Collection<Chirp> result;

		result = this.chirpRepository.findChirpWithTabooWord(tabooWord);

		return result;
	}

	public Set<Chirp> ChirpWithTabooWord() {

		this.adminService.checkPrincipal();
		
		Set<Chirp> result;
		Collection<String> tabooWords;
		Iterator<String> it;

		result = new HashSet<>();
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		while (it.hasNext())
			result.addAll(this.findChirpWithTabooWord(it.next()));

		return result;

	}

	public void flush() {
		this.chirpRepository.flush();
	}

	public Chirp reconstruct(final Chirp chirp, final BindingResult bindingResult) {
		Chirp result;
		Chirp chirpBD;
		if (chirp.getId() == 0) {
			User userPrincipal;

			userPrincipal = this.userService.findByPrincipal();
			chirp.setUser(userPrincipal);
			result = chirp;
		} else {
			chirpBD = this.chirpRepository.findOne(chirp.getId());
			chirp.setId(chirpBD.getId());
			chirp.setVersion(chirpBD.getVersion());
			chirp.setPostedMoment(chirpBD.getPostedMoment());
			chirp.setUser(chirpBD.getUser());
			chirp.setDescription(chirpBD.getDescription());
			chirp.setTitle(chirpBD.getTitle());

			result = chirp;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}
}
