
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SubscriptionRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Subscription;

@Service
@Transactional
public class SubscriptionService {

	// Managed repository -----------------------------------------------------

	@Autowired
	SubscriptionRepository	subscriptionRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	CustomerService			customerService;

	@Autowired
	NewspaperService		newspaperService;

	//Importar la que pertenece a Spring
	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------------------------

	public SubscriptionService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	//CREATE
	public Subscription create(final int newspaperId) {
		Subscription result;
		Customer customerPrincipal;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(!newspaper.isOpen(), "the newspaper must be private");
		customerPrincipal = this.customerService.findByPrincipal();
		result = new Subscription();
		result.setCustomer(customerPrincipal);
		result.setNewspaper(newspaper);

		return result;
	}

	public Subscription create(final int newspaperId, Customer customer) {
		Subscription result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);
		Assert.isTrue(!newspaper.isOpen(), "the newspaper must be private");
		result = new Subscription();
		result.setCustomer(customer);
		result.setNewspaper(newspaper);

		return result;
	}

	//SAVE
	public Subscription save(final Subscription subscription) {
		Customer customerPrincipal;
		Subscription result;

		Assert.notNull(subscription);
		customerPrincipal = this.customerService.findByPrincipal();
		Assert.isTrue(!customerPrincipal.getSubcriptions().contains(subscription), "el cliente ya esta subscrito a este periodico");
		Assert.isTrue(subscription.getCustomer().equals(customerPrincipal), "El cliente de la subscripcion debe ser el mismo que el logueado");
		Assert.isTrue(!subscription.getNewspaper().isOpen(), "solo se pueden subscribir a los periodicos privados");
		Assert.notNull(subscription.getNewspaper().getPublicationDate(), "solo se pueden subscribir a los periodicos publicados");
		Assert.isTrue(this.checkCreditCard(subscription.getCreditCard()), "Invalid credit card");

		result = this.subscriptionRepository.save(subscription);
		return result;
	}

	public Subscription save(final Subscription subscription, Customer customer) {
		Subscription result;

		Assert.notNull(subscription);
		Assert.isTrue(!customer.getSubcriptions().contains(subscription), "el cliente ya esta subscrito a este periodico");
		Assert.isTrue(subscription.getCustomer().equals(customer), "El cliente de la subscripcion debe ser el mismo que el logueado");
		Assert.isTrue(!subscription.getNewspaper().isOpen(), "solo se pueden subscribir a los periodicos privados");
		Assert.notNull(subscription.getNewspaper().getPublicationDate(), "solo se pueden subscribir a los periodicos publicados");
		Assert.isTrue(this.checkCreditCard(subscription.getCreditCard()), "Invalid credit card");

		result = this.subscriptionRepository.save(subscription);
		return result;
	}

	public void flush() {
		this.subscriptionRepository.flush();
	}

	// Other business methods -------------------------------------------------

	public Subscription reconstruct(final Subscription subscription, final BindingResult binding) {
		Subscription result;
		final Customer customerPrincipal;
		if (subscription.getId() == 0) {

			Assert.isTrue(!subscription.getNewspaper().isOpen(), "the newspaper must be private");
			customerPrincipal = this.customerService.findByPrincipal();
			subscription.setCustomer(customerPrincipal);

			result = subscription;
		} else {
			Assert.notNull(null, "a subscription can not be modified");
			result = subscription;
		}
		this.validator.validate(result, binding);
		return result;
	}

	public Subscription reconstruct(final Subscription subscription) {
		Subscription result;
		final Customer customerPrincipal;
		if (subscription.getId() == 0) {

			Assert.isTrue(!subscription.getNewspaper().isOpen(), "the newspaper must be private");
			customerPrincipal = this.customerService.findByPrincipal();
			subscription.setCustomer(customerPrincipal);

			result = subscription;
		} else {
			Assert.notNull(null, "a subscription can not be modified");
			result = subscription;
		}
		//recontruct sin validaciones, estas ya las hace underwrite
		return result;
	}

	public Subscription reconstruct(final Subscription subscription, Customer customer) {
		Subscription result;
		if (subscription.getId() == 0) {

			Assert.isTrue(!subscription.getNewspaper().isOpen(), "the newspaper must be private");
			subscription.setCustomer(customer);

			result = subscription;
		} else {
			Assert.notNull(null, "a subscription can not be modified");
			result = subscription;
		}
		//recontruct sin validaciones, estas ya las hace underwrite
		return result;
	}

	public Collection<Subscription> findSubscriptionByNewspaper(final int newspaperId) {

		Collection<Subscription> result;

		result = this.subscriptionRepository.findSubscriptionByNewspaper(newspaperId);

		return result;
	}

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

}
