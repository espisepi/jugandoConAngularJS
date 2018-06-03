
package controllers.actor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CourseService;
import services.ItemService;
import services.OpinableService;
import services.OpinionService;
import controllers.AbstractController;
import domain.Actor;
import domain.Course;
import domain.Item;
import domain.Opinion;
import forms.OpinionForm;

@Controller
@RequestMapping("/opinion/actor")
public class OpinionActorController extends AbstractController {

	//	//Services--------------------------------------------

	@Autowired
	private OpinionService	opinionService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ItemService		itemService;

	@Autowired
	private CourseService	courseService;

	@Autowired
	private OpinableService	opinableService;


	//Constructor--------------------------------------------------------

	public OpinionActorController() {
		super();
	}

	//Listar mis opiniones de itemos
	@RequestMapping(value = "/myListOpinionItem", method = RequestMethod.GET)
	public ModelAndView myListOpinionItem() {
		ModelAndView result;
		Collection<Opinion> myOpinions;
		Actor actorPrincipal;

		actorPrincipal = this.actorService.findPrincipal();
		myOpinions = this.opinionService.findOpinableItemByActor(actorPrincipal.getId());

		result = new ModelAndView("opinion/list");
		result.addObject("opinions", myOpinions);
		result.addObject("showButtonEdit", true);
		result.addObject("requestURI", "opinion/actor/myListOpinionItem.do?d-16544-p=1");
		return result;
	}

	//Listar mis opiniones de cursos
	@RequestMapping(value = "/myListOpinionCourse", method = RequestMethod.GET)
	public ModelAndView myListOpinionCourse() {
		ModelAndView result;
		Collection<Opinion> myOpinions;
		Actor actorPrincipal;

		actorPrincipal = this.actorService.findPrincipal();
		myOpinions = this.opinionService.findOpinableCourseByActor(actorPrincipal.getId());

		//Solo pueden opinar los que asisten a los cursos, es decir, los recyclers
		final Authority authority = new Authority();
		authority.setAuthority("RECYCLER");
		Assert.isTrue(actorPrincipal.getUserAccount().getAuthorities().contains(authority), "must be a recycler");

		result = new ModelAndView("opinion/list");
		result.addObject("opinions", myOpinions);
		result.addObject("showButtonEdit", true);
		result.addObject("requestURI", "opinion/actor/myListOpinionCourse.do?d-16544-p=1");
		return result;
	}

	// Create opinable item-----------------------------------------------------------------

	@RequestMapping(value = "/createOpinableItem", method = RequestMethod.GET)
	public ModelAndView createOpinableItem() {
		ModelAndView result;
		Opinion opinion;
		OpinionForm opinionForm;

		opinion = this.opinionService.create();

		opinionForm = new OpinionForm();
		opinionForm.setOpinion(opinion);
		opinionForm.setOpinableItem(true);

		result = this.createEditModelAndView(opinionForm);
		result.addObject("showItem", true);

		return result;
	}
	// Create opinable item-----------------------------------------------------------------

	@RequestMapping(value = "/createOpinableCourse", method = RequestMethod.GET)
	public ModelAndView createOpinableCourse() {
		ModelAndView result;
		Opinion opinion;
		OpinionForm opinionForm;

		//Solo pueden opinar los que asisten a los cursos, es decir, los recyclers
		final Authority authority = new Authority();
		authority.setAuthority("RECYCLER");
		Assert.isTrue(this.actorService.findPrincipal().getUserAccount().getAuthorities().contains(authority), "must be a recycler");

		opinion = this.opinionService.create();

		opinionForm = new OpinionForm();
		opinionForm.setOpinion(opinion);
		opinionForm.setOpinableItem(false);

		result = this.createEditModelAndView(opinionForm);
		result.addObject("showItem", false);

		return result;
	}
	//Edition--------------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int opinionId) {
		ModelAndView result;
		Opinion opinion;
		Actor actor;
		final OpinionForm opinionForm;
		int opinableId;

		actor = this.actorService.findPrincipal();
		opinion = this.opinionService.findOne(opinionId);
		Assert.isTrue(actor.getOpinions().contains(opinion), "Cannot commit this operation, because it's illegal");
		Assert.notNull(opinion);

		//Pasar el opinion a opinionform para la vista
		opinionForm = new OpinionForm();
		opinionForm.setOpinion(opinion);
		opinableId = this.opinableService.findByOpinionId(opinion.getId()).getId();
		if (this.opinableService.isItem(opinableId))
			opinionForm.setOpinableItem(true);
		else
			opinionForm.setOpinableItem(false);

		result = this.createEditModelAndView(opinionForm);
		result.addObject("hiddenSelects", true);

		if (this.opinableService.isItem(opinableId))
			result.addObject("opinableItem", true);
		else
			result.addObject("opinableItem", false);

		return result;
	}

	// Save Opinion Item-----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(OpinionForm opinionForm, final BindingResult binding) {
		ModelAndView result;

		opinionForm = this.opinionService.reconstruct(opinionForm, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(opinionForm);
		else
			try {
				this.opinionService.save(opinionForm);
				if (this.opinableService.isItem(opinionForm.getOpinableId()))
					result = new ModelAndView("redirect:myListOpinionItem.do");
				else
					result = new ModelAndView("redirect:myListOpinionCourse.do");

			} catch (final Throwable oops) {
				if (oops.getMessage().equals("you have an opinion in this opinable"))
					result = this.createEditModelAndView(opinionForm, "opinion.duplicateOpinable.error");
				else if (oops.getMessage().equals("you don't assist to this course"))
					result = this.createEditModelAndView(opinionForm, "opinion.courseNotAssist.error");
				else
					result = this.createEditModelAndView(opinionForm, "opinion.commit.error");
			}
		return result;
	}
	protected ModelAndView createEditModelAndView(final OpinionForm opinionForm) {
		Assert.notNull(opinionForm);
		ModelAndView result;
		result = this.createEditModelAndView(opinionForm, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final OpinionForm opinionForm, final String messageCode) {
		assert opinionForm != null;

		ModelAndView result;

		result = new ModelAndView("opinion/edit");
		if (opinionForm.isOpinableItem()) {
			Collection<Item> items;

			items = this.itemService.findToOpineByActorId(this.actorService.findPrincipal().getId());
			result.addObject("items", items);
			result.addObject("selectItems", true);
			result.addObject("showItem", true);
			result.addObject("opinableItem", true);
		} else {
			Collection<Course> courses;

			courses = this.courseService.findToOpineByActorId(this.actorService.findPrincipal().getId());
			result.addObject("courses", courses);
			result.addObject("selectCourses", true);
			result.addObject("showItem", false);
			result.addObject("opinableFalse", true);
		}

		if (opinionForm.getOpinion().getId() != 0)
			result.addObject("hiddenSelects", true);

		result.addObject("opinionForm", opinionForm);
		result.addObject("requestURI", "opinion/actor/edit.do");
		result.addObject("message", messageCode);
		return result;

	}
}
