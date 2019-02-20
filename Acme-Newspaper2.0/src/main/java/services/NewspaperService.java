
package services;

import java.util.ArrayList;
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

import repositories.NewspaperRepository;
import domain.Advertisement;
import domain.Agent;
import domain.Article;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class NewspaperService {

	// Managed repository -----------------------------------------------------

	@Autowired
	NewspaperRepository	newspaperRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	UserService			userService;

	@Autowired
	ArticleService		articleService;

	@Autowired
	AdminService		adminService;

	@Autowired
	CustomerService		customerService;

	@Autowired
	SubscriptionService	subscriptionService;

	@Autowired
	TabooWordService	tabooWordService;

	@Autowired
	AgentService		agentService;

	@Autowired
	VolumeService		volumeService;

	//Importar la que pertenece a Spring
	@Autowired
	private Validator	validator;


	// Constructors -----------------------------------------------------------

	public NewspaperService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	//CREATE
	public Newspaper create() {
		final Newspaper result;
		User userPrincipal;
		final Collection<Article> articles;
		Collection<Advertisement> advertisements;

		userPrincipal = this.userService.findByPrincipal();
		articles = new ArrayList<Article>();
		advertisements = new ArrayList<Advertisement>();
		result = new Newspaper();
		result.setArticles(articles);
		result.setPublisher(userPrincipal);
		result.setAdvertisements(advertisements);

		return result;
	}

	//SAVE
	public Newspaper save(final Newspaper newspaper) {
		Newspaper result;

		Assert.notNull(newspaper);

		Assert.isTrue(newspaper.getPublisher().equals(this.userService.findByPrincipal()));

		result = this.newspaperRepository.save(newspaper);

		return result;
	}

	//SAVE que hace falta para advertisement
	public Newspaper saveA(final Newspaper newspaper) {
		Newspaper result;
		Assert.notNull(newspaper);

		result = this.newspaperRepository.save(newspaper);
		return result;
	}

	//DELETE
	public void delete(final Newspaper newspaper) {

		Collection<Subscription> subscriptions;
		Collection<Volume> volumes;

		subscriptions = this.subscriptionService.findSubscriptionByNewspaper(newspaper.getId());

		Assert.notNull(newspaper);
		Assert.notNull(this.adminService.findByPrincipal());
		//Solo se pueden eliminar los newspaper publicos
		Assert.isTrue(newspaper.isOpen() || (newspaper.isOpen() == false && subscriptions.size() == 0), "Se pueden eliminar los periodicos publicos y privado que aún no tengas suscripciones");

		//Si el newspaper tiene volume hay que quitar de ese volume el newspaper
		volumes = this.volumeService.findByNewspaperId(newspaper.getId());
		for (final Volume v : volumes)
			v.getNewspapers().remove(newspaper);

		this.newspaperRepository.delete(newspaper);
	}

	public Collection<Newspaper> findAll() {

		Collection<Newspaper> result;

		result = this.newspaperRepository.findAll();

		return result;

	}

	public Newspaper findOne(final int newspaperId) {

		Assert.isTrue(newspaperId != 0);
		Newspaper result;
		result = this.newspaperRepository.findOne(newspaperId);

		return result;

	}

	public Collection<Newspaper> findNewspapersByKeyword(final String keyWord) {
		Collection<Newspaper> result;
		result = this.newspaperRepository.findNewspapersByKeyword(keyWord);
		return result;
	}
	public Collection<Newspaper> findNewspapersByKeywordAuthenticate(final String keyWord) {
		Collection<Newspaper> result;
		result = this.newspaperRepository.findNewspapersByKeywordAuthenticate(keyWord);
		return result;
	}

	public void flush() {
		this.newspaperRepository.flush();
	}

	// Other business methods -------------------------------------------------

	//PUBLICAR
	public void publish(Newspaper newspaper) {

		Assert.isTrue(newspaper.getId() != 0);
		Assert.isTrue(newspaper.getPublisher().equals(this.userService.findByPrincipal()), "el publicador del periodico debe ser el mismo que el logueado");
		Assert.isTrue(this.isAllFinalMode(newspaper.getId()), "todos sus articulos tienen que estar en modo final");
		Assert.isTrue(newspaper.getArticles().size() != 0, "tiene que tener al menos un articulo para publicarse");
		Assert.isNull(newspaper.getPublicationDate(), "la fecha de publicacion tiene que estar vacia");

		newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1000));
		newspaper = this.save(newspaper);
	}

	private boolean isAllFinalMode(final int newsPaperId) {
		Collection<Article> articlesWithoutFinalMode;
		boolean result;

		articlesWithoutFinalMode = this.newspaperRepository.isAllFinalMode(newsPaperId);
		result = true;
		if (articlesWithoutFinalMode.size() != 0)
			//Esto significa que existe al menos un articulo que esta en modo borrador
			result = false;
		return result;
	}

	public Newspaper findByArticleId(final int articleId) {
		Newspaper result;

		result = this.newspaperRepository.findByArticleId(articleId);

		return result;
	}

	public Collection<Newspaper> findNewspapersCreatedByUser() {
		Collection<Newspaper> result;
		User userPrincipal;

		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findNewspapersCreatedByUser(userPrincipal.getId());
		return result;
	}

	public Collection<Newspaper> findNewspapersCreatedByUserAndNotPublished() {
		Collection<Newspaper> result;
		User userPrincipal;
		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findNewspapersCreatedByUserAndNotPublished(userPrincipal.getId());
		return result;
	}
	public Collection<Newspaper> findAllNewspapersPublicByUser() {
		Collection<Newspaper> result;
		User userPrincipal;

		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findAllNewspapersPublicByUser(userPrincipal.getId());
		return result;
	}
	public Collection<Newspaper> findAllNewspapersPrivateByUser() {
		Collection<Newspaper> result;
		User userPrincipal;

		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findAllNewspapersPrivateByUser(userPrincipal.getId());
		return result;

	}

	public Collection<Newspaper> findNewspapersPublishedAndOpen() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspapersPublishedAndOpen();

		return result;
	}

	public Collection<Newspaper> findNewspaperNotPublished() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspaperNotPublished();

		return result;
	}

	public Collection<Newspaper> findNewspapersPublished() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspapersPublished();

		return result;
	}

	public Collection<Newspaper> findByUserId(final int userId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findByUserId(userId);

		return result;
	}

	public Collection<Newspaper> findPrivateAndPublicatedNewspapersToSubscribe() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findPrivateAndPublicatedNewspapersToSubscribe();

		return result;
	}

	public Collection<Newspaper> findNewspapersSubscribedByCustomerId(final int customerId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspapersSubscribedByCustomerId(customerId);

		return result;
	}

	public Collection<Newspaper> findNewspapersByCustomerId(final int customerId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspapersByCustomerId(customerId);

		return result;
	}

	public Collection<Newspaper> findAllNewspapersByAdmin(final String keyword) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAllNewspapersByAdmin(keyword);

		return result;
	}

	public Collection<Newspaper> findNewsPapersSearchForCustomer(final String keyWord) {
		final Collection<Newspaper> newspapersWord;
		final Collection<Newspaper> newspapersCustomer;
		Collection<Newspaper> newspaperPublic;
		Customer customer;

		customer = this.customerService.findByPrincipal();
		newspapersWord = this.newspaperRepository.findNewspapersByKeywordAuthenticate(keyWord);
		newspapersCustomer = this.newspaperRepository.findNewspapersByCustomerId(customer.getId());
		newspaperPublic = this.newspaperRepository.findNewspapersByKeyword(keyWord);
		newspapersCustomer.retainAll(newspapersWord);
		newspapersCustomer.addAll(newspaperPublic);
		return newspapersCustomer;

	}

	public Collection<Newspaper> findNewspapersForUser(final String keyWord) {
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperRepository.findNewspapersForUser(keyWord);
		return newspapers;
	}

	public Newspaper reconstruct(final Newspaper newspaper, final BindingResult bindingResult) {
		Newspaper result;
		Newspaper newspaperBD;
		if (newspaper.getId() == 0) {
			User userPrincipal;
			final Collection<Article> articles;
			Collection<Advertisement> advertisements;

			userPrincipal = this.userService.findByPrincipal();
			articles = new ArrayList<Article>();
			advertisements = new ArrayList<Advertisement>();
			newspaper.setArticles(articles);
			newspaper.setPublisher(userPrincipal);
			newspaper.setAdvertisements(advertisements);
			result = newspaper;
		} else {
			newspaperBD = this.newspaperRepository.findOne(newspaper.getId());
			newspaper.setId(newspaperBD.getId());
			newspaper.setVersion(newspaperBD.getVersion());
			newspaper.setPublicationDate(newspaperBD.getPublicationDate());
			newspaper.setPublisher(newspaperBD.getPublisher());
			if (newspaper.getArticles() == null)
				newspaper.setArticles(new ArrayList<Article>());
			else
				newspaper.setArticles(newspaperBD.getArticles());
			if (newspaper.getAdvertisements() == null)
				newspaper.setAdvertisements(new ArrayList<Advertisement>());
			else
				newspaper.setAdvertisements(newspaperBD.getAdvertisements());
			result = newspaper;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Newspaper> findNewspaperWithTabooWord(final String tabooWord) {

		Collection<Newspaper> result;

		result = this.newspaperRepository.findNewspaperWithTabooWord(tabooWord);

		return result;
	}

	public Set<Newspaper> NewspaperWithTabooWord() {

		this.adminService.checkPrincipal();

		Set<Newspaper> result;
		Collection<String> tabooWords;
		Iterator<String> it;

		result = new HashSet<>();
		tabooWords = this.tabooWordService.findTabooWordByName();
		it = tabooWords.iterator();
		while (it.hasNext())
			result.addAll(this.findNewspaperWithTabooWord(it.next()));

		return result;

	}

	public Collection<Newspaper> findAllNewspaperToWriteAnAdvertisement() {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAllNewspaperToWriteAnAdvertisement();

		return result;
	}

	public Newspaper findNewspaperByAdvertisement(final Advertisement advertisement) {
		Newspaper result;

		result = this.newspaperRepository.findNewspaperByAdvertisement(advertisement.getId());

		return result;
	}

	public Collection<Newspaper> findAllNewspaperHavingAtLeastOneAdvertisement() {
		Collection<Newspaper> result;
		Agent agent;

		agent = this.agentService.findByPrincipal();
		result = this.newspaperRepository.findAllNewspaperHavingAtLeastOneAdvertisement(agent.getId());

		return result;
	}

	public Collection<Newspaper> findAllNewspapersPrivateByVolumeId(final int volumeId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAllNewspapersPrivateByVolumeId(volumeId);

		return result;
	}

	public Collection<Newspaper> findAllNewspapersPublicByVolumeId(final int volumeId) {
		Collection<Newspaper> result;

		result = this.newspaperRepository.findAllNewspapersPublicByVolumeId(volumeId);

		return result;
	}

	public Collection<Newspaper> findAllNewspapersPublicByUserNotPublished() {
		Collection<Newspaper> result;
		User userPrincipal;

		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findAllNewspapersPublicByUserNotPublished(userPrincipal.getId());
		return result;
	}

	public Collection<Newspaper> findAllNewspapersPrivateByUserNotPublished() {
		Collection<Newspaper> result;
		User userPrincipal;

		userPrincipal = this.userService.findByPrincipal();
		result = this.newspaperRepository.findAllNewspapersPrivateByUserNotPublished(userPrincipal.getId());
		return result;
	}

}
