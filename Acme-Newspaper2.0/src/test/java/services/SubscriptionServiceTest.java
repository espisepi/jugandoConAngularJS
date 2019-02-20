
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.Newspaper;
import domain.Subscription;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SubscriptionServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	SubscriptionService	subscriptionService;

	@PersistenceContext
	EntityManager		entityManager;


	//Caso de uso 22.1: Subscribe to a private newspaper by providing a valid credit card.
	@Test
	public void driverCreateAndSave() {
		final Collection<CreditCard> listCreditCards;
		final Iterator<CreditCard> iterator;
		CreditCard creditCardOk;

		listCreditCards = this.createAllCreditCardsForTesting();
		iterator = listCreditCards.iterator();
		creditCardOk = iterator.next();
		final Object testingData[][] = {
			{
				//Se crea una subscription para el newspaper1 (privado y publicado) correctamente
				"customer1", "newspaper1", creditCardOk, null
			}, {
				//Se crea una subscription para el newspaper2 (publico y no publicado) incorrectamente porque es publico y ademas no esta publicado
				"customer1", "newspaper2", creditCardOk, IllegalArgumentException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || HolderName null
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || BrandeName null
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || Number null
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || ExpirationMonth null
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || ExpirationMonth fuera del rango
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || Year null
				//Salta el Assert de checkCreditCard que comprueba el año
				"customer1", "newspaper1", iterator.next(), IllegalArgumentException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || Year fuera del rango
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea una subscription para el newspaper1 (privado y publicado) incorrectamente con una creditCard no válida || CVV fuera del rango
				"customer1", "newspaper1", iterator.next(), javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (CreditCard) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String username, final String newspaperBean, final CreditCard creditCard, final Class<?> expected) {
		Subscription subscription;
		Newspaper newspaper;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.findOne(super.getEntityId(newspaperBean));
			subscription = this.subscriptionService.create(newspaper.getId());
			subscription.setCreditCard(creditCard);
			subscription = this.subscriptionService.save(subscription);
			this.subscriptionService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//	//Other Methods additionals---------------------------------------------------------------------------------------
	private Collection<CreditCard> createAllCreditCardsForTesting() {
		final Collection<CreditCard> result;
		final CreditCard creditCardOK;
		final CreditCard creditcardHolderNameNull;
		final CreditCard creditCardBrandNameNull;
		final CreditCard creditCardNumberNull;
		final CreditCard creditCardExpirationMonthNull;
		final CreditCard creditCardExpirationMonthInvalid;
		final CreditCard creditCardExpirationYearNull;
		final CreditCard creditCardExpirationYearInvalid;
		final CreditCard creditCardCvvInvalid;

		result = new ArrayList<CreditCard>();

		creditCardOK = new CreditCard();
		creditCardOK.setHolderName("Jose Joaquin");
		creditCardOK.setBrandName("La caixa");
		creditCardOK.setNumber("4388576018410707");
		creditCardOK.setExpirationMonth("03");
		creditCardOK.setExpirationYear("20");
		creditCardOK.setCvv(102);
		result.add(creditCardOK);

		creditcardHolderNameNull = new CreditCard();
		creditcardHolderNameNull.setHolderName(null);
		creditcardHolderNameNull.setBrandName("La caixa");
		creditcardHolderNameNull.setNumber("4388576018410707");
		creditcardHolderNameNull.setExpirationMonth("03");
		creditcardHolderNameNull.setExpirationYear("20");
		creditcardHolderNameNull.setCvv(102);
		result.add(creditcardHolderNameNull);

		creditCardBrandNameNull = new CreditCard();
		creditCardBrandNameNull.setHolderName("Jose Joaquin");
		creditCardBrandNameNull.setBrandName(null);
		creditCardBrandNameNull.setNumber("4388576018410707");
		creditCardBrandNameNull.setExpirationMonth("03");
		creditCardBrandNameNull.setExpirationYear("20");
		creditCardBrandNameNull.setCvv(102);
		result.add(creditCardBrandNameNull);

		creditCardNumberNull = new CreditCard();
		creditCardNumberNull.setHolderName("Jose Joaquin");
		creditCardNumberNull.setBrandName("La caixa");
		creditCardNumberNull.setNumber(null);
		creditCardNumberNull.setExpirationMonth("03");
		creditCardNumberNull.setExpirationYear("20");
		creditCardNumberNull.setCvv(102);
		result.add(creditCardNumberNull);

		creditCardExpirationMonthNull = new CreditCard();
		creditCardExpirationMonthNull.setHolderName("Jose Joaquin");
		creditCardExpirationMonthNull.setBrandName("La caixa");
		creditCardExpirationMonthNull.setNumber("4388576018410707");
		creditCardExpirationMonthNull.setExpirationMonth(null);
		creditCardExpirationMonthNull.setExpirationYear("20");
		creditCardExpirationMonthNull.setCvv(102);
		result.add(creditCardExpirationMonthNull);

		creditCardExpirationMonthInvalid = new CreditCard();
		creditCardExpirationMonthInvalid.setHolderName("Jose Joaquin");
		creditCardExpirationMonthInvalid.setBrandName("La caixa");
		creditCardExpirationMonthInvalid.setNumber("4388576018410707");
		creditCardExpirationMonthInvalid.setExpirationMonth("13");
		creditCardExpirationMonthInvalid.setExpirationYear("20");
		creditCardExpirationMonthInvalid.setCvv(102);
		result.add(creditCardExpirationMonthInvalid);

		creditCardExpirationYearNull = new CreditCard();
		creditCardExpirationYearNull.setHolderName("Jose Joaquin");
		creditCardExpirationYearNull.setBrandName("La caixa");
		creditCardExpirationYearNull.setNumber("4388576018410707");
		creditCardExpirationYearNull.setExpirationMonth("03");
		creditCardExpirationYearNull.setExpirationYear(null);
		creditCardExpirationYearNull.setCvv(102);
		result.add(creditCardExpirationYearNull);

		creditCardExpirationYearInvalid = new CreditCard();
		creditCardExpirationYearInvalid.setHolderName("Jose Joaquin");
		creditCardExpirationYearInvalid.setBrandName("La caixa");
		creditCardExpirationYearInvalid.setNumber("4388576018410707");
		creditCardExpirationYearInvalid.setExpirationMonth("03");
		creditCardExpirationYearInvalid.setExpirationYear("200");
		creditCardExpirationYearInvalid.setCvv(102);
		result.add(creditCardExpirationYearInvalid);

		creditCardCvvInvalid = new CreditCard();
		creditCardCvvInvalid.setHolderName("Jose Joaquin");
		creditCardCvvInvalid.setBrandName("La caixa");
		creditCardCvvInvalid.setNumber("4388576018410707");
		creditCardCvvInvalid.setExpirationMonth("03");
		creditCardCvvInvalid.setExpirationYear("20");
		creditCardCvvInvalid.setCvv(6666);
		result.add(creditCardCvvInvalid);

		return result;
	}

}
