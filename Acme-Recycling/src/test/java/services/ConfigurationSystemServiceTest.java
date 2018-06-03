
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.ConfigurationSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationSystemServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	ConfigurationSystemService	configurationSystemService;

	@PersistenceContext
	EntityManager				entityManager;


	// Test Edit ----------------------------------------------------------------------------------

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//Se edita el configurationSystem por un admin
				"admin", "name1", "https://tinyurl.com/adventure-meetup", "hello", "hola", 12, 12, "http://www.picture1.com", "hola", "hola", "hola", "hola", null

			}, {
				//Se edita incorrectamente el configurationSystem por un admin por poner el nombre en blanco
				"admin", "", "https://tinyurl.com/adventure-meetup", "hello", "hola", 12, 12, "http://www.picture1.com", "hola", "hola", "hola", "hola", javax.validation.ConstraintViolationException.class
			}, {
				//Se edita incorrectamente el configurationSystem por un admin por poner uno de los mensajes de bienvenida en blanco
				"admin", "hola", "https://tinyurl.com/adventure-meetup", "", "hola", 12, 12, "http://www.picture1.com", "hola", "hola", "hola", "hola", javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (int) testingData[i][5], (int) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (Class<?>) testingData[i][12]);
	}
	private void templateEdit(final String username, final String name, final String banner, final String englishWelcomeMessage, final String spanishWelcomeMessage, final int maxNumberFinder, final int cacheMaxTime, final String aboutUsPicture,
		final String locationEnglish, final String locationSpanish, final String whoAreWeEnglish, final String whoAreWeSpanish, final Class<?> expected) {
		ConfigurationSystem configurationSystem;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			configurationSystem = this.configurationSystemService.findOne();
			configurationSystem.setEnglishWelcomeMessage(englishWelcomeMessage);
			configurationSystem.setSpanishWelcomeMessage(spanishWelcomeMessage);
			configurationSystem.setName(name);
			configurationSystem.setBanner(banner);
			configurationSystem.setCacheMaxTime(cacheMaxTime);
			configurationSystem.setMaxNumberFinder(maxNumberFinder);
			configurationSystem.setAboutUsPicture(aboutUsPicture);
			configurationSystem.setLocationEnglish(locationEnglish);
			configurationSystem.setLocationSpanish(locationSpanish);
			configurationSystem.setWhoAreWeEnglish(whoAreWeEnglish);
			configurationSystem.setWhoAreWeSpanish(whoAreWeSpanish);
			configurationSystem = this.configurationSystemService.save(configurationSystem);
			this.unauthenticate();
			this.configurationSystemService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
}
