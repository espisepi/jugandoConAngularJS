
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Admin;
import domain.TabooWord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TabooWordServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	TabooWordService	tabooWordService;

	@Autowired
	AdminService		adminService;

	@PersistenceContext
	EntityManager		entityManager;


	//Edit a taboo word
	@Test
	public void driveEditTabooWord() {

		final Object testingData[][] = {
			//Admin edit name, positive case
			{
				"tabooWord5", "admin", "prueba", null
			},
			//Admin edit blank name, negative case
			{
				"tabooWord5", "admin", "", javax.validation.ConstraintViolationException.class
			},
			//Admin edit default taboo word, negative case
			{
				"tabooWord1", "admin", "prueba", java.lang.IllegalArgumentException.class
			},
			//Recycler edit default taboo word, negative case
			{
				"tabooWord5", "recycler1", "prueba", java.lang.IllegalArgumentException.class
			},
			//Editor1 edit default taboo word, negative case
			{
				"tabooWord5", "editor1", "prueba", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTabooWord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	public void templateEditTabooWord(final String entity, final String username, final String name, final Class<?> expected) {

		Class<?> caught;
		TabooWord tabooWord;

		caught = null;

		try {
			super.authenticate(username);
			tabooWord = this.tabooWordService.findOne(super.getEntityId(entity));
			tabooWord.setName(name);
			this.tabooWordService.save(tabooWord);
			this.tabooWordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}

	//Create a tabooWord
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea una palabra tabú correctamente
				"admin", "hola", null

			}, {
				//Se crea una palabra tabú incorrecta ya que dejamos el nombre vacío
				"admin", "", javax.validation.ConstraintViolationException.class

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateCreateAndSave(final String username, final String name, final Class<?> expected) {
		Class<?> caught;
		TabooWord tabooWord;
		caught = null;

		try {
			super.authenticate(username);
			tabooWord = this.tabooWordService.create();

			tabooWord.setName(name);

			tabooWord = this.tabooWordService.save(tabooWord);
			this.tabooWordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Remove a tabooWord
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina la tabooWord5 correctamente ya que no es por defecto
				"admin", "tabooWord5", null

			}, {
				//Se elimina la tabooWord1 incorrectamente ya que es por defecto
				"admin", "tabooWord1", java.lang.IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int tabooWordId, final Class<?> expected) {
		TabooWord tabooWord;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			tabooWord = this.tabooWordService.findOne(tabooWordId);
			this.tabooWordService.delete(tabooWord);

			this.tabooWordService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Listar todas las palabras tabú de mi sistema
	@Test
	public void driverListTabooWords() {
		final Object testingData[][] = {
			{
				//El admin lista las 5 palabras tabú que hay en el sistema
				"admin", 5, null
			}, {
				//No hay un total de 3 si no de 5 
				"admin", 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListTabooWords(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateListTabooWords(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		Admin adminConnected;
		adminConnected = this.adminService.findOne(usernameId);
		Collection<TabooWord> tabooWords;

		caught = null;
		try {
			super.authenticate(adminConnected.getUserAccount().getUsername());
			tabooWords = this.tabooWordService.findAll();
			Assert.isTrue(tabooWords.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
