
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdvertisementRepository;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	AdvertisementRepository	advertisementRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	AgentService			agentService;

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	AdminService			adminService;

	@Autowired
	TabooWordService		tabooWordService;

	//Importar la que pertenece a Spring
	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------------------------

	public AdvertisementService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Advertisement create() {
		Advertisement result;
		Agent agent;

		agent = this.agentService.findByPrincipal();
		result = new Advertisement();
		result.setAgent(agent);

		return result;
	}

	public Advertisement save(final Advertisement advertisement) {

		Advertisement result;
		Assert.notNull(advertisement);

		Assert.isTrue(this.checkCreditCard(advertisement.getCreditCard()), "Invalid credit card");

		result = this.advertisementRepository.save(advertisement);
		return result;
	}

	public Advertisement findOne(final int advertisementId) {
		Assert.isTrue(advertisementId != 0);
		Advertisement result;
		result = this.advertisementRepository.findOne(advertisementId);

		return result;
	}

	public void delete(final Advertisement advertisement) {
		this.adminService.checkPrincipal();
		Assert.notNull(advertisement);

		Newspaper newspaper;

		if (this.newspaperService.findNewspaperByAdvertisement(advertisement) != null) {
			newspaper = this.newspaperService.findNewspaperByAdvertisement(advertisement);
			newspaper.getAdvertisements().remove(advertisement);
		}

		this.advertisementRepository.delete(advertisement);

	}
	public Collection<Advertisement> findAll() {
		Collection<Advertisement> advertisments;
		advertisments = this.advertisementRepository.findAll();
		return advertisments;
	}

	//Other business methods--------------------------------------------------------------

	public boolean checkCreditCard(final CreditCard creditCard) {
		boolean res;
		Calendar calendar;
		int actualYear;
		int actualMonth;

		res = false;
		calendar = new GregorianCalendar();
		actualYear = calendar.get(Calendar.YEAR);
		actualMonth = (calendar.get(Calendar.MONTH) + 1);
		actualYear = actualYear % 100;
		actualMonth = actualMonth % 100;
		if (creditCard.getExpirationYear() != null)
			if (Integer.parseInt(creditCard.getExpirationYear()) > actualYear)
				res = true;
			else if (Integer.parseInt(creditCard.getExpirationYear()) == actualYear && Integer.parseInt(creditCard.getExpirationMonth()) > calendar.get(Calendar.MONTH) + 1)
				res = true;
			else if (Integer.parseInt(creditCard.getExpirationYear()) == actualYear && Integer.parseInt(creditCard.getExpirationMonth()) == actualMonth)
				res = false;
		return res;
	}

	public void flush() {
		this.advertisementRepository.flush();
	}

	//RECONSTRUCTOR--------------------------------------------------------------

	public Advertisement reconstruct(final Advertisement advertisement, final BindingResult bindingResult) {
		Advertisement result;
		Advertisement advertisementBD;
		if (advertisement.getId() == 0) {
			Agent agentPrincipal;

			agentPrincipal = this.agentService.findByPrincipal();
			advertisement.setAgent(agentPrincipal);

			result = advertisement;
		} else {
			advertisementBD = this.advertisementRepository.findOne(advertisement.getId());
			advertisement.setId(advertisementBD.getId());
			advertisement.setVersion(advertisementBD.getVersion());
			advertisement.setAgent(advertisementBD.getAgent());

			result = advertisement;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Advertisement> findAdvertisementWithTabooWord(final String tabooWord) {
		Collection<Advertisement> result;

		result = this.advertisementRepository.findAdvertisementWithTabooWord(tabooWord);

		return result;
	}

	public Set<Advertisement> advertisementWithTabooWord() {

		this.adminService.checkPrincipal();

		Set<Advertisement> result;
		Collection<String> tabooWords;
		Iterator<String> it;

		result = new HashSet<>();
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		while (it.hasNext())
			result.addAll(this.findAdvertisementWithTabooWord(it.next()));

		return result;

	}

	public List<Advertisement> findAdvertisementsByNewspaper(final int newspaperId) {
		List<Advertisement> result;

		result = this.advertisementRepository.findAdvertisementsByNewspaper(newspaperId);

		return result;
	}

	public Advertisement randomAdvertisement(final Newspaper newspaper) {
		List<Advertisement> advertisements;
		Integer size;
		Advertisement advertisement;

		advertisement = new Advertisement();
		//Encontrar todos los advertisements que tiene un periódico
		advertisements = this.findAdvertisementsByNewspaper(newspaper.getId());
		size = advertisements.size();

		if (size != 0) {
			final int rand = (int) (Math.random() * size);
			advertisement = advertisements.get(rand);
		} else
			advertisement = null;
		return advertisement;

	}

}
