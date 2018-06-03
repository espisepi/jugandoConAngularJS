
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.CourseService;
import controllers.AbstractController;
import domain.Course;

@Controller
@RequestMapping("/course/admin")
public class CourseAdminController extends AbstractController {

	// Services---------------------------------------------------------
	@Autowired
	private CourseService	courseService;


	//Constructor--------------------------------------------------------

	public CourseAdminController() {
		super();
	}

	//Listing

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Course> courses;

		courses = this.courseService.findAllWithoutRecyclers();

		result = new ModelAndView("course/list");
		result.addObject("courses", courses);
		//result.addObject("showDelete", true);
		result.addObject("requestURI", "course/admin/list.do?d-16544-p=1");
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(final int courseId) {
		ModelAndView result;
		Course course;

		course = this.courseService.findOne(courseId);
		Assert.notNull(course);
		try {
			this.courseService.deleteAdmin(course);
			result = new ModelAndView("redirect:list.do?d-16544-p=1");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("No puede tener recicladores"))
				result = this.listWithMessage("course.commit.error.recyclers");
			else
				result = this.listWithMessage("course.commit.error");
		}

		return result;
	}

	protected ModelAndView listWithMessage(final String message) {
		final ModelAndView result;
		Collection<Course> courses;
		courses = this.courseService.findAllWithoutRecyclers();
		result = new ModelAndView("course/list");
		result.addObject("courses", courses);
		result.addObject("requestURI", "course/admin/list.do?d-16544-p=1");
		result.addObject("message", message);

		return result;

	}

	//display ------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int courseId) {
		ModelAndView result;
		Course course;

		course = this.courseService.findOne(courseId);
		result = new ModelAndView("course/display");
		result.addObject("course", course);
		result.addObject("requestURI", "course/display.do?courseId=" + courseId);

		return result;
	}

}
