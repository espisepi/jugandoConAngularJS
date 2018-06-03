
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

import repositories.BuyerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Buy;
import domain.Buyer;
import domain.Course;
import domain.Finder;
import domain.Material;
import domain.Opinion;
import forms.BuyerForm;

@Service
@Transactional
public class BuyerService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private BuyerRepository	buyerRepository;
	@Autowired
	private Validator		validator;

	@Autowired
	FinderService			finderService;
	
	@Autowired
	private MessageFolderService messageFolderService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public BuyerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Buyer create() {
		Buyer result;
		UserAccount userAccount;
		Authority authority;
		final Collection<Buy> buys;
		final Collection<Course> courses;
		final Collection<Opinion> opinions;

		result = new Buyer();
		userAccount = new UserAccount();
		authority = new Authority();
		buys = new ArrayList<>();
		courses = new ArrayList<>();
		opinions = new ArrayList<>();

		authority.setAuthority(Authority.BUYER);
		userAccount.setActivated(true);
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		result.setBuys(buys);
		result.setCourses(courses);
		result.setOpinions(opinions);
		return result;
	}

	public Collection<Buyer> findAll() {
		Collection<Buyer> result;
		result = this.buyerRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Buyer findOne(final int buyerId) {
		Assert.isTrue(buyerId != 0);
		Buyer result;
		result = this.buyerRepository.findOne(buyerId);
		return result;
	}

	public Buyer save(final Buyer buyer) {
		Assert.notNull(buyer);
		Buyer result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (buyer.getId() == 0) {
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(buyer.getUserAccount().getPassword(), null);
			buyer.getUserAccount().setPassword(passwordHash);
		}

		result = this.buyerRepository.save(buyer);
		this.buyerRepository.flush();

		//Codigo para añadir el finder al buyer, este codigo no funciona en controlador pero si en el finderServiceTest, descomentar para probarlo
		if (buyer.getFinder() == null) {
			Finder finder;

			finder = this.finderService.create();
			finder = this.finderService.save(finder);
			this.buyerRepository.flush();
			result = this.findOne(result.getId());
			result.setFinder(finder);
			result = this.buyerRepository.save(result);
			this.buyerRepository.flush();

		}

		Assert.notNull(result);

		//TODO: Falta método creacion de carpetas por defecto
		if (buyer.getId() == 0)
			this.messageFolderService.createDefaultMessageFolder(result);

		return result;
	}

	//Delete	------------------------------------------------------------
	public void delete(final Buyer buyer) {
		Assert.notNull(buyer);
		Assert.isTrue(buyer.getId() != 0);
		this.buyerRepository.delete(buyer);
	}

	// Other business methods -------------------------------------------------
	public Buyer findByPrincipal() {
		Buyer result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.buyerRepository.findByBuyerAccountId(userAccount.getId());
		return result;
	}

	public Buyer findBuyerByCourse(final Course course) {
		Buyer result;

		result = this.buyerRepository.findBuyerByCourse(course.getId());

		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.BUYER);

		Assert.isTrue(authorities.contains(auth));
	}

	public boolean checkPrincipalBoolean() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.BUYER);

		return (authorities.contains(auth));
	}

	public BuyerForm reconstruct(final BuyerForm buyerForm, final BindingResult binding) {

		BuyerForm result = null;
		Buyer buyerBD;
		buyerBD = buyerForm.getBuyer();

		if (buyerBD.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			final Collection<Buy> buys;
			final Collection<Course> courses;
			final Collection<Opinion> opinions;

			userAccount = buyerForm.getBuyer().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.BUYER);
			userAccount.addAuthority(authority);
			buyerForm.getBuyer().setUserAccount(userAccount);
			userAccount.setActivated(true);
			buys = new ArrayList<>();
			courses = new ArrayList<>();
			opinions = new ArrayList<>();
			buyerForm.getBuyer().setBuys(buys);
			buyerForm.getBuyer().setCourses(courses);
			buyerForm.getBuyer().setOpinions(opinions);
			result = buyerForm;

		} else {

			buyerBD = this.buyerRepository.findOne(buyerForm.getBuyer().getId());
			buyerForm.getBuyer().setId(buyerBD.getId());
			buyerForm.getBuyer().setVersion(buyerBD.getVersion());
			buyerForm.getBuyer().setUserAccount(buyerBD.getUserAccount());
			buyerForm.getBuyer().getUserAccount().setActivated(buyerBD.getUserAccount().isActivated());
			buyerForm.getBuyer().setBuys(buyerBD.getBuys());
			buyerForm.getBuyer().setCourses(buyerBD.getCourses());
			buyerForm.getBuyer().setOpinions(buyerBD.getOpinions());

			result = buyerForm;

		}

		this.validator.validate(result, binding);

		return result;

	}

	public void flush() {
		this.buyerRepository.flush();
	}

	public Buyer findBuyerOfBuy(final int buyId) {
		Buyer result;

		result = this.buyerRepository.findBuyerOfBuy(buyId);
		return result;
	}

	public Buyer findBuyerOfFinder(final int finderId) {
		Buyer buyer;

		buyer = this.buyerRepository.findBuyerOfFinder(finderId);

		return buyer;
	}

	public Collection<Material> findAllMaterialsBuyByABuyer() {
		Collection<Material> result;
		Buyer principal;

		principal = this.findByPrincipal();

		result = this.buyerRepository.findAllMaterialsBuyByABuyer(principal.getId());

		return result;
	}

	public Collection<Buy> findAllBuysByABuyer() {
		Collection<Buy> result;

		Buyer principal;

		principal = this.findByPrincipal();

		result = this.buyerRepository.findAllBuysByABuyer(principal.getId());

		return result;
	}

}
