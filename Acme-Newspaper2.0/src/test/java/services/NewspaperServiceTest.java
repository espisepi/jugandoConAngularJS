
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NewspaperServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	ArticleService		articleService;

	@PersistenceContext
	EntityManager		entityManager;


	//Caso de uso 6.1: Create a newspaper. A user who has created a newspaper is commonly referred to as a publisher. (parte 1)
	// Se listan las newspapers creadas por el user logueado y de ellas se coge la pasada por parametro para cambiarles los valores
	@Test
	public void driverListEdit() {
		final Object testingData[][] = {
			{
				//Se edita un newspaper correctamente
				"user1", "newspaper1", "title test", "description test", "http://www.pictureTest.com", true, null
			}, {
				//Se edita un newspaper que no aparece en la lista de los newspapers del user logueado
				"user2", "newspaper1", "title test", "description test", "http://www.pictureTest.com", true, IllegalArgumentException.class
			}, {
				//Se edita un newspaper incorrectamente con title en blank
				"user1", "newspaper1", "", "description test", "http://www.pictureTest.com", true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un newspaper incorrectamente con description en blank
				"user1", "newspaper1", "title test", "", "http://www.pictureTest.com", true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un newspaper correctamente con url en null
				"user1", "newspaper1", "title test", "description test", null, true, null
			}, {
				//Se edita un newspaper incorrectamente con url malamente
				"user1", "newspaper1", "title test", "description test", "esto no es una url", true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita un newspaper correctamente poniendolo en privado
				"user1", "newspaper1", "title test", "description test", "http://www.pictureTest.com", false, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (boolean) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateListEdit(final String username, final int newspaperId, final String title, final String description, final String picture, final boolean open, final Class<?> expected) {
		Newspaper newspaper;
		Collection<Newspaper> newspapers;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			//newspapers = new ArrayList<Newspaper>(this.newspaperService.findNewspapersPublishedAndOpen());
			newspapers = new ArrayList<Newspaper>(this.newspaperService.findNewspapersCreatedByUser());
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.isTrue(newspapers.contains(newspaper));

			newspaper.setTitle(title);
			newspaper.setDescription(description);
			newspaper.setPicture(picture);
			newspaper.setOpen(open);
			newspaper = this.newspaperService.save(newspaper);
			this.newspaperService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 6.1: Create a newspaper. A user who has created a newspaper is commonly referred to as a publisher. (parte 2)
	//Caso de uso 23.1: Decide on whether a newspaper that he or she's created is public or private.
	//El caso de uso 23.1 se va a comprobar demostrando que se puede crear un newspaper privado y publico
	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea un newspaper correctamente publico
				"user1", "title test", "description test", "http://www.pictureTest.com", true, null
			}, {
				//Se crea un newspaper correctamente privado
				"user1", "title test", "description test", "http://www.pictureTest.com", false, null
			}, {
				//Se crea un newspaper incorrectamente porque lo crea un customer
				"customer1", "title test", "description test", "http://www.pictureTest.com", true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (boolean) testingData[i][4], (Class<?>) testingData[i][5]);
	}
	private void templateCreateAndSave(final String username, final String title, final String description, final String picture, final boolean open, final Class<?> expected) {
		Newspaper newspaper;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.create();

			newspaper.setTitle(title);
			newspaper.setDescription(description);
			newspaper.setPicture(picture);
			newspaper.setOpen(open);
			newspaper = this.newspaperService.save(newspaper);
			this.newspaperService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 6.2: Publish a newspaper that he or she's created. Note that no newspaper can be published until each of the articles of which it is composed is saved in final mode.
	@Test
	public void driverPublish() {
		final Object testingData[][] = {
			{
				//Se publica un newspaper correctamente ya que no tiene fecha de publicacion
				"user1", "newspaper2", null
			}, {
				//Se publica un newspaper incorrectamente ya que tiene fecha de publicacion
				"user1", "newspaper1", IllegalArgumentException.class
			}, {
				//Se publica un newspaper incorrectamente ya que no pertenece a ese usuario
				"user2", "newspaper1", IllegalArgumentException.class
			}, {
				//Se publica un newspaper incorrectamente ya que el newspaper3 tiene el article4 que esta en modo borrador
				"user1", "newspaper3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templatePublish((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templatePublish(final String username, final int newspaperId, final Class<?> expected) {
		Newspaper newspaper;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.findOne(newspaperId);
			this.newspaperService.publish(newspaper);

			this.newspaperService.flush();
			//Se comprueba que se haya publicado correctamente
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.notNull(newspaper.getPublicationDate());

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 7.2: Remove a newspaper that he or she thinks is inappropriate. Removing a newspape implies removing all of the articles of which it is composed.
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina el newspaper2 correctamente ya que el rol de borrar lo tiene el admin
				"admin", "newspaper2", null
			}, {
				//Se elimina el newspaper1 incorrectamente porque es privado y solo se pueden eliminar los publicos
				"admin", "newspaper1", IllegalArgumentException.class
			}, {
				//Se elimina el newspaper3 incorrectamente porque solo lo puede eliminar el admin
				"user1", "newspaper3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int newspaperId, final Class<?> expected) {
		Newspaper newspaper;
		final Collection<Article> articles;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.findOne(newspaperId);
			this.newspaperService.delete(newspaper);

			this.newspaperService.flush();
			//Se comprueba que se haya eliminado del sistema todos los articulos de ese newspaper
			articles = newspaper.getArticles();
			for (final Article a : articles)
				Assert.isNull(this.articleService.findOne(a.getId()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
