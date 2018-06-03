
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CourseRepository;
import domain.Buyer;
import domain.Course;
import domain.Item;
import domain.Lesson;
import domain.Material;
import domain.Opinion;
import domain.Recycler;

@Service
@Transactional
public class CourseService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private CourseRepository	courseRepository;

	@Autowired
	private BuyerService		buyerService;

	@Autowired
	private RecyclerService		recyclerService;

	@Autowired
	private LessonService		lessonService;

	@Autowired
	private Validator			validator;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public CourseService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Course create() {
		Course result;
		Collection<Material> materials;
		Collection<Lesson> lessons;
		Collection<Opinion> opinions;

		materials = new ArrayList<Material>();
		lessons = new ArrayList<Lesson>();
		opinions = new ArrayList<Opinion>();

		//Solo un buyer podrá crear un curso
		Assert.isTrue(this.buyerService.checkPrincipalBoolean());

		result = new Course();

		result.setMaterials(materials);
		result.setLessons(lessons);
		result.setOpinions(opinions);

		return result;
	}

	public Course save(final Course course) {
		Assert.notNull(course);
		final Course result;
		Collection<Course> courses;
		Buyer buyer;
		Date date;

		date = new Date();
		buyer = new Buyer();
		buyer = this.buyerService.findByPrincipal();
		courses = new ArrayList<>(this.courseRepository.findCoursesCreatedByBuyer(buyer.getId()));

		//Un buyer solo podrá editar un curso que haya creado él.
		if (course.getId() != 0)
			Assert.isTrue(courses.contains(course));
		//Solo un buyer podrá crear un curso
		Assert.isTrue(this.buyerService.checkPrincipalBoolean());
		//Sólo si está en modo borrador se podrá editar el course
		//Assert.isTrue(course.isDraftMode());
		//Si está en modo final la fecha es obligatoria
		if (!course.isDraftMode())
			Assert.notNull(course.getRealisedMoment(), "La fecha no puede ser nula");
		//Al menos un material es obligatorio
		Assert.isTrue(course.getMaterials().size() > 0, "Al menos un material es obligatorio");
		//El minimumScore no puede ser negativo
		if (course.getMinimumScore() != null)
			Assert.isTrue(course.getMinimumScore() > 0, "No puede ser 0 el minimum");

		if (course.getRealisedMoment() != null)
			Assert.isTrue(course.getRealisedMoment().after(date), "Fecha de realizacion debe ser posterior a la actual");

		result = this.courseRepository.save(course);
		if (course.getId() == 0)
			buyer.getCourses().add(result);
		return result;
	}

	public void delete(final Course course) {
		Assert.notNull(course);
		Buyer buyerConnected;
		Collection<Recycler> recyclers;
		Collection<Course> coursesOfBuyer;
		Collection<Lesson> lessons;

		buyerConnected = this.buyerService.findByPrincipal();
		coursesOfBuyer = this.courseRepository.findCoursesCreatedByBuyer(buyerConnected.getId());

		//Solo podrá eliminar los cursos que sean del buyer que esté conectado
		Assert.isTrue(coursesOfBuyer.contains(course));
		//Sólo si está en modo borrar se podrá eliminar el course
		Assert.isTrue(course.isDraftMode(), "Existe un curso que no esta en modo final");

		lessons = this.lessonService.findLessonsByCourseId(course.getId());
		if (!lessons.isEmpty())
			for (final Lesson l : lessons)
				this.lessonService.delete(l);

		recyclers = this.recyclerService.findRecyclerByCourse(course);
		for (final Recycler r : recyclers)
			r.getCourses().remove(course);

		//Borramos ese curso de la lista de cursos del Buyer
		buyerConnected.getCourses().remove(course);
		this.courseRepository.delete(course);
	}

	public void deleteAdmin(final Course course) {
		Assert.notNull(course);
		Buyer buyer;
		Collection<Recycler> recyclers;
		Collection<Lesson> lessons;

		buyer = this.buyerService.findBuyerByCourse(course);
		recyclers = this.recyclerService.findRecyclerByCourse(course);
		Assert.isTrue(recyclers.size() == 0, "No puede tener recicladores");

		lessons = this.lessonService.findLessonsByCourseId(course.getId());
		if (!lessons.isEmpty())
			for (final Lesson l : lessons)
				this.lessonService.deleteAdmin(l);

		//A lso recicladores le quitamos el curso
		for (final Recycler r : recyclers)
			r.getCourses().remove(course);

		buyer.getCourses().remove(course);
		this.courseRepository.delete(course);
	}

	public Collection<Course> findAll() {
		Collection<Course> result;
		result = this.courseRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Collection<Course> findAllWithoutRecyclers() {
		Collection<Course> courses;
		Collection<Course> result;
		Collection<Recycler> recyclers;

		result = new ArrayList<>();
		courses = this.courseRepository.findAll();

		for (final Course c : courses) {
			recyclers = this.recyclerService.findRecyclerByCourse(c);
			if (recyclers.isEmpty())
				result.add(c);
		}
		Assert.notNull(result);
		return result;
	}

	public Course findOne(final int courseId) {
		Assert.isTrue(courseId != 0);
		Course result;
		result = this.courseRepository.findOne(courseId);
		return result;
	}

	// Other business methods -------------------------------------------------

	public Collection<Course> coursesOfRecyclerFinished() {
		Collection<Course> result;
		Recycler recyclerConnected;

		recyclerConnected = this.recyclerService.findByPrincipal();

		result = this.courseRepository.coursesOfRecyclerFinished(recyclerConnected.getId());

		return result;

	}
	public Collection<Course> findCoursesCreatedByBuyer() {
		Collection<Course> result;
		Buyer buyerConnected;

		buyerConnected = this.buyerService.findByPrincipal();

		result = this.courseRepository.findCoursesCreatedByBuyer(buyerConnected.getId());

		return result;

	}

	public Collection<Course> coursesAvailables(final Recycler recycler) {

		Collection<Item> itemsOfRecycler;
		Collection<Course> coursesAvailables;
		Collection<Course> coursesOfRecycler;
		itemsOfRecycler = recycler.getItems();

		Integer puntuationOfRecycler = 0;
		for (final Item i : itemsOfRecycler)
			if (i.getRequest() != null)
				if (i.getRequest().getStatus().equals("FINISHED"))
					puntuationOfRecycler = puntuationOfRecycler + i.getValue();

		coursesOfRecycler = recycler.getCourses();
		coursesAvailables = this.courseRepository.coursesAvailables(puntuationOfRecycler);
		coursesAvailables.removeAll(coursesOfRecycler);
		return coursesAvailables;

	}

	public void assist(final Course course) {
		Recycler recyclerConnected;
		Recycler result;
		Collection<Course> coursesAvailables;
		Double puntuationOfCourse;
		Double puntuationOfRecycler;
		Collection<Lesson> lessonsOfCourse;

		lessonsOfCourse = this.lessonService.findLessonsByCourseId(course.getId());
		recyclerConnected = this.recyclerService.findByPrincipal();
		coursesAvailables = this.coursesAvailables(recyclerConnected);

		puntuationOfCourse = course.getMinimumScore() * 1.0;
		puntuationOfRecycler = this.recyclerService.puntuationOfRecycler1(recyclerConnected.getId());
		Assert.isTrue(!lessonsOfCourse.isEmpty());
		Assert.isTrue(puntuationOfRecycler >= puntuationOfCourse);
		Assert.isTrue(coursesAvailables.contains(course));
		Assert.notNull(course);
		Assert.notNull(recyclerConnected);
		Assert.isTrue(!course.isDraftMode());

		recyclerConnected.getCourses().add(course);

		result = this.recyclerService.save(recyclerConnected);

		Assert.notNull(result);
	}

	public void notAssist(final Course course) {
		Recycler recyclerConnected;
		Recycler result;
		final Date actual = new Date();
		final Long diaMili = (long) 86400000;
		final Long semana = diaMili * 7;

		final Long restaFechas = course.getRealisedMoment().getTime() - actual.getTime();
		Assert.isTrue(restaFechas > semana, "This course will start in one week or less");
		recyclerConnected = this.recyclerService.findByPrincipal();

		Assert.notNull(course);

		Assert.notNull(recyclerConnected);
		Assert.isTrue(recyclerConnected.getCourses().contains(course));
		recyclerConnected.getCourses().remove(course);
		Assert.isTrue(!course.isDraftMode());

		result = this.recyclerService.save(recyclerConnected);

		Assert.notNull(result);
	}

	//	RECONSTRUCTOR
	public Course reconstruct(final Course course, final BindingResult bindingResult) {
		Course result;
		Course courseBd;
		final Collection<Material> materials;
		materials = new ArrayList<Material>();

		if (course.getId() == 0) {
			Collection<Lesson> lessons;
			Collection<Opinion> opinions;

			result = course;

			lessons = new ArrayList<Lesson>();
			opinions = new ArrayList<Opinion>();

			if (course.getMaterials() == null || course.getMaterials().contains(null))
				course.setMaterials(materials);

			result.setLessons(lessons);
			result.setOpinions(opinions);
		}

		else {
			courseBd = this.courseRepository.findOne(course.getId());
			course.setId(courseBd.getId());
			course.setVersion(courseBd.getVersion());
			course.setLessons(courseBd.getLessons());

			if (course.getMaterials() == null || course.getMaterials().contains(null))
				course.setMaterials(materials);

			course.setOpinions(courseBd.getOpinions());
			result = course;
		}
		this.validator.validate(result, bindingResult);
		return result;
	}

	public Collection<Course> findCoursesOfMaterial(final int materialId) {
		Collection<Course> courses;
		courses = this.courseRepository.findCoursesOfMaterial(materialId);
		return courses;
	}

	public Collection<Course> findToOpineByActorId(final int actorId) {
		Collection<Course> result;
		Recycler recyclerPrincipal;

		recyclerPrincipal = this.recyclerService.findByPrincipal();
		result = new ArrayList<Course>(recyclerPrincipal.getCourses());
		//Se eliminan los cursos en los que ya se ha opinado
		result.removeAll(new ArrayList<Course>(this.courseRepository.findToOpineByActorId(actorId)));

		return result;
	}

	public Course findCourseByLessonId(final int lessonId) {
		Course course;
		Assert.notNull(lessonId);
		course = this.courseRepository.findCourseByLessonId(lessonId);
		return course;
	}

	public Collection<Course> findCoursesNoAuthenticate() {
		Collection<Course> courses;
		courses = this.courseRepository.findCoursesNoAuthenticate();
		return courses;
	}

	public void flush() {
		this.courseRepository.flush();
	}
}
