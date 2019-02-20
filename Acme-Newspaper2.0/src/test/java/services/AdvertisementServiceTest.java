
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
import domain.Advertisement;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdvertisementServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	AdvertisementService	advertisementService;

	@PersistenceContext
	EntityManager			entityManager;


	//Caso de uso 4.2)Register an advertisement and place it in a newspaper.
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
				//Se crea un aviso correctamente con una credit card correcta
				"agent1", "title 1", "http://www.pictureTest.com", "http://www.pictureTest.com", creditCardOk, null
			}, {
				//Se crea un aviso incorrectamente porque lo crea un customer
				"customer1", "title 2", "http://www.pictureTest.com", "http://www.pictureTest.com", creditCardOk, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente porque el título es blanco
				"agent1", "", "http://www.pictureTest.com", "http://www.pictureTest.com", creditCardOk, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente porque el banner no es una imagen
				"agent1", "title 3", "noEsUnaImagen", "http://www.pictureTest.com", creditCardOk, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Holder Name null
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Brand Name null
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Number null
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Expiration Month null
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Expiration Month fuera de rango
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Year null
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), IllegalArgumentException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || Year fuera del rango
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un aviso incorrectamente con una credit card no válida || CVV fuera del rango
				"agent1", "title 3", "http://www.pictureTest.com", "http://www.pictureTest.com", iterator.next(), javax.validation.ConstraintViolationException.class

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (CreditCard) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateCreateAndSave(final String username, final String title, final String banner, final String targetPage, final CreditCard creditCard, final Class<?> expected) {
		Advertisement advertisement;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			advertisement = this.advertisementService.create();

			advertisement.setBanner(banner);
			advertisement.setTitle(title);
			advertisement.setCreditCard(creditCard);
			advertisement.setTargetPage(targetPage);

			advertisement = this.advertisementService.save(advertisement);
			this.advertisementService.flush();
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
		creditCardOK.setHolderName("Miguel Ternero");
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
		creditCardBrandNameNull.setHolderName("Laura Vera");
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
		creditCardExpirationMonthNull.setHolderName("Daniel Lozano");
		creditCardExpirationMonthNull.setBrandName("La caixa");
		creditCardExpirationMonthNull.setNumber("4388576018410707");
		creditCardExpirationMonthNull.setExpirationMonth(null);
		creditCardExpirationMonthNull.setExpirationYear("20");
		creditCardExpirationMonthNull.setCvv(102);
		result.add(creditCardExpirationMonthNull);

		creditCardExpirationMonthInvalid = new CreditCard();
		creditCardExpirationMonthInvalid.setHolderName("Jose Ángel");
		creditCardExpirationMonthInvalid.setBrandName("La caixa");
		creditCardExpirationMonthInvalid.setNumber("4388576018410707");
		creditCardExpirationMonthInvalid.setExpirationMonth("13");
		creditCardExpirationMonthInvalid.setExpirationYear("20");
		creditCardExpirationMonthInvalid.setCvv(102);
		result.add(creditCardExpirationMonthInvalid);

		creditCardExpirationYearNull = new CreditCard();
		creditCardExpirationYearNull.setHolderName("María");
		creditCardExpirationYearNull.setBrandName("La caixa");
		creditCardExpirationYearNull.setNumber("4388576018410707");
		creditCardExpirationYearNull.setExpirationMonth("03");
		creditCardExpirationYearNull.setExpirationYear(null);
		creditCardExpirationYearNull.setCvv(102);
		result.add(creditCardExpirationYearNull);

		creditCardExpirationYearInvalid = new CreditCard();
		creditCardExpirationYearInvalid.setHolderName("Pepita");
		creditCardExpirationYearInvalid.setBrandName("La caixa");
		creditCardExpirationYearInvalid.setNumber("4388576018410707");
		creditCardExpirationYearInvalid.setExpirationMonth("03");
		creditCardExpirationYearInvalid.setExpirationYear("200");
		creditCardExpirationYearInvalid.setCvv(102);
		result.add(creditCardExpirationYearInvalid);

		creditCardCvvInvalid = new CreditCard();
		creditCardCvvInvalid.setHolderName("Maria Antonia");
		creditCardCvvInvalid.setBrandName("La caixa");
		creditCardCvvInvalid.setNumber("4388576018410707");
		creditCardCvvInvalid.setExpirationMonth("03");
		creditCardCvvInvalid.setExpirationYear("20");
		creditCardCvvInvalid.setCvv(6666);
		result.add(creditCardCvvInvalid);

		return result;
	}
}
