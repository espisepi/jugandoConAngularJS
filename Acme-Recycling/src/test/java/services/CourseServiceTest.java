
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
import domain.Buyer;
import domain.Course;
import domain.GPS;
import domain.Material;
import domain.Opinion;
import domain.Recycler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CourseServiceTest extends AbstractTest {

	//Supporting services ----------------------------------------------------
	@Autowired
	LabelProductService	labelProductService;

	@Autowired
	ActorService		actorService;

	@Autowired
	CourseService		courseService;

	@Autowired
	MaterialService		materialService;

	@Autowired
	RecyclerService		recyclerService;

	@PersistenceContext
	EntityManager		entityManager;

	@Autowired
	ManagerService		managerService;

	@Autowired
	OpinionService		opinionService;

	@Autowired
	BuyerService		buyerService;


	@Test
	public void driverAssist() {
		final Object testingData[][] = {
			{

				"recycler1", "course1", null

			}, {

				"recycler1", "course2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateAssist(final String username, final int courseId, final Class<?> expected) {
		Class<?> caught;
		Course course;
		caught = null;
		Recycler recyclerConnected;

		try {
			super.authenticate(username);
			course = this.courseService.findOne(courseId);
			this.courseService.assist(course);
			recyclerConnected = this.recyclerService.findByPrincipal();
			Assert.isTrue(recyclerConnected.getCourses().contains(course));

			this.labelProductService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	@Test
	public void driverNotAssist() {
		final Object testingData[][] = {
			{

				"recycler1", "course5", null

			}, {

				"recycler1", "course2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateNotAssist((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateNotAssist(final String username, final int courseId, final Class<?> expected) {
		Class<?> caught;
		Course course;
		caught = null;
		Recycler recyclerConnected;

		try {
			super.authenticate(username);
			course = this.courseService.findOne(courseId);
			this.courseService.notAssist(course);
			recyclerConnected = this.recyclerService.findByPrincipal();
			Assert.isTrue(!recyclerConnected.getCourses().contains(course));

			this.labelProductService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Test caso de uso 6.c-I: Listar cursos
	@Test
	public void driverList() {

		final Object testingData[][] = {
			{
				//Mostrar listado de los cursos del buyer2 y mirar que tenga el curso2
				"buyer2", "course2", null
			}, {
				//Mostrar listado de los cursos del buyer2 y mirar que no tenga el curso1 al ser del buyer1
				"buyer2", "course1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateList(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateList(final int buyerId, final int courseId, final Class<?> expected) {
		Class<?> caught;
		Buyer buyer;
		Course course;
		Collection<Course> courses;

		caught = null;

		try {
			buyer = this.buyerService.findOne(buyerId);
			super.authenticate(buyer.getUserAccount().getUsername());
			courses = this.courseService.findCoursesCreatedByBuyer();
			course = this.courseService.findOne(courseId);

			Assert.isTrue(courses.contains(course));

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	//Test caso de uso 6.c-II: Crear cursos
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateAndSave() {

		final Collection<Material> materialsOk;
		final Collection<Material> emptyMaterials;

		materialsOk = this.materialsOk();
		emptyMaterials = this.emptyMaterials();

		final Object testingData[][] = {
			{
				//Buyer1 crea un curso correctamente
				"buyer1", "title test", "2018/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, null
			}, {
				//Buyer1 crea un curso con el título en blanco
				"buyer1", "", "2018/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, javax.validation.ConstraintViolationException.class
			}, {
				//Buyer1 crea un curso con el minimumScore en blanco
				"buyer1", "title test", "2018/12/24", null, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, javax.validation.ConstraintViolationException.class
			}, {
				//Buyer1 crea un curso con la descripción en blanco
				"buyer1", "title test", "2018/12/24", 23, true, materialsOk, "", "name 1 coordenate", 34.5, 12.6, javax.validation.ConstraintViolationException.class
			}, {
				//Buyer1 crea un curso con el nombre de la localización en blanco
				"buyer1", "title test", "2018/12/24", 23, true, materialsOk, "description test", "", 34.5, 12.6, javax.validation.ConstraintViolationException.class
			}, {
				//Buyer1 crea un curso sin materiales (Al menos un material es necesario para un curso)
				"buyer1", "title test", "2018/12/24", 23, true, emptyMaterials, "description test", "name 1 coordenate", 34.5, 12.6, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (boolean) testingData[i][4], (Collection<Material>) testingData[i][5],
				(String) testingData[i][6], (String) testingData[i][7], (Double) testingData[i][8], (Double) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	private void templateCreateAndSave(final int buyerId, final String title, final String realisedMoment, final Integer minimumScore, final boolean draftMode, final Collection<Material> materials, final String description, final String nameCoordenate,
		final Double latitude, final Double longitud, final Class<?> expected) {
		Course course;
		Buyer buyer;
		final GPS location;

		location = new GPS();
		buyer = this.buyerService.findOne(buyerId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(buyer.getUserAccount().getUsername());
			course = this.courseService.create();

			course.setTitle(title);
			course.setRealisedMoment((new SimpleDateFormat("yyyy/MM/dd").parse(realisedMoment)));
			course.setMinimumScore(minimumScore);
			course.setDraftMode(draftMode);
			course.setMaterials(materials);
			course.setDescription(description);
			location.setName(nameCoordenate);
			location.setLatitude(latitude);
			location.setLongitude(longitud);
			course.setLocation(location);
			course = this.courseService.save(course);
			this.courseService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Test caso de uso 6.c-III: Editar cursos
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditAndSave() {

		final Collection<Material> materialsOk;
		//final Collection<Material> emptyMaterials;

		materialsOk = this.materialsOk();
		//emptyMaterials = this.emptyMaterials();

		final Object testingData[][] = {
			{
				//Buyer2 edita el titulo del curso3 que es suyo
				"buyer2", "course3", "title edited", "2020/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, 1, null
			}, {
				//Buyer2 intenta editar el titulo del curso3 que es suyo por uno en blanco
				"buyer2", "course3", "", "2020/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, 1, javax.validation.ConstraintViolationException.class
			}, {
				//Buyer2 intenta editar el titulo del curso2 que es suyo pero esta en modo final 
				"buyer2", "course2", "title edited", "2020/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, 1, IllegalArgumentException.class
			}, {
				//Buyer1 intenta editar el titulo del curso3 que NO es suyo
				"buyer1", "course3", "title edited", "2020/12/24", 23, true, materialsOk, "description test", "name 1 coordenate", 34.5, 12.6, 1, IllegalArgumentException.class
			}, {
				//Buyer2 edita la descripcion del curso3 que es suyo
				"buyer2", "course3", "title edited", "2020/12/24", 23, true, materialsOk, "description edited test", "name 1 coordenate", 34.5, 12.6, 2, null
			}, {
				//Buyer2 edita la fecha del curso3 que es suyo
				"buyer2", "course3", "title edited", "2002/12/24", 23, true, materialsOk, "description edited test", "name 1 coordenate", 34.5, 12.6, 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditAndSave(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (Integer) testingData[i][4], (boolean) testingData[i][5],
				(Collection<Material>) testingData[i][6], (String) testingData[i][7], (String) testingData[i][8], (Double) testingData[i][9], (Double) testingData[i][10], (Integer) testingData[i][11], (Class<?>) testingData[i][12]);
	}

	private void templateEditAndSave(final int buyerId, final int courseId, final String title, final String realisedMoment, final Integer minimumScore, final boolean draftMode, final Collection<Material> materials, final String description,
		final String nameCoordenate, final Double latitude, final Double longitud, final Integer aux, final Class<?> expected) {
		Course course;
		Buyer buyer;

		buyer = this.buyerService.findOne(buyerId);
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(buyer.getUserAccount().getUsername());
			course = this.courseService.findOne(courseId);
			//Para el primer caso editamos solo el título
			if (aux == 1) {
				course.setTitle(title);
				course = this.courseService.save(course);
				//Para el segundo caso editamos solo la descripción
			} else if (aux == 2) {
				course.setDescription(description);
				course = this.courseService.save(course);
				//Para el tercer caso editamos la fecha por una en pasado
			} else if (aux == 3) {
				course.setRealisedMoment((new SimpleDateFormat("yyyy/MM/dd").parse(realisedMoment)));
				course = this.courseService.save(course);
			}

			this.courseService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Test caso de uso 6.c-IV: Eliminar cursos
	@Test
	public void driverDeleteBuyer() {
		final Object testingData[][] = {
			{
				//Buyer 4 elimina el course5 correctamente
				"buyer4", "course6", null
			}, {
				//Buyer 2 intenta eliminar el course 1 que NO es suyo.
				"buyer2", "course1", IllegalArgumentException.class
			}, {
				//Buyer 2 intenta eliminar el course 2 que está en modo final
				"buyer2", "course2", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteBuyer((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateDeleteBuyer(final String username, final int courseId, final Class<?> expected) {
		Course course;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			course = this.courseService.findOne(courseId);
			this.courseService.delete(course);

			this.courseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	private Collection<Material> materialsOk() {
		Collection<Material> result;
		result = new ArrayList<Material>();
		final Material material1;
		final Material material2;

		material1 = this.materialService.findOne(super.getEntityId("material1"));
		material2 = this.materialService.findOne(super.getEntityId("material2"));

		result.add(material1);
		result.add(material2);

		return result;
	}
	private Collection<Material> emptyMaterials() {
		final Collection<Material> result;
		result = new ArrayList<>();

		return result;
	}

	//Test caso de uso 8.d-IV: Eliminar cursos por parte del admin
	@Test
	public void driverDeleteAdmin() {
		final Object testingData[][] = {
			{
				//Admin elimina el course1 correctamente al no tener asistentes.
				"admin", "course1", null
			}, {
				//Admin intenta eliminar el course 3 que SÍ tiene asistentes.
				"admin", "course3", IllegalArgumentException.class
			}, {
				//Admin intenta eliminar el course 5 que SÍ tiene asistentes
				"admin", "course5", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteAdmin((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateDeleteAdmin(final String username, final int courseId, final Class<?> expected) {
		Course course;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			course = this.courseService.findOne(courseId);
			this.courseService.deleteAdmin(course);

			this.courseService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Caso de uso 1.d: Listar todos los cursos del sistema y ver sus opiniones. Pero no podrás escribir una opinión ni asistir al curso.
	@Test
	public void driverListNonAuthenticated() {

		final Object testingData[][] = {
			{
				//Se comprueba que el course3 se encuentra en el listado y que tiene la opinion8
				"course3", "opinion8", null
			}, {
				//Se comprueba que el course1 se encuentra en el listado pero que NO tiene la opinion1 porque no es suya
				"course1", "opinion1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListNonAuthenticated(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateListNonAuthenticated(final int courseId, final int opinionId, final Class<?> expected) {
		Class<?> caught;
		Collection<Course> courses;
		Course course;
		Opinion opinion;

		caught = null;

		try {
			courses = this.courseService.findAll();
			course = this.courseService.findOne(courseId);
			opinion = this.opinionService.findOne(opinionId);
			Assert.isTrue(courses.contains(course));
			Assert.isTrue(course.getOpinions().contains(opinion));

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
}
