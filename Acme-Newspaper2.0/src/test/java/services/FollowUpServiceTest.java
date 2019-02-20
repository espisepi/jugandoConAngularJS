
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

import utilities.AbstractTest;
import domain.Article;
import domain.FollowUp;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FollowUpServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	FollowUpService	followUpService;
	@Autowired
	UserService		userService;
	@Autowired
	ArticleService	articleService;

	@PersistenceContext
	EntityManager	entityManager;


	//Caso de uso 14: Create a followUp
	//@SuppressWarnings("unchecked")
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateAndSave() {
		final Collection<String> picturesOk;
		final Collection<String> picturesBadUrls;

		picturesOk = this.addPicturesOk();
		picturesBadUrls = this.addPicturesBadUrls();

		final Object testingData[][] = {

			{
				//El user1 crea una followUp para el articulo 1;
				//Articulo 1 cumple que está en modo final.
				//Su correspondiente newspaper(newspaper1) ha sido publicado.
				"user1", "article1", "title test 1", "2006/02/24", "summary test 1", "text folowUp test 1", picturesOk, null
			}, {
				//El user1 crea una followUp para el articulo 9;
				//Articulo 9 cumple que está en modo final pero no pertenece al user1.
				//Su correspondiente newspaper(newspaper6) ha sido publicado pero no es del user1.
				"user1", "article9", "title test 2", "2006/02/24", "summary test 2", "text folowUp test 2", picturesOk, IllegalArgumentException.class
			}, {
				//El user1 crea una followUp para el articulo 4;
				//Articulo 4 NO cumple que está en modo final aunque sea del user1.
				//Su correspondiente newspaper(newspaper4) ha sido publicado.
				"user1", "article4", "title test 3", "2006/02/24", "summary test 3", "text folowUp test 3", picturesOk, IllegalArgumentException.class
			}, {
				//El user1 crea una followUp para el articulo 2;
				//Articulo 2 cumple que está en modo final y que sea del user 1.
				//Su correspondiente newspaper(newspaper2) NO cumple que haya sido publicado.
				"user1", "article2", "title test 4", "2006/02/24", "summary test 4", "text folowUp test 4", picturesOk, IllegalArgumentException.class
			}, {
				//El user1 crea una followUp para el articulo 1;	FollowUp con el título vacío.
				//Articulo 1 cumple que está en modo final.
				//Su correspondiente newspaper(newspaper1) ha sido publicado.
				"user1", "article1", "", "2006/02/24", "summary test 5", "text folowUp test 5", picturesOk, javax.validation.ConstraintViolationException.class
			}, {
				//El user1 crea una followUp para el articulo 1;	FollowUp con el summary vacío.
				//Articulo 1 cumple que está en modo final.
				//Su correspondiente newspaper(newspaper1) ha sido publicado.
				"user1", "article1", "title test 7", "2006/02/24", "", "text folowUp test 7", picturesOk, javax.validation.ConstraintViolationException.class
			}, {
				//El user1 crea una followUp para el articulo 1;	FollowUp con el text vacío.
				//Articulo 1 cumple que está en modo final.
				//Su correspondiente newspaper(newspaper1) ha sido publicado.
				"user1", "article1", "title test 8", "2006/02/24", "summary test 8", "", picturesOk, javax.validation.ConstraintViolationException.class
			}, {
				//El user1 crea una followUp para el articulo 1;	FollowUp con el urls que no coinciden con el pattern de URL.
				//Articulo 1 cumple que está en modo final.
				//Su correspondiente newspaper(newspaper1) ha sido publicado.
				"user1", "article1", "title test 9", "2006/02/24", "summary test 9", "text folowUp test 9", picturesBadUrls, javax.validation.ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(Collection<String>) testingData[i][6], (Class<?>) testingData[i][7]);
	}

	private void templateCreateAndSave(final int usernameId, final int articleId, final String title, final String publicationDate, final String summary, final String text, final Collection<String> pictures, final Class<?> expected) {
		Class<?> caught;
		User userConnected;
		FollowUp followUp;
		Article article;

		userConnected = this.userService.findOne(usernameId);
		article = this.articleService.findOne(articleId);
		caught = null;
		try {
			super.authenticate(userConnected.getUserAccount().getUsername());
			followUp = this.followUpService.create(article);
			followUp.setTitle(title);
			followUp.setPublicationMoment((new SimpleDateFormat("yyyy/MM/dd").parse(publicationDate)));
			followUp.setSummary(summary);
			followUp.setText(text);
			followUp.setPictures(pictures);
			followUp = this.followUpService.save(followUp);
			this.unauthenticate();
			this.followUpService.flush();
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
}
