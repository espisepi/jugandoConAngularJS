
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
import domain.Advertisement;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdminService		adminService;

	@Autowired
	AdvertisementService		advertisementService;

	@Autowired
	private ArticleService		articleService;

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ChirpService		chirpService;

	@PersistenceContext
	EntityManager				entityManager;


	//Login 
	@Test
	public void driveLoginAdmin() {

		final Object testingData[][] = {
			//Admin is registered
			{
				"admin", null
			},
			//Admin isn't registered
			{
				"adminNoRegistrado", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateLoginAdmin((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	public void templateLoginAdmin(final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	//Test to edit all administrator attributes
	@Test
	public void driveEditNameAdministrator() {

		final Object testingData[][] = {
			//Try entering all the data of an admin with the null address and null telephone, positive case 
			{
				"admin", "admin", "adminTest", "surnameTest", null, null, "prueba@gmail.com", null
			},
			//Try entering a null phone number, this case positive
			{
				"admin", "admin", "adminTest", "surnameTest", "c/test", null, "prueba@gmail.com", null
			},
			//Try entering all the data of an admin, positive case
			{
				"admin", "admin", "adminTest", "surnameTest", "c/test", "+34657896576", "prueba@gmail.com", null
			},
			//Try entering a blank name, negative case
			{
				"admin", "admin", "", "surnameTest", null, null, "prueba@gmail.com", javax.validation.ConstraintViolationException.class
			},
			//Try entering a null name, negative case
			{
				"admin", "admin", null, "surnameTest", null, null, "prueba@gmail.com", javax.validation.ConstraintViolationException.class
			},
			//Try entering a blank surname, negative case
			{
				"admin", "admin", "adminTest", "", null, null, "prueba@gmail.com", javax.validation.ConstraintViolationException.class
			},
			//Try entering a null surname, negative case
			{
				"admin", "admin", "adminTest", null, null, null, "prueba@gmail.com", javax.validation.ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditAdministrator((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}

	public void templateEditAdministrator(final String entity, final String username, final String name, final String surname, final String postalAddress, final String phone, final String email, final Class<?> expected) {

		Class<?> caught;
		Admin admin;

		caught = null;
		admin = this.adminService.findOne(super.getEntityId(entity));

		try {
			super.authenticate(username);
			admin.setName(name);
			admin.setSurname(surname);
			admin.setPostalAddress(postalAddress);
			admin.setPhone(phone);
			admin.setEmail(email);
			this.adminService.save(admin);
			this.unauthenticate();
			this.adminService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.1. An admin removes an article that he or she thinks is inappropriate.
	@Test
	public void driveRemoveArticleAdmin() {

		final Object testingData[][] = {
			//Admin remove article, positive case
			{
				"admin", "article2", null
			},

			//Admin can't remove article 1 because his newspaper have subscriptions. Negative case
			{
				"admin", "article1", java.lang.IllegalArgumentException.class
			},
			//User can't remove an article. Negative case
			{
				"user1", "article2", java.lang.IllegalArgumentException.class
			},
			//Customer can't remove an article. Negative case
			{
				"customer1", "article2", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveArticleAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateRemoveArticleAdmin(final String username, final String articleEntity, final Class<?> expected) {

		Class<?> caught;
		Article article;

		caught = null;
		article = this.articleService.findOne(super.getEntityId(articleEntity));

		try {
			super.authenticate(username);
			this.articleService.delete(article);
			this.unauthenticate();
			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.2. An admin removes a newspaper that he or she thinks is inappropriate.
	@Test
	public void driveRemoveNewspaperAdmin() {

		final Object testingData[][] = {
			//Admin remove newspaper, positive case
			{
				"admin", "newspaper2", null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveNewspaperAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateRemoveNewspaperAdmin(final String username, final String newspaperEntity, final Class<?> expected) {

		Class<?> caught;
		Newspaper newspaper;

		caught = null;
		newspaper = this.newspaperService.findOne(super.getEntityId(newspaperEntity));

		try {
			super.authenticate(username);
			this.newspaperService.delete(newspaper);
			this.unauthenticate();
			this.newspaperService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.2. An admin removes a chirp that he or she thinks is inappropriate.
	@Test
	public void driveRemoveChirpAdmin() {

		final Object testingData[][] = {
			//Admin remove chirp, positive case
			{
				"admin", "chirp2", null
			},

			//User can't remove a chirp. Negative case
			{
				"user1", "chirp1", java.lang.IllegalArgumentException.class
			},
			//Customer can't remove a chirp. Negative case
			{
				"customer1", "chirp1", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRemoveChirpAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateRemoveChirpAdmin(final String username, final String chirpEntity, final Class<?> expected) {

		Class<?> caught;
		Chirp chirp;

		caught = null;
		chirp = this.chirpService.findOne(super.getEntityId(chirpEntity));

		try {
			super.authenticate(username);
			this.chirpService.delete(chirp);
			this.unauthenticate();
			this.chirpService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.2. An admin lists the articles that contains taboo words
	@Test
	public void driveListArticleWithTabooWordAdmin() {

		final Object testingData[][] = {
			//Admin lists articles, positive case
			{
				"admin", "article2", null
			},

			//User can't list articles. Negative case
			{
				"user1", "article2", java.lang.IllegalArgumentException.class
			},
			//Customer can't list an articles. Negative case
			{
				"customer1", "article2", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListArticleWithTabooWordAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateListArticleWithTabooWordAdmin(final String username, final String articleEntity, final Class<?> expected) {

		Class<?> caught;
		Article article;
		Collection<Article> articles;

		caught = null;
		article = this.articleService.findOne(super.getEntityId(articleEntity));

		try {
			super.authenticate(username);
			articles = this.articleService.articleWithTabooWord();
			Assert.isTrue(articles.contains(article));
			this.unauthenticate();
			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.3. An admin lists the newspapers that contains taboo words
	@Test
	public void driveListNewspaperWithTabooWordAdmin() {

		final Object testingData[][] = {
			//Admin list newspapers, positive case
			{
				"admin", "newspaper2", null
			},

			//User can't list newspapers. Negative case
			{
				"user1", "newspaper1", java.lang.IllegalArgumentException.class
			},
			//Customer can't list newspapers. Negative case
			{
				"customer1", "newspaper1", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListNewspaperWithTabooWordAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateListNewspaperWithTabooWordAdmin(final String username, final String newspaperEntity, final Class<?> expected) {

		Class<?> caught;
		Newspaper newspaper;
		Collection<Newspaper> newspapers;

		caught = null;
		newspaper = this.newspaperService.findOne(super.getEntityId(newspaperEntity));

		try {
			super.authenticate(username);
			newspapers = this.newspaperService.NewspaperWithTabooWord();
			Assert.isTrue(newspapers.contains(newspaper));
			this.unauthenticate();
			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.3. An admin lists the chirps that contains taboo words
	@Test
	public void driveListChirpsWithTabooWordAdmin() {

		final Object testingData[][] = {
			//Admin list newspapers, positive case
			{
				"admin", "chirp1", null
			},

			//User can't list newspapers. Negative case
			{
				"user1", "chirp1", java.lang.IllegalArgumentException.class
			},
			//Customer can't list newspapers. Negative case
			{
				"customer1", "chirp1", java.lang.IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListChirpsWithTabooWordAdmin((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateListChirpsWithTabooWordAdmin(final String username, final String newspaperEntity, final Class<?> expected) {

		Class<?> caught;
		Chirp chirp;
		Collection<Chirp> chirps;

		caught = null;
		chirp = this.chirpService.findOne(super.getEntityId(newspaperEntity));

		try {
			super.authenticate(username);
			chirps = this.chirpService.ChirpWithTabooWord();
			Assert.isTrue(chirps.contains(chirp));
			this.unauthenticate();
			this.articleService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.3.1. The average and the standard deviation of newspapers created per user.  
	@Test
	public void drivetheAvgAndStddevOfNewspapersForUserAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 2, 2.25, 0.433, null
			},
			//User registers, negativo case
			{
				"user1", 2, 2.25, 0.433, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheAvgAndStddevOfNewspapersForUserAdministrator((String) testingData[i][0], (int) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	public void templatetheAvgAndStddevOfNewspapersForUserAdministrator(final String username, final int num, final double num1, final double num2, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theAvgAndStddevOfNewspapersForUser();
			Assert.isTrue(result.length == num);
			Assert.isTrue(result[0] == num1);
			Assert.isTrue(result[1] == num2);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//Use case 7.3.2. The average and the standard deviation of articles written by writer
	@Test
	public void drivetheAvgAndStddevOfArticlesPerWriterAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 2, 3.0, 0.7071, null
			},
			//user registers, negative case
			{
				"user1", 2, 3.0, 0.7071, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheAvgAndStddevOfArticlesPerWriterAdministrator((String) testingData[i][0], (int) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	public void templatetheAvgAndStddevOfArticlesPerWriterAdministrator(final String username, final int num, final double num1, final double num2, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theAvgAndStddevOfArticlesPerWriter();
			Assert.isTrue(result.length == num);
			Assert.isTrue(result[0] == num1);
			Assert.isTrue(result[1] == num2);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//Use case 7.3.3. The average and the standard deviation of articles per newspaper.
	@Test
	public void drivetheAvgAndStddevOfArticlePerNewspaperAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 2, 1.3333, 0.4714, null
			},
			//user registers, negative case
			{
				"user1", 2, 1.3333, 0.4714, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheAvgAndStddevOfArticlePerNewspaperAdministrator((String) testingData[i][0], (int) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	public void templatetheAvgAndStddevOfArticlePerNewspaperAdministrator(final String username, final int num, final double num1, final double num2, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theAvgAndStddevOfArticlePerNewspaper();
			Assert.isTrue(result.length == num);
			Assert.isTrue(result[0] == num1);
			Assert.isTrue(result[1] == num2);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.3.4. The newspapers that have at 10% more articles than the average. 
	@Test
	public void drivenewspaperHaveLeast10MorePercentFewerArtclesThanAveragedAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 3, null
			},
			//user registers, negative case
			{
				"user1", 3, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatenewspaperHaveLeast10MorePercentFewerArtclesThanAverageAdministrator((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templatenewspaperHaveLeast10MorePercentFewerArtclesThanAverageAdministrator(final String username, final int num, final Class<?> expected) {

		Class<?> caught;
		Collection<Newspaper> result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.newspaperHaveLeast10MorePercentFewerArtclesThanAverage();
			Assert.isTrue(result.size() == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.3.5. the newspaper that have at least 10% fewer articles than the average. 
	@Test
	public void drivenewspaperHaveLeast10LeastPercentFewerArtclesThanAverageAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 6, null
			},
			//user registers, negative case
			{
				"user1", 6, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatenewspaperHaveLeast10LeastPercentFewerArtclesThanAverageAdministrator((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templatenewspaperHaveLeast10LeastPercentFewerArtclesThanAverageAdministrator(final String username, final int num, final Class<?> expected) {

		Class<?> caught;
		Collection<Newspaper> result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.newspaperHaveLeast10LeastPercentFewerArtclesThanAverage();
			Assert.isTrue(result.size() == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.3.6. the newspaper that have at least 10% fewer articles than the average.
	@Test
	public void drivetheRatioOfUsersWritingNewspaperAdministrator() {

		final Object testingData[][] = {
			//admin registes, positive case
			{
				"admin", 1.0, null
			},
			//user registers, negative case
			{

				"user1", 1.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheRatioOfUsersWritingNewspaperAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templatetheRatioOfUsersWritingNewspaperAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theRatioOfUsersWritingNewspaper();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 7.3.7. the ratio of users who have ever created a newspaper. 
	@Test
	public void drivetheRatioOfUsersWritingArticleAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 1.0, null
			},
			//user registers, negative case
			{
				"user1", 1.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheRatioOfUsersWritingArticleAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templatetheRatioOfUsersWritingArticleAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theRatioOfUsersWritingArticle();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.6.1. The average number of follow.ups per article.
	@Test
	public void driveAvgFollowupsPerArticleAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 0.4167, null
			},
			//user registers, negative case
			{
				"user1", 0.4167, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgFollowupsPerArticleAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateAvgFollowupsPerArticleAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgFollowupsPerArticle();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.6.2. The average number of follow-ups per article up to two weeks after corresponding newspaper's been published.
	@Test
	public void driveAvgNumberOfFollowUpsPerArticleAfterOneWeekAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 0.5, null
			},
			//user registers, negative case
			{
				"user1", 0.5, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgNumberOfFollowUpsPerArticleAfterOneWeekAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateAvgNumberOfFollowUpsPerArticleAfterOneWeekAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgNumberOfFollowUpsPerArticleAfterOneWeek();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.6.3. The average number of follow-ups per article up to two weeks after the corresponding newspaper's been published.
	@Test
	public void driveAvgNumberOfFollowUpsPerArticleAfterTwoWeekAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 0.5, null
			},
			//user registers, negative case
			{
				"user1", 0.5, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgNumberOfFollowUpsPerArticleAfterTwoWeekAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateAvgNumberOfFollowUpsPerArticleAfterTwoWeekAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgNumberOfFollowUpsPerArticleAfterTwoWeek();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.6.4. The average and the standard deviation of the number of chirps per user. 
	@Test
	public void drivetheAvgAndStddevOfNumberOfChirpPerUserAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 2, 1.75, 0.433, null
			},
			//user registers, negative case
			{
				"user1", 2, 1.75, 0.433, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgAndStddevOfNumberOfChirpPerUserAdministrator((String) testingData[i][0], (int) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	public void templateAvgAndStddevOfNumberOfChirpPerUserAdministrator(final String username, final int num, final double num1, final double num2, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgAndStddevOfNumberOfChirpPerUser();
			Assert.isTrue(result.length == num);
			Assert.isTrue(result[0] == num1);
			Assert.isTrue(result[1] == num2);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 17.6.5. The ratio of users who have posted above 75% the averaage number of chirps per user .
	@Test
	public void driveratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSerAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 0, null
			},
			//user registers, negative case
			{
				"user1", 0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSerAdministrator((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSerAdministrator(final String username, final int num, final Class<?> expected) {

		Class<?> caught;
		Collection<User> result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.ratioOfUserWhoHavePostedAbove75PercentTheAvgNumberOfChirpsPerUSer();
			Assert.isTrue(result.size() == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 24.1.1. The ratio of public versus private newspapers
	@Test
	public void driveRatioOfNewspaperPublicPerNespaperProvateAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 1.25, null
			},
			//user registers, negative case
			{
				"user1", 1.25, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRatioOfNewspaperPublicPerNespaperProvateAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateRatioOfNewspaperPublicPerNespaperProvateAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.ratioOfNewspaperPublicPerNespaperProvate();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 24.1.2. The average number of articles per private newspapers
	@Test
	public void driveAvgArticlePerNewspapersPrivateAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 1.25, null
			},
			//user registers, negative case
			{
				"user1", 1.25, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgArticlePerNewspapersPrivateAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateAvgArticlePerNewspapersPrivateAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgArticlePerNewspapersPrivate();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 24.1.3. The average number of articles per public newspapers
	@Test
	public void driveAvgArticlesPerNewspapersPublicAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 1.4, null
			},
			//user registers, negative case
			{
				"user1", 1.4, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAvgArticlesPerNewspapersPublicAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateAvgArticlesPerNewspapersPublicAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgArticlesPerNewspapersPublic();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 24.1.4. The ratio of subscribers per private newspaper versus the total number of customers
	@Test
	public void driveRatioOfSubscribersWhenNewspaperPrivatePerNumberCustomerAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 2.0, null
			},
			//user registers, negative case
			{
				"user1", 2.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateRatioOfSubscribersWhenNewspaperPrivatePerNumberCustomerAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templateRatioOfSubscribersWhenNewspaperPrivatePerNumberCustomerAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.ratioOfSubscribersWhenNewspaperPrivatePerNumberCustomer();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//Use case 24.1.5. The average ratio of private versus public newspapers per publisher
	@Test
	public void drivetheAverageRatioOfPrivateVersusPublicNewspaperPerPublishedAdministrator() {

		final Object testingData[][] = {
			//admin registers, positive case
			{
				"admin", 0.8, null
			},
			//user registers, negative case
			{
				"user1", 0.8, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templatetheAverageRatioOfPrivateVersusPublicNewspaperPerPublishedAdministrator((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void templatetheAverageRatioOfPrivateVersusPublicNewspaperPerPublishedAdministrator(final String username, final double num, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.theAverageRatioOfPrivateVersusPublicNewspaperPerPublished();
			Assert.isTrue(result == num);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//Caso de uso 5.1: List the advertisements that contain taboo words in its title.
	@Test
	public void driverListAdvertisementWithTabooWord() {
		final Object testingData[][] = {

			{
				//Se listan los advertisement que tienen palabras taboo y se comprueba que el anuncio 5 contiene palabra tabu.
				"admin", "advertisement5", null
			}, {
				//Se listan los advertisement que tienen palabras taboo y el anuncio 2 no contiene palabra tabu
				"admin", "advertisement2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListAdvertisementWithTabooWord((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateListAdvertisementWithTabooWord(final String username, final String advertisement, final Class<?> expected) {
		Collection<Advertisement> advertisements;
		Advertisement advertisementToCompare;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			advertisements = this.advertisementService.advertisementWithTabooWord();
			advertisementToCompare = this.advertisementService.findOne(this.getEntityId(advertisement));
			Assert.isTrue(advertisements.contains(advertisementToCompare));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 5.2. Remove an advertisement that he or she thinks is inappropriate.
	@Test
	public void driverDeleteAdvertisement() {
		final Object testingData[][] = {

			{
				//Se borra un advertisement que esta en un periodico por parte del administrador
				"admin", "advertisement1", null
			}, {
				//Se borrar un advertisement por parte de un user
				"user1", "advertisement2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAdvertisement((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateDeleteAdvertisement(final String username, final String advertisementId, final Class<?> expected) {
		Class<?> caught;
		Advertisement advertisementDelete;
		caught = null;
		try {
			super.authenticate(username);
			advertisementDelete = this.advertisementService.findOne(this.getEntityId(advertisementId));
			this.advertisementService.delete(advertisementDelete);
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 5.3.1: The ratio of newspapers that have at least one advertisement versus the newspapers that havent any.
	@Test
	public void driverTheRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement() {
		final Object testingData[][] = {

			{
				//admin registers, positive case
				"admin", 0.5, null
			}, {
				//user registers, negative case
				"user1", 0.5, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateTheRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateTheRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement(final String username, final double result, final Class<?> expected) {
		Class<?> caught;
		Double resultToCompareWithResult;
		caught = null;
		try {
			super.authenticate(username);
			resultToCompareWithResult = this.adminService.theRatioOfNewspapersAtLeastOneAdvertisementVersusZeroAdvertisement();
			Assert.isTrue(result == resultToCompareWithResult);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 5.3.2: The ratio of advertisements that have taboo words
	@Test
	public void driverTheRatioOfAdvertisementsThatHaveTabooWords() {
		final Object testingData[][] = {

			{
				//admin registers, positive case
				"admin", 0.375, null
			}, {
				//user registers, negative case
				"user1", 0.375, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateTheRatioOfAdvertisementsThatHaveTabooWords((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateTheRatioOfAdvertisementsThatHaveTabooWords(final String username, final double result, final Class<?> expected) {
		Class<?> caught;
		Double resultToCompareWithResult;
		caught = null;
		try {
			super.authenticate(username);
			resultToCompareWithResult = this.adminService.theRatioOfAdvertisementsThatHaveTabooWords();
			Assert.isTrue(result == resultToCompareWithResult);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 11.1.1: The average number of newspapers per volume.
	@Test
	public void driverTheAverageNumberOfNewspaperPerVolume() {
		final Object testingData[][] = {

			{
				//admin registers, positive case
				"admin", 1.6667, null
			}, {
				//User registers, negative case
				"user1", 1.6667, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateTheAverageNumberOfNewspaperPerVolume((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateTheAverageNumberOfNewspaperPerVolume(final String username, final double result, final Class<?> expected) {
		Class<?> caught;
		Double resultToCompareWithResult;
		caught = null;
		try {
			super.authenticate(username);
			resultToCompareWithResult = this.adminService.theAverageNumberOfNewspaperPerVolume();
			Assert.isTrue(result == resultToCompareWithResult);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 11.1.2: The ratio of subscriptions to volumes versus subscriptions to newspapers.
	@Test
	public void driverTheRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers() {
		final Object testingData[][] = {

			{
				///admin registers, positive case
				"admin", 0.875, null
			}, {
				//user registers, negative case
				"user1", 0.875, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateTheRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	private void templateTheRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers(final String username, final double result, final Class<?> expected) {
		Class<?> caught;
		Double resultToCompareWithResult;
		caught = null;
		try {
			super.authenticate(username);
			resultToCompareWithResult = this.adminService.theRatioOfSubcrptionsToVolumesVersusSubcriptionsToNewspapers();
			Assert.isTrue(result == resultToCompareWithResult);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
