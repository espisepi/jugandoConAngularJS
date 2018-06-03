
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
import domain.Editor;
import domain.Item;
import domain.LabelProduct;
import domain.Material;
import domain.Newscast;
import domain.Recycler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdminServiceTest extends AbstractTest {

	// Supporting services ---------------------------------------------------
	@Autowired
	private AdminService		adminService;
	@Autowired
	private LabelProductService	labelProductService;
	@Autowired
	private EditorService		editorService;
	@Autowired
	private ItemService			itemService;
	@Autowired
	private MaterialService		materialService;
	@Autowired
	private NewscastService		newscastService;
	@Autowired
	private RecyclerService		recyclerService;

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
			admin.setAddress(postalAddress);
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

	//8.i	-Las noticias que contengan más comentarios.
	@Test
	public void findNewWithMoreComments() {
		final Object testingData[][] = {

			{
				//Se listan las noticias con más comentarios y aparece la noticia 1
				"admin", "new1", null
			}, {
				//Se listan las noticias con más comentarios y no aparece la noticia 2
				"admin", "new2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.findNewWithMoreComments((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void findNewWithMoreComments(final String username, final int newId, final Class<?> expected) {
		final Collection<Newscast> news;

		Newscast newcast;
		newcast = this.newscastService.findOne(newId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			news = this.adminService.findNewWithMoreComments();
			Assert.isTrue(news.contains(newcast));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	//8.ii	-Los redactores con mayor número de noticias redactadas.
	@Test
	public void findEditorsWithMoreNewsRedacted() {
		final Object testingData[][] = {

			{
				//Se listan los redactores con mayor número de noticias y aparece el editor1
				"admin", "editor1", null
			}, {
				//Se listan los redactores con mayor número de noticias y NO aparece el editor2
				"admin", "editor2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.findEditorsWithMoreNewsRedacted((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void findEditorsWithMoreNewsRedacted(final String username, final int editorId, final Class<?> expected) {
		final Collection<Editor> editors;

		Editor editor;
		editor = this.editorService.findOne(editorId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			editors = this.adminService.findEditorsWithMoreNewsRedacted();
			Assert.isTrue(editors.contains(editor));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//8.iii	-Las 5 categorías de productos con más productos asociados
	@Test
	public void findTop5LabelProducts() {
		final Object testingData[][] = {

			{
				//Se listan las categorias con más productos apareciendo Aluminio	
				"admin", "labelProduct1", null
			}, {
				//Se listan las categorias con más productos y NO aparece Pilas y Baterias
				"admin", "labelProduct4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.findTop5LabelProducts((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void findTop5LabelProducts(final String username, final int labelProdcutId, final Class<?> expected) {
		final Collection<LabelProduct> labelProducts;

		LabelProduct labelProduct;
		labelProduct = this.labelProductService.findOne(labelProdcutId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			labelProducts = this.adminService.findTop5LabelProducts();
			Assert.isTrue(labelProducts.contains(labelProduct));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//8.iv	-La media, el mínimo, el máximo y la desviación típica de cursos manejados por los compradores.
	@Test
	public void avgMinMaxAndStddevOfCoursesByBuyer() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 1., 0., 2.0, 0.6325, null
			},
			//admin negative
			{
				"user1", 1., 1., 2.25, 0.6325, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgMinMaxAndStddevOfCoursesByBuyer((String) testingData[i][0], (double) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (double) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	public void avgMinMaxAndStddevOfCoursesByBuyer(final String username, final double avg, final double min, final double max, final double stdev, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {

			super.authenticate(username);
			result = this.adminService.avgMinMaxAndStddevOfCoursesByBuyer();
			Assert.isTrue(result[0] == avg);
			Assert.isTrue(result[1] == min);
			Assert.isTrue(result[2] == max);
			Assert.isTrue(result[3] == stdev);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//8.v	-La media de la incidencias resueltas.
	@Test
	public void avgOfIncidencesResolved() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 0.57143, null
			},
			//admin negative
			{
				"user1", 0.6325, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgOfIncidencesResolved((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void avgOfIncidencesResolved(final String username, final double avg, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgOfIncidencesResolved();
			Assert.isTrue(result == avg);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//8.vi	-La media de usuarios que han reciclado al menos un item.
	@Test
	public void avgOfRecyclerWithAtLeastOneProduct() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 1.750, null
			},
			//admin negative
			{
				"user1", 1.3, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgOfRecyclerWithAtLeastOneProduct((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void avgOfRecyclerWithAtLeastOneProduct(final String username, final double avg, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgOfRecyclerWithAtLeastOneProduct();
			Assert.isTrue(result == avg);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//8.vii	-La media de usuarios baneados en el sistema.
	@Test
	public void avgOfUsersBanned() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 0.07692, null
			},
			//admin negative
			{
				"user1", 1.11538, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgOfUsersBanned((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void avgOfUsersBanned(final String username, final double avg, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.avgOfUsersBanned();
			Assert.isTrue(result == avg);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
	//8.viii	-La media, el mínimo, el máximo y la desviación típica de comentarios por noticias.
	@Test
	public void avgMinMaxAndStddevOfCommentsByNews() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 1.3333, 0., 4.0, 1.3744, null
			},
			//admin negative
			{
				"user1", 1.3333, 0., 4.0, 0.6325, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgMinMaxAndStddevOfCommentsByNews((String) testingData[i][0], (double) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (double) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	public void avgMinMaxAndStddevOfCommentsByNews(final String username, final double avg, final double min, final double max, final double stdev, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {

			super.authenticate(username);
			result = this.adminService.avgMinMaxAndStddevOfCommentsByNews();
			Assert.isTrue(result[0] == avg);
			Assert.isTrue(result[1] == min);
			Assert.isTrue(result[2] == max);
			Assert.isTrue(result[3] == stdev);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//8.ix	-Items que se han subido al sistema en el último mes.
	@Test
	public void findLatestItems() {
		final Object testingData[][] = {

			{
				//Se listan los items subido en el último mes apareciendo el item6
				"admin", "item6", null
			}, {
				//Se listan los items subido en el último mes NO apareciendo el item1
				"admin", "item1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.findLatestItems((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void findLatestItems(final String username, final int itemId, final Class<?> expected) {
		final Collection<Item> items;

		Item item;
		item = this.itemService.findOne(itemId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			items = this.adminService.findLatestItems();
			Assert.isTrue(items.contains(item));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//8.x	-Nombre del reciclador y título del Item que más valor tiene del sistema.
	@Test
	public void nameTitleRecyclerWithItemMostValue() {
		final Object testingData[][] = {

			{
				//Se lista el nombre del reciclador y su item de mas valor apareciendo el recycler1
				"admin", "recycler1", null
			}, {
				//Se lista el nombre del reciclador y su item de mas valor NO apareciendo el recycler3
				"admin", "recycler3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.nameTitleRecyclerWithItemMostValue((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void nameTitleRecyclerWithItemMostValue(final String username, final int recyclerId, final Class<?> expected) {
		String[] result;
		Recycler recycler;

		recycler = this.recyclerService.findOne(recyclerId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			result = this.adminService.nameTitleRecyclerWithItemMostValue();

			Assert.isTrue(result[0].equals(recycler.getName()));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	//8.xi	-La media, el mínimo, el máximo y la desviación típica peticiones por manager.
	@Test
	public void avgMinMaxAndStddevOfRequestByManager() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 1.000, 0., 2.0, 0.6325, null
			},
			//admin negative
			{
				"user1", 0.8000, 0., 4.0, 0.4000, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.avgMinMaxAndStddevOfRequestByManager((String) testingData[i][0], (double) testingData[i][1], (double) testingData[i][2], (double) testingData[i][3], (double) testingData[i][4], (Class<?>) testingData[i][5]);

	}

	public void avgMinMaxAndStddevOfRequestByManager(final String username, final double avg, final double min, final double max, final double stdev, final Class<?> expected) {

		Class<?> caught;
		Double[] result;

		caught = null;

		try {

			super.authenticate(username);
			result = this.adminService.avgMinMaxAndStddevOfRequestByManager();
			Assert.isTrue(result[0] == avg);
			Assert.isTrue(result[1] == min);
			Assert.isTrue(result[2] == max);
			Assert.isTrue(result[3] == stdev);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}

	//8.xiii	-Los 3 materiales más demandados.
	@Test
	public void findTop3Materials() {
		final Object testingData[][] = {

			{
				//Se listan los materiales mas demandados apareciendo el material1
				"admin", "material1", null
			}, {
				//Se listan los materiales mas demandados NO apareciendo el material 4
				"admin", "material4", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.findTop3Materials((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void findTop3Materials(final String username, final int materialId, final Class<?> expected) {
		final Collection<Material> materials;

		Material material;
		material = this.materialService.findOne(materialId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			materials = this.adminService.findTop3Materials();
			Assert.isTrue(materials.contains(material));
			this.entityManager.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//8.xiv	-El ratio de transportistas que han tenido al menos una solicitud frente a los que no han tenido ninguna.
	@Test
	public void ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest() {

		final Object testingData[][] = {
			//admin positive
			{
				"admin", 1.500, null
			},
			//admin negative
			{
				"user1", 0.6325, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest((String) testingData[i][0], (double) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	public void ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest(final String username, final double avg, final Class<?> expected) {

		Class<?> caught;
		Double result;

		caught = null;

		try {
			super.authenticate(username);
			result = this.adminService.ratioCarrierWithAtLeastOneRequestVersusCarrierWithNoOneRequest();
			Assert.isTrue(result == avg);
			this.adminService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();

	}
}
