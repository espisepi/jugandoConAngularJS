
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Article;
import domain.Newspaper;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	private UserService			userService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private ArticleService		articleService;

	@PersistenceContext
	EntityManager				entityManager;


	//Test caso de uso extra: Login usuario
	@Test
	public void driverLoginUser() {

		final Object testingData[][] = {
			{
				//login usuario registrado
				"user1", null

			}, {
				//login user no registrado
				"juanya", IllegalArgumentException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateLogin((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}
	private void templateLogin(final String username, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso 4.1:Register to the system as a user
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar user correctamente(poniendo teléfono!=null)
				"usertest1", "passwordtest1", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", null
			}, {
				//Registrar user incorrectamente con name en blanco
				"usertest2", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar user con surname en blanco
				"usertest3", "passwordtest3", "miguel", "", "calle Huertas nº 3", "662657322", "Email@email.com", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar user incorrectamente con un email no válido
				"usertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar user incorrectamente ya que está registrado
				"usertest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@gmail.com", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar user correctamente con número de teléfono vacío
				"usertest9", "passwordtest9", "name1", "surname1", "calle Huertas nº 3", "", "maria@gmail.com", null

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		User user;
		UserAccount userAccount;

		caught = null;
		try {
			user = this.userService.create();
			user.setName(name);
			user.setSurname(surname);
			user.setPostalAddress(postalAdress);
			user.setPhone(phone);
			user.setEmail(email);
			userAccount = user.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			user.setUserAccount(userAccount);
			user = this.userService.save(user);
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso extra: Edit personal data of user
	@Test
	public void driverEditUser() {

		final Object testingData[][] = {
			{
				//Edito mi nombre
				"user1", "user1 name edited", "surname user 1", "postal Adress user 1", "617557203", "user1@acmenewspaper.com", null
			}, {
				//Edito mis apellidos
				"user1", "user 1", "surname user 1 edited", "postal Adress user 1", "617557203", "user1@acmenewspaper.com", null
			}, {
				//Edito mi dirección
				"user1", "user 1", "surname user 1", "postal Adress user 1 edited", "617557203", "user1@acmenewspaper.com", null
			}, {
				//Edito mi email
				"user1", "user 1", "surname user 1", "postal Adress user 1", "617557203", "user1EDITED@acmenewspaper.com", null
			}, {
				//Edito mi nombre por uno en blanco
				"user1", "", "surname user 1", "postal Adress user 1", "617557203", "user1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito mis apellidos por uno en blanco
				"user1", "user 1", "", "postal Adress user 1", "617557203", "user1@acmenewspaper.com", ConstraintViolationException.class
			}, {
				//Edito mi email por uno que no cumple el formato
				"user1", "user 1", "surname user 1", "postal Adress user 1", "617557203", "user1", ConstraintViolationException.class
			}, {
				//Edito mi email por uno en blanco
				"user1", "", "surname user 1", "postal Adress user 1", "617557203", "", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditUser(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateEditUser(final int userNameId, final String name, final String surname, final String postalAdress, final String phone, final String email, final Class<?> expected) {
		Class<?> caught;
		User user;
		user = this.userService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			user.setName(name);
			user.setSurname(surname);
			user.setPostalAddress(postalAdress);
			user.setPhone(phone);
			user.setEmail(email);
			user = this.userService.save(user);
			this.unauthenticate();
			this.userService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}

	//Test caso de uso 4.2: List the newspapers that are published and browse their articles.
	@Test
	public void driverListNewspapers() {
		final Object testingData[][] = {
			{
				//El user 1 lista los newspapers publicados y aparece el newspaper5 porque ha sido publicado y está público.
				//El newspaper5 contiene el article 8
				"user1", "newspaper5", "article8", null
			}, {
				//El user 1 lista los newspapers publicados y NO le aparece el newspaper2 porque NO ha sido publicado.
				//El newspaper2 contiene el article 2
				"user1", "newspaper2", "article2", IllegalArgumentException.class
			}, {
				//El user 1 lista los newspapers publicados y NO le aparece el newspaper1 porque NO ha es público aunque SÍ ha sido públicado.
				//El newspaper1 contiene el article 1
				"user1", "newspaper1", "article1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNewspapers(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateListNewspapers(final int usernameId, final int newspaperId, final int articleId, final Class<?> expected) {
		Class<?> caught;
		User user;
		Collection<Newspaper> newspapers;
		final Collection<Article> articles;
		final Newspaper newspaper;
		Article article;

		user = this.userService.findOne(usernameId);
		newspaper = this.newspaperService.findOne(newspaperId);
		article = this.articleService.findOne(articleId);
		articles = this.articleService.findArticlesByNewspaperId(newspaperId);
		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			newspapers = this.newspaperService.findNewspapersPublishedAndOpen();
			Assert.isTrue(newspapers.contains(newspaper));
			Assert.isTrue(articles.contains(article));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Test caso de uso 4.3: List the users of the system and display their profiles, 
	//which must include their personal data an the list of articles that they 
	//have written as long as they are published in a newspaper.
	@Test
	public void driverListUsers() {
		final Object testingData[][] = {
			{
				//El user 1 lista todos los usuarios; 
				//lista los articulos del user 2, conteniendo así el article 9 entre ellos.
				"user1", "user2", "article9", null
			}, {
				//El user 1 lista todos los usuarios; 
				//lista los articulos del user 2, NO conteniendo así el article 5(es del user 3) entre ellos.
				"user1", "user2", "article5", IllegalArgumentException.class
			}, {
				//El user 1 lista todos los usuarios; 
				//lista los articulos del user 2, NO conteniendo así el article 4(aún no ha sido publicado).
				"user1", "user2", "article4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListUsers(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateListUsers(final int usernameIdLogin, final int usernameIdListArticles, final int articleId, final Class<?> expected) {
		Class<?> caught;
		User userLogin;
		final Collection<Article> articles;
		Article article;

		userLogin = this.userService.findOne(usernameIdLogin);

		caught = null;
		try {
			super.authenticate(userLogin.getUserAccount().getUsername());
			//Articles publicados por el usuario 2
			articles = this.articleService.findArticlesPublishedByUserId(usernameIdListArticles);
			article = this.articleService.findOne(articleId);
			Assert.isTrue(articles.contains(article));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}

	//Test caso de uso 4.4: Search for a published article using a single key word that must appear somewhere
	//in its title, summary, or body.
	@Test
	public void driverSearchPublishedArticle() {
		final Object testingData[][] = {
			{
				//El user 1 busca por la palabra "especial" mostrándole el article 6 
				//que contiene dicha palabra en el summary y ha sido publicado.
				"user1", "especial", "article6", null
			}, {
				//El user 1 busca por la palabra "impresionante" mostrándole el article 8 
				//que contienen dicha palabra en el body y ha sido publicado.
				"user1", "impresionante", "article8", null
			}, {
				//El user 1 busca por la palabra article 4 no mostrándole ningún article
				//ya que el article 4 NO ha sido publicado.
				"user1", "article 4", "article4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSearchArticles(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateSearchArticles(final int usernameId, final String keyWord, final int articleId, final Class<?> expected) {
		Class<?> caught;
		User userLogin;
		Collection<Article> articles;
		Article articleExpected;
		caught = null;
		userLogin = this.userService.findOne(usernameId);

		try {
			super.authenticate(userLogin.getUserAccount().getUsername());
			articleExpected = this.articleService.findOne(articleId);
			articles = this.articleService.findArticleByKeyword(keyWord);
			Assert.isTrue(articles.contains(articleExpected));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}

	//Test caso de uso 4.5: Search for a published newspaper using a single keyword that must appear somewhere
	//in its title or its description.
	@Test
	public void driverSearchPublishedNewspaper() {
		final Object testingData[][] = {
			{
				//El user 1 busca por la palabra "terror" mostrándole el newspaper7 
				//que contiene dicha palabra en el título y ha sido publicado.
				"user1", "terror", "newspaper7", null
			}, {
				//El user 1 busca por la palabra "amanecer" mostrándole el newspaper6
				//que contienen dicha palabra en la descripción y ha sido publicado.
				"user1", "amanecer", "newspaper6", null
			}, {
				//El user 1 busca por la palabra "accidente" NO mostrándole el newspaper3
				//pese a que lo contiene en el título pero NO ha sido publicado
				"user1", "accidente", "newspaper3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSearchNewspapers(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], super.getEntityId((String) testingData[i][2]), (Class<?>) testingData[i][3]);
	}

	private void templateSearchNewspapers(final int usernameId, final String keyWord, final int newspaperId, final Class<?> expected) {
		Class<?> caught;
		User userLogin;
		Collection<Newspaper> newspapers;
		Newspaper newspaperExpected;
		caught = null;
		userLogin = this.userService.findOne(usernameId);

		try {
			super.authenticate(userLogin.getUserAccount().getUsername());
			newspaperExpected = this.newspaperService.findOne(newspaperId);
			newspapers = this.newspaperService.findNewspapersByKeyword(keyWord);
			Assert.isTrue(newspapers.contains(newspaperExpected));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
	}

	//Use Case 16.2 Follow or unfollow another user.
	//Follow
	@Test
	public void driverFollowUser() {
		final Object testingData[][] = {
			{
				//El user 4 va a seguir de manera correcta al user 1 que aun no lo sige.
				"user4", "user1", null
			}, {
				//El user 2 va a seguir al user 3 al cual sige ya y debe dar error
				"user2", "user3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFollowUser(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateFollowUser(final int usernameId, final int username1Id, final Class<?> expected) {
		Class<?> caught;
		User user;
		User userToFollow;

		user = this.userService.findOne(usernameId);
		userToFollow = this.userService.findOne(username1Id);
		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			this.userService.followUser(userToFollow);
			Assert.isTrue(user.getFollowed().contains(userToFollow));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Use Case 16.2 Follow or unfollow another user.
	//Unfollow
	@Test
	public void driverUnFollowUser() {
		final Object testingData[][] = {
			{
				//El user 1 va a dejar de seguir al user 3 que es su seguidor
				"user1", "user3", null
			}, {
				//El user 4 va a dejar de seguir al user3 que no lo seguia anteriormente
				"user4", "user3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateUnFollowUser(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateUnFollowUser(final int usernameId, final int username1Id, final Class<?> expected) {
		Class<?> caught;
		User user;
		User userToUnFollow;

		user = this.userService.findOne(usernameId);
		userToUnFollow = this.userService.findOne(username1Id);
		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			this.userService.unFollowUser(userToUnFollow);
			Assert.isTrue(!user.getFollowed().contains(userToUnFollow));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Use Case 16.3List the users who he or she follows.
	@Test
	public void driverListFollowed() {
		final Object testingData[][] = {
			{
				//El user 4 no sigue a nadie.
				"user4", 0, null
			}, {
				//El user 4 no sige a nadie por tanto debe dar error
				"user4", 4, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListFollowed(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateListFollowed(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		User user;
		Collection<User> followed;

		user = this.userService.findOne(usernameId);
		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			followed = user.getFollowed();
			Assert.isTrue(followed.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Use Case 16.4 List the users who follow him or her..lo siguen
	@Test
	public void driverListFollowers() {
		final Object testingData[][] = {
			{
				//El user 1 es seguido por el user 3 usuarios.
				"user4", "user1", null
			}, {
				//El user 4 no es seguido por el user 2
				"user1", "user4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListFollowers(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateListFollowers(final int usernameId, final int username1, final Class<?> expected) {
		Class<?> caught;
		User user;
		User user1;
		Collection<User> followers;

		user = this.userService.findOne(usernameId);
		user1 = this.userService.findOne(username1);
		caught = null;
		try {
			super.authenticate(user.getUserAccount().getUsername());
			followers = user.getFollowers();
			Assert.isTrue(followers.contains(user1));
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

}
