
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
public class ArticleServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	ArticleService		articleService;

	@PersistenceContext
	EntityManager		entityManager;


	//Caso de uso 6.3: Write an article and attach it to any newspaper that has not been published, yet. Note that articles may be saved in draft mode, which allows to modify them later, or final model, which freezes them forever. (parte 1)
	@SuppressWarnings("unchecked")
	@Test
	public void driverListAndEdit() {
		final Collection<String> picturesOk;
		final Collection<String> picturesBadUrls;

		picturesOk = this.addPicturesOk();
		picturesBadUrls = this.addPicturesBadUrls();

		final Object testingData[][] = {

			{
				//Se edita el article3 para el newspaper3 (publico y no publicado) correctamente
				"user1", "article3", "newspaper3", "title test", null, "summary test", "body test", picturesOk, true, true, null
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) incorrectamente porque el user2 no es el creador del article4
				"user2", "article3", "newspaper3", "title test", null, "summary test", "body test", picturesOk, true, true, IllegalArgumentException.class
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) incorrectamente porque tiene el title en blank
				"user1", "article3", "newspaper3", "", null, "summary test", "body test", picturesOk, true, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) incorrectamente porque tiene el summary en blank
				"user1", "article3", "newspaper3", "title test", null, "", "body test", picturesOk, true, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) incorrectamente porque tiene el body en blank
				"user1", "article3", "newspaper3", "title test", null, "summary test", "", picturesOk, true, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) correctamente con la lista de pictures vacia
				"user1", "article3", "newspaper3", "title test", null, "summary test", "body test", new ArrayList<String>(), true, true, null
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) incorrectamente con la lista de pictures con mala urls
				"user1", "article3", "newspaper3", "title test", null, "summary test", "body test", picturesBadUrls, true, true, javax.validation.ConstraintViolationException.class
			}, {
				//Se edita el article3 para el newspaper3 (publico y no publicado) correctamente poniendo el article en modo final
				"user1", "article3", "newspaper3", "title test", null, "summary test", "body test", picturesOk, false, true, null
			}, {
				//Se edita el article4 para el newspaper1 (publico y no publicado) incorrectamente porque el newspaper1 ya esta publicado (comprobamos el list)
				"user1", "article1", "newspaper1", "title test", null, "summary test", "body test", picturesOk, true, true, IllegalArgumentException.class
			}, {
				//Se edita el article1 para el newspaper1 (privado y publicado) incorrectamente porque ya esta publicado el newspaper
				"user1", "article1", "newspaper1", "title test", null, "summary test", "body test", picturesOk, true, false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListAndEdit((String) testingData[i][0], (Integer) super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(String) testingData[i][6], (Collection<String>) testingData[i][7], (boolean) testingData[i][8], (boolean) testingData[i][9], (Class<?>) testingData[i][10]);
	}
	private void templateListAndEdit(final String username, final Integer articleId, final String newspaperBean, final String title, final String publishedMoment, final String summary, final String body, final Collection<String> pictures,
		final boolean draftMode, final boolean checkList, final Class<?> expected) {
		Article article;
		Newspaper newspaper;
		Collection<Newspaper> newspapersNotPublished;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			article = this.articleService.findOne(articleId);
			newspaper = this.newspaperService.findOne(super.getEntityId(newspaperBean));
			article.setNewspaper(newspaper);
			if (checkList) {
				newspapersNotPublished = this.newspaperService.findNewspaperNotPublished();
				//Se comprueba que el newspaper pasado por parametro se encuentra en la lista de newspapers no publicados aun
				Assert.isTrue(newspapersNotPublished.contains(newspaper), "el newspaper no se encuentra entre los no publicados");
			}
			article.setTitle(title);
			article.setSummary(summary);
			article.setBody(body);
			article.setPictures(pictures);
			article.setDraftMode(draftMode);
			article = this.articleService.save(article);
			this.articleService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 6.3: Write an article and attach it to any newspaper that has not been published, yet. Note that articles may be saved in draft mode, which allows to modify them later, or final model, which freezes them forever. (parte 2)
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateAndSave() {
		final Collection<String> picturesOk;

		picturesOk = this.addPicturesOk();

		final Object testingData[][] = {

			{
				//Se crea un article para el newspaper3 (publico y no publicado) correctamente
				"user1", "newspaper3", "title test", null, "summary test", "body test", picturesOk, true, null
			}, {
				//Se crea un article para el newspaper5 (publico y publicado) incorrectamente porque ya esta publicado
				"user4", "newspaper5", "title test", null, "summary test", "body test", picturesOk, true, IllegalArgumentException.class
			}, {

				//Se crea un article para el newspaper9 (privado y no publicado) correctamente
				"user4", "newspaper9", "title test", null, "summary test", "body test", picturesOk, true, null

			}, {
				//Se crea un article para el newspaper1 (privado y publicado) incorrectamente porque solo lo puede crear el user
				"customer1", "newspaper1", "title test", null, "summary test", "body test", picturesOk, true, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Collection<String>) testingData[i][6],
				(boolean) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	private void templateCreateAndSave(final String username, final String newspaperBean, final String title, final String publishedMoment, final String summary, final String body, final Collection<String> pictures, final boolean draftMode,
		final Class<?> expected) {
		Article article;
		Newspaper newspaper;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			newspaper = this.newspaperService.findOne(super.getEntityId(newspaperBean));
			article = this.articleService.create(newspaper.getId());

			article.setTitle(title);
			article.setSummary(summary);
			article.setBody(body);
			article.setPictures(pictures);
			article.setDraftMode(draftMode);
			article = this.articleService.save(article);
			this.articleService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 7.1: Remove an article that he or she thinks is inappropriate.
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina el article2 correctamente
				"admin", "article2", null
			}, {
				//Se elimina el article3 correctamente
				"admin", "article3", null
			}, {
				//Se elimina el article4 incorrectamente porque solo lo puede eliminar el admin
				"user1", "article4", IllegalArgumentException.class
			}, {
				//Se elimina el article4 correctamente
				"admin", "article4", null
			}, {
				//Se elimina el article1 incorrectamente porque su newspaper ya esta publicado
				"admin", "article1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int articleId, final Class<?> expected) {
		Article article;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			article = this.articleService.findOne(articleId);
			this.articleService.delete(article);

			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Other methods --------------------------------------------------

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

}
