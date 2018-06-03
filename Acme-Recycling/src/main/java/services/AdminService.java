
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import domain.Editor;
import domain.Item;
import domain.LabelProduct;
import domain.Material;
import domain.Newscast;
import domain.Opinion;
import forms.AdminForm;

@Service
@Transactional
public class AdminService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private AdminRepository			adminRepository;
	@Autowired
	private Validator				validator;

	@Autowired
	private MessageFolderService	messageFolderService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public AdminService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Admin create() {
		Admin result;
		UserAccount userAccount;
		Authority authority;

		result = new Admin();
		userAccount = new UserAccount();
		authority = new Authority();

		authority.setAuthority(Authority.ADMIN);
		userAccount.addAuthority(authority);
		userAccount.setActivated(true);
		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Admin> findAll() {
		Collection<Admin> result;
		result = this.adminRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Admin findOne(final int adminId) {
		Assert.isTrue(adminId != 0);
		Admin result;
		result = this.adminRepository.findOne(adminId);
		return result;
	}

	public Admin save(final Admin admin) {
		Assert.notNull(admin);
		Admin result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (admin.getId() == 0) {
			final String password = admin.getUserAccount().getPassword();
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(password, null);
			admin.getUserAccount().setPassword(passwordHash);
		}
		result = this.adminRepository.save(admin);
		Assert.notNull(result);

		if (admin.getId() == 0)
			this.messageFolderService.createDefaultMessageFolder(result);

		return result;
	}

	// Other business methods -------------------------------------------------
	public Admin findByPrincipal() {
		Admin result;
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.adminRepository.findByUserAccountId(userAccount.getId());
		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);

		Assert.isTrue(authorities.contains(auth));
	}

	public boolean checkPrincipalBoolean() {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);

		return (authorities.contains(auth));
	}

	public AdminForm reconstruct(final AdminForm adminForm, final BindingResult bindingResult) {
		final AdminForm result;
		final Admin adminBD;

		if (adminForm.getAdmin().getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			final Collection<Opinion> opinions;

			userAccount = adminForm.getAdmin().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.ADMIN);
			userAccount.addAuthority(authority);
			userAccount.setActivated(true);
			adminForm.getAdmin().setUserAccount(userAccount);
			opinions = new ArrayList<>();
			adminForm.getAdmin().setOpinions(opinions);
			result = adminForm;
		} else {
			adminBD = this.adminRepository.findOne(adminForm.getAdmin().getId());
			adminForm.getAdmin().setId(adminBD.getId());
			adminForm.getAdmin().setVersion(adminBD.getVersion());
			adminForm.getAdmin().setUserAccount(adminBD.getUserAccount());
			adminForm.getAdmin().getUserAccount().setActivated(adminBD.getUserAccount().isActivated());
			adminForm.getAdmin().setOpinions(adminBD.getOpinions());
			result = adminForm;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public void flush() {
		this.adminRepository.flush();
	}

	//QUERY I Las noticias que contengan más comentarios.
	public Collection<Newscast> findNewWithMoreComments() {
		Collection<Newscast> result;
		this.checkPrincipal();

		result = this.adminRepository.findNewWithMoreComments();
		return result;
	}

	//QUERY II Los redactores con mayor número de noticias redactadas.
	public Collection<Editor> findEditorsWithMoreNewsRedacted() {
		Collection<Editor> result;

		this.checkPrincipal();
		result = this.adminRepository.findEditorsWithMoreNewsRedacted();
		return result;
	}

	//QUERY III Las 5 categorías de productos con más productos asociados
	public Collection<LabelProduct> findTop5LabelProducts() {
		Collection<LabelProduct> result;
		final Page<LabelProduct> resPage;
		final Pageable pageable;

		this.checkPrincipal();

		pageable = new PageRequest(0, 5);
		resPage = this.adminRepository.findTop5LabelProducts(pageable);
		result = resPage.getContent();
		return result;
	}

	//QUERY IV La media, el mínimo, el máximo y la desviación típica de productos manejados por los manager.
	public Double[] avgMinMaxAndStddevOfCoursesByBuyer() {

		this.checkPrincipal();
		Double[] result;
		result = this.adminRepository.avgMinMaxAndStddevOfCoursesByBuyer();
		return result;
	}

	//QUERY V La media de las incidencias resueltas
	public Double avgOfIncidencesResolved() {
		this.checkPrincipal();
		Double result;
		result = this.adminRepository.avgOfIncidencesResolved();
		return result;
	}

	//QUERY VI La media de recicladores que han reciclado al menos un producto.
	public Double avgOfRecyclerWithAtLeastOneProduct() {
		this.checkPrincipal();
		Double result;
		result = this.adminRepository.avgOfRecyclerWithAtLeastOneProduct();
		return result;
	}

	//QUERY VII La media de usuarios baneados en el sistema.
	public Double avgOfUsersBanned() {
		this.checkPrincipal();
		Double result;
		result = this.adminRepository.avgOfUsersBanned();
		return result;
	}

	//QUERY VIII La media, el mínimo, el máximo y la desviación típica de comentarios por noticias.
	public Double[] avgMinMaxAndStddevOfCommentsByNews() {
		this.checkPrincipal();
		Double[] result;
		result = this.adminRepository.avgMinMaxAndStddevOfCommentsByNews();
		return result;
	}

	//QUERY IX Items que se han subido al sistema en el último mes.
	public Collection<Item> findLatestItems() {
		this.checkPrincipal();
		Collection<Item> result;
		Calendar calendar;
		Date since;

		calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -31);
		since = calendar.getTime();

		result = this.adminRepository.findLatestItems(since);

		return result;
	}

	//QUERY X Nombre del reciclador y título del Item que más valor tiene del sistema.
	public String[] nameTitleRecyclerWithItemMostValue() {
		this.checkPrincipal();
		String[] result;
		result = this.adminRepository.nameTitleRecyclerWithItemMostValue();
		return result;
	}

	//QUERY XI La media, el mínimo, el máximo y la desviación típica peticiones por manager.
	public Double[] avgMinMaxAndStddevOfRequestByManager() {
		this.checkPrincipal();
		Double[] result;
		result = this.adminRepository.avgMinMaxAndStddevOfRequestByManager();
		return result;
	}

	//QUERY XII Los 3 materiales más demandados.
	public Collection<Material> findTop3Materials() {
		Collection<Material> result;
		final Page<Material> resPage;
		final Pageable pageable;

		this.checkPrincipal();

		pageable = new PageRequest(0, 3);
		resPage = this.adminRepository.findTop3Materials(pageable);
		result = resPage.getContent();
		return result;
	}

	//QUERY XIII La media de transportistas que han tenido al menos una solicitud frente a los que no han tenido ninguna.
	public Double ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest() {
		this.checkPrincipal();
		Double result;
		result = this.adminRepository.ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest();
		return result;
	}
}
