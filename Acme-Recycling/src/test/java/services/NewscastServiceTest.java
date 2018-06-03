
package services;

import java.text.SimpleDateFormat;
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
import domain.Editor;
import domain.Newscast;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class NewscastServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	NewscastService	newscastService;

	@Autowired
	EditorService	editorService;

	@Autowired
	CommentService	commentService;

	@PersistenceContext
	EntityManager	entityManager;


	//Caso de uso 14: Create new
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateAndSave() {
		final Collection<String> picturesOk;
		final Collection<String> picturesBadUrls;

		picturesOk = this.addPicturesOk();
		picturesBadUrls = this.addPicturesBadUrls();

		final Object testingData[][] = {

			{
				//Crear correctamente una noticia
				"editor1", "title test 1", "2018/02/24 12:21", "content", picturesOk, null
			}, {
				// Crear incorrectamente una noticia por dejar el título en blanco
				"editor1", "", "2018/02/24 12:21", "content", picturesOk, javax.validation.ConstraintViolationException.class

			}, {
				//Crear incorrectamente una noticia por poner mal la colección de imágenes
				"editor1", "hola", "2018/02/24 12:21", "content", picturesBadUrls, javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Collection<String>) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	private void templateCreateAndSave(final int usernameId, final String title, final String publicationDate, final String content, final Collection<String> pictures, final Class<?> expected) {
		Class<?> caught;
		Editor userConnected;
		Newscast newscast;

		userConnected = this.editorService.findOne(usernameId);

		caught = null;
		try {
			super.authenticate(userConnected.getUserAccount().getUsername());
			newscast = this.newscastService.create();
			newscast.setTitle(title);
			newscast.setCreationDate((new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(publicationDate)));
			newscast.setContent(content);
			newscast.setPictures(pictures);
			newscast = this.newscastService.save(newscast);
			this.unauthenticate();
			this.newscastService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test

			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	private Collection<String> addPicturesOk() {
		Collection<String> picturesOk;
		picturesOk = new ArrayList<String>();
		picturesOk.add("http://www.picture1.com");
		picturesOk.add("http://www.picture2.com");
		return picturesOk;
	}
	private Collection<String> addPicturesBadUrls() {
		Collection<String> picturesBadUrls;
		picturesBadUrls = new ArrayList<String>();
		picturesBadUrls.add("http://www.picture1.com");
		picturesBadUrls.add("esto no es una url");
		return picturesBadUrls;
	}

	//Edit newscasts
	@SuppressWarnings("unchecked")
	@Test
	public void driveEditNewscast() {
		final Collection<String> picturesOk;
		final Collection<String> picturesBadUrls;

		picturesOk = this.addPicturesOk();
		picturesBadUrls = this.addPicturesBadUrls();

		final Object testingData[][] = {
			//Editar la new1 correctamente
			{
				"new1", "admin", "title", "2018/02/24 12:21", "content", picturesOk, null
			},
			//Editar la new1 incorrectamente con el título en blanco
			{
				"new1", "admin", "", "2018/02/24 12:21", "content", picturesOk, javax.validation.ConstraintViolationException.class
			},
			//Editar la new1 incorrectamente poniendo imágenes incorrectas
			{
				"new1", "admin", "title1", "2018/02/24 12:21", "content", picturesBadUrls, javax.validation.ConstraintViolationException.class

			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditNewscast((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Collection<String>) testingData[i][5], (Class<?>) testingData[i][6]);

	}
	public void templateEditNewscast(final String entity, final String username, final String title, final String publicationDate, final String content, final Collection<String> pictures, final Class<?> expected) {

		Class<?> caught;
		Newscast newscast;

		caught = null;

		try {
			super.authenticate(username);
			newscast = this.newscastService.findOne(super.getEntityId(entity));
			newscast.setTitle(title);
			newscast.setCreationDate((new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(publicationDate)));
			newscast.setContent(content);
			newscast.setPictures(pictures);
			this.newscastService.save(newscast);
			this.newscastService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
	//Borrar una noticia
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina correctamente la noticia 1
				"editor1", "new1", null

			}, {
				//Se elimina incorrectamente la noticia dos porque el editor1 no es su autor
				"editor2", "new2", java.lang.IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String username, final int newId, final Class<?> expected) {
		Newscast newscast;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newscast = this.newscastService.findOne(newId);
			this.newscastService.delete(newscast);

			this.newscastService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Listar y editar las noticias 
	@SuppressWarnings("unchecked")
	@Test
	public void driverListAndEdit() {
		final Collection<String> picturesOk;
		final Collection<String> picturesBadUrls;

		picturesOk = this.addPicturesOk();
		picturesBadUrls = this.addPicturesBadUrls();

		final Object testingData[][] = {

			{
				//correctamente la noticia 1 ya que es del editor 1
				"editor1", "new1", "title1", "content", picturesOk, true, null
			}, {
				//correctamente la noticia 2 ya que es del editor 1 también
				"editor1", "new2", "title", "content", picturesOk, true, null
			}, {
				//Se edita incorrectamente la noticia1 ya que el editor1 no es el autor de la noticia 3
				"editor1", "new3", "title", "content", picturesOk, true, IllegalArgumentException.class
			}, {
				//Se edita incorrectamente la noticia1 por poner el titulo en blanco
				"editor1", "new2", "", "content", picturesOk, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita incorrectamente la noticia1 por poner el content en blanco
				"editor1", "new2", "title", "", picturesOk, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita incorrectamente la noticia1 por poner malas urls 
				"editor1", "new2", "title", "content", picturesBadUrls, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita incorrectamente ya que el editor no puede ser nulo
				null, "new2", "title", "content", picturesBadUrls, true, java.lang.IllegalArgumentException.class
			}, {
				//Se edita correctamente ya que las imágenes son opcionales
				"editor1", "new2", "title", "content", null, true, null
			}, {
				//otro actor recycler intenta editar una noticia
				"recycler1", "new2", "title", "content", null, true, java.lang.NullPointerException.class
			}, {
				//otro actor intenta editar una noticia
				"buyer1", "new2", "title", "content", null, true, java.lang.NullPointerException.class
			},

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListAndEdit((String) testingData[i][0], (Integer) super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Collection<String>) testingData[i][4], (boolean) testingData[i][5],
				(Class<?>) testingData[i][6]);
	}
	private void templateListAndEdit(final String username, final Integer newscastId, final String title, final String content, final Collection<String> pictures, final boolean checkList, final Class<?> expected) {
		Newscast newscast;
		Collection<Newscast> mynewscast;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newscast = this.newscastService.findOne(newscastId);
			if (checkList) {
				mynewscast = this.editorService.findAllNewByEditor();
				//Se comprueba que el newspaper pasado por parametro se encuentra en la lista de newspapers no publicados aun
				Assert.isTrue(mynewscast.contains(newscast), "la noticia no es del editor logueado");
			}
			newscast.setTitle(title);
			newscast.setContent(content);
			newscast.setPictures(pictures);
			newscast = this.newscastService.save(newscast);
			this.newscastService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
