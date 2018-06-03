
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.OpinionRepository;
import domain.Actor;
import domain.Course;
import domain.Opinable;
import domain.Opinion;
import domain.Recycler;
import forms.OpinionForm;

@Service
@Transactional
public class OpinionService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private OpinionRepository	opinionRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private OpinableService		opinableService;

	@Autowired
	private Validator			validator;

	@Autowired
	private RecyclerService		recyclerService;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------
	public OpinionService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Opinion create() {
		Opinion result;

		//No copiar la siguiente linea en el reconstruct
		result = new Opinion();

		return result;
	}
	public Opinion findOne(final int opinionId) {
		Opinion result;

		result = this.opinionRepository.findOne(opinionId);

		return result;
	}

	public Collection<Opinion> findAll() {
		Collection<Opinion> result;

		result = this.opinionRepository.findAll();

		return result;
	}

	public Opinion save(final OpinionForm opinionForm) {
		final Opinion result;
		Opinion opinion;
		Actor actor;

		opinion = opinionForm.getOpinion();

		if (opinion.getId() == 0) {
			Date createdMoment;

			createdMoment = new Date(System.currentTimeMillis() - 1000);
			opinion.setCreatedMoment(createdMoment);

			//No se puede crear una opinion en un opinable si ya has creado una
			final Opinable opinable = this.opinableService.findOneManual(opinionForm.getOpinableId());
			final Collection<Opinable> myOpinables = this.opinableService.findByActorId(opinion.getActor().getId());
			Assert.isTrue(!myOpinables.contains(opinable), "you have an opinion in this opinable");

		} else {
			actor = this.actorService.findPrincipal();
			Assert.isTrue(actor.getOpinions().contains(this.findOne(opinion.getId())), "Cannot commit this operation, because it's illegal");
			Assert.notNull(opinion);
		}

		//Solo se crea opiniones de los cursos a los que ha asistido el recycler
		if (!opinionForm.isOpinableItem()) {
			Course course;
			Recycler recyclerPrincipal;
			recyclerPrincipal = this.recyclerService.findByPrincipal();
			course = (Course) this.opinableService.findOneManual(opinionForm.getOpinableId());
			Assert.isTrue(recyclerPrincipal.getCourses().contains(course), "you don't assist to this course");
		}

		result = this.opinionRepository.save(opinion);

		//Añadimos la opinion al Opinable si es la primera vez que creamos la opinion
		if (opinion.getId() == 0) {
			Opinable opinable;
			opinable = this.opinableService.findOneManual(opinionForm.getOpinableId());
			opinable.getOpinions().add(result);
			this.opinableService.save(opinable);
		}

		return result;
	}
	public void delete(final Opinion opinion) {
		Assert.notNull(opinion);
		Actor actorPrincipal;

		this.opinionRepository.delete(opinion);
		//Eliminamos la opinion de la lista de opiniones del actor ya que es unidireccional y no se actualiza
		actorPrincipal = this.actorService.findPrincipal();
		actorPrincipal.getOpinions().remove(opinion);
	}

	public Collection<Opinion> findOpinableItemByActor(final int ActorId) {
		Collection<Opinion> result;

		result = this.opinionRepository.findOpinableItemByActor(ActorId);

		return result;
	}

	public Collection<Opinion> findOpinableCourseByActor(final int ActorId) {
		Collection<Opinion> result;

		result = this.opinionRepository.findOpinableCourseByActor(ActorId);

		return result;
	}

	public OpinionForm reconstruct(final OpinionForm opinionForm, final BindingResult bindingResult) {
		Opinion opinion;
		Opinion opinionBD;

		opinion = opinionForm.getOpinion();

		if (opinion.getId() == 0) {
			//Si no se ha elegido ningun opinable, hacemos que salte en mensaje de notNull
			if (opinionForm.getOpinableId() == 0)
				opinionForm.setOpinableId(null);
		} else {
			opinionBD = this.opinionRepository.findOne(opinion.getId());
			opinion.setId(opinionBD.getId());
			opinion.setVersion(opinionBD.getVersion());
			opinion.setCreatedMoment(opinionBD.getCreatedMoment());
			opinionForm.setOpinableId(this.opinableService.findByOpinionId(opinion.getId()).getId());
			opinionForm.setOpinableItem(this.opinableService.isItem(opinionForm.getOpinableId()));
		}
		opinion.setActor(this.actorService.findPrincipal());
		opinionForm.setOpinion(opinion);
		this.validator.validate(opinionForm, bindingResult);

		return opinionForm;
	}
}
