
package controllers.buyer;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BuyerService;
import services.CourseService;
import services.LessonService;
import services.MaterialService;
import controllers.AbstractController;
import domain.Course;
import domain.Lesson;
import domain.Material;

@Controller
@RequestMapping("/course/buyer")
public class CourseBuyerController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private CourseService	courseService;
	@Autowired
	private BuyerService	buyerService;
	@Autowired
	private LessonService	lessonService;
	@Autowired
	private MaterialService	materialService;


	//Constructor--------------------------------------------------------

	public CourseBuyerController() {
		super();
	}

	//Display-----------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView displayCourse(@RequestParam final int courseId) {
		final ModelAndView result;
		Course course = new Course();
		Collection<Lesson> lessons;
		Collection<Course> coursesOfBuyer;

		coursesOfBuyer = this.courseService.findCoursesCreatedByBuyer();
		course = this.courseService.findOne(courseId);
		//Un buyer solo podrá ver sus cursos.
		Assert.isTrue(coursesOfBuyer.contains(course));
		lessons = new ArrayList<>();

		lessons = this.lessonService.findLessonsByCourseId(courseId);

		result = new ModelAndView("course/display");
		result.addObject("course", course);
		result.addObject("lessons", lessons);
		result.addObject("requestURI", "course/buyer/display.do");

		return result;
	}

	//Listing-----------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		this.buyerService.checkPrincipal();
		final ModelAndView result;
		final Collection<Course> courses;
		courses = this.courseService.findCoursesCreatedByBuyer();
		result = new ModelAndView("course/mylist");
		result.addObject("courses", courses);
		result.addObject("requestURI", "course/buyer/list.do");
		return result;
	}

	//Creation-----------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Course course;

		course = this.courseService.create();

		result = this.createEditModelAndView(course);
		result.addObject("requestURI", "course/buyer/edit.do");
		return result;

	}

	//Edit -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int courseId) {
		ModelAndView result;
		final Course course;
		Assert.notNull(courseId);

		course = this.courseService.findOne(courseId);

		//Sólo si está en modo borrador se podrá editar el course
		Assert.isTrue(course.isDraftMode());
		result = this.createEditModelAndView(course);
		return result;
	}
	// Save -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Course course, final BindingResult binding) {
		ModelAndView result;

		course = this.courseService.reconstruct(course, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(course);
		else
			try {
				this.courseService.save(course);
				result = new ModelAndView("redirect:list.do?d-16544-p=1");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("La fecha no puede ser nula"))
					result = this.createEditModelAndView(course, "request.course.moment.error");
				else if (oops.getMessage().equals("Al menos un material es obligatorio"))
					result = this.createEditModelAndView(course, "request.course.materials.error");
				else if (oops.getMessage().equals("Fecha de realizacion debe ser posterior a la actual"))
					result = this.createEditModelAndView(course, "request.course.date.error");
				else if (oops.getMessage().equals("No puede ser 0 el minimum"))
					result = this.createEditModelAndView(course, "request.course.minimumScore.error");
				else
					result = this.createEditModelAndView(course, "request.commit.error");
			}
		return result;
	}

	//Delete -----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute final Course course, final BindingResult bindingResult) {
		ModelAndView result;

		//buyer = new Buyer();
		//buyer = this.buyerService.findByPrincipal();
		try {
			//Solo un buyer podrá eliminar un curso
			Assert.isTrue(this.buyerService.checkPrincipalBoolean());
			//buyer.getCourses().remove(course);
			this.courseService.delete(course);
			result = new ModelAndView("redirect:list.do?d-16544-p=1");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(course, "course.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Course course) {
		assert course != null;
		ModelAndView result;
		result = this.createEditModelAndView(course, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Course course, final String message) {

		assert course != null;
		Collection<Material> materials;
		ModelAndView result;

		materials = new ArrayList<>();

		materials = this.materialService.findAll();

		result = new ModelAndView("course/edit");
		result.addObject("course", course);
		result.addObject("message", message);
		result.addObject("materials", materials);

		return result;

	}

}
