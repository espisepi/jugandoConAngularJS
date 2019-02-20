
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VolumeRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class VolumeService {

	@Autowired
	VolumeRepository	volumeRepository;

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	CustomerService		customerService;

	@Autowired
	UserService			userService;
	@Autowired
	SubscriptionService	subscriptionService;
	@Autowired
	UnderwriteService	underwriteService;

	@Autowired
	private Validator	validator;


	public VolumeService() {

	}
	// Simple CRUD methods ----------------------------------------------------
	//CREATE
	public Volume create() {
		final Volume result;
		result = new Volume();
		return result;
	}

	public Volume findOne(final int volumeId) {
		Volume result;
		result = this.volumeRepository.findOne(volumeId);
		return result;
	}

	public Collection<Volume> findAll() {
		Collection<Volume> result;
		result = this.volumeRepository.findAll();
		return result;
	}

	//SAVE
	public Volume save(final Volume volume) {
		Volume result;
		User userPrincipal;
		Collection<Newspaper> newspapersPrivateOfThisVolume;
		Assert.notNull(volume);

		Collection<Newspaper> newspapers;

		userPrincipal = this.userService.findByPrincipal();

		//Comprobamos si el volume tiene newspaper private, si no lo tiene el volume pasado por parametro no puede estar sin ningun newspaper
		newspapersPrivateOfThisVolume = this.newspaperService.findAllNewspapersPrivateByVolumeId(volume.getId());
		if (newspapersPrivateOfThisVolume.size() == 0) {
			Assert.notNull(volume.getNewspapers(), "The volume must have at least one newspaper");
			Assert.isTrue(volume.getNewspapers().size() != 0, "The volume must have at least one newspaper");
		}

		// Cuando editamos añadimos los periódicos que tenía el volumen antes de ser editado
		if (volume.getId() != 0) {
			Collection<Customer> customerSubcriptos;
			Subscription subcripcion;
			newspapers = this.newspaperService.findAllNewspapersPrivateByVolumeId(volume.getId());
			customerSubcriptos = this.customerService.customerWithUnderwriteToVolumeId(volume.getId());
			if (volume.getNewspapers().size() != 0) {
				for (final Newspaper n : volume.getNewspapers())
					if (n.isOpen() == false)
						for (final Customer customer : customerSubcriptos) {
							CreditCard credito;
							credito = this.underwriteService.credictcardByVolumenAndCustomer(volume.getId(), customer.getId());
							subcripcion = this.subscriptionService.create(n.getId(), customer);
							subcripcion.setCreditCard(credito);
							subcripcion.setCustomer(customer);
							this.subscriptionService.reconstruct(subcripcion, customer);
							this.subscriptionService.save(subcripcion, customer);
						}
				volume.getNewspapers().addAll(newspapers);
			} else
				volume.setNewspapers(newspapers);

		}

		result = this.volumeRepository.save(volume);
		if (volume.getId() == 0)
			userPrincipal.getVolumes().add(result);
		return result;
	}
	//DELETE
	//	public void delete(final Volume volume) {
	//
	//		Collection<Subscription> subscriptions;
	//
	//		subscriptions = this.subscriptionService.findSubscriptionByVolume(newspaper.getId());
	//
	//		Assert.notNull(newspaper);
	//		Assert.notNull(this.adminService.findByPrincipal());
	//		//Solo se pueden eliminar los newspaper publicos
	//		Assert.isTrue(newspaper.isOpen() || (newspaper.isOpen() == false && subscriptions.size() == 0), "Se pueden eliminar los periodicos publicos y privado que aún no tengas suscripciones");
	//
	//		this.newspaperRepository.delete(newspaper);
	//	}

	//other Method Utilities

	public Collection<Volume> myVolumes() {
		Collection<Volume> result;
		User user;
		user = this.userService.findByPrincipal();
		result = this.volumeRepository.myVolumes(user.getId());

		return result;

	}

	public Collection<Newspaper> volumesNewspaper(final int volId) {
		Collection<Newspaper> result;
		result = this.volumeRepository.volumesNewspaper(volId);
		return result;
	}
	public Collection<Volume> volumesWithUnderwriteOneCustomer() {
		Collection<Volume> result;
		Customer principal;
		principal = this.customerService.findByPrincipal();
		result = this.volumeRepository.volumesWithUnderwriteOneCustomer(principal.getId());
		return result;
	}
	public Volume reconstruct(final Volume volume, final BindingResult bindingResult) {
		Volume result;
		Volume volumeBD;
		if (volume.getId() == 0) {
			if (volume.getNewspapers() == null)
				volume.setNewspapers(new ArrayList<Newspaper>());
			result = volume;
		} else {
			volumeBD = this.volumeRepository.findOne(volume.getId());
			volume.setId(volumeBD.getId());
			volume.setVersion(volumeBD.getVersion());
			if (volume.getNewspapers() == null)
				volume.setNewspapers(new ArrayList<Newspaper>());
			result = volume;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Volume> findByNewspaperId(final int newspaperId) {
		Collection<Volume> result;
		result = this.volumeRepository.findByNewspaperId(newspaperId);
		return result;
	}

	public void flush() {
		this.volumeRepository.flush();
	}
}
