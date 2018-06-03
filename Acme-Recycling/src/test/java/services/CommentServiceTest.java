
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Comment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CommentServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	CommentService	commentService;

	@Autowired
	NewscastService	newscastService;

	@PersistenceContext
	EntityManager	entityManager;


	//Crear y salvar un comentario
	@Test
	public void driverCreateAndSaveComment() {

		final Object testingData[][] = {
			{
				//Crear comentario correctamente
				"body", "new1", "recycler1", null
			}, {
				//Crear incorrectamente un comentario dejando el cuerpo vacío
				"", "new1", "recycler2", javax.validation.ConstraintViolationException.class
			}

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String body, final int newscastId, final String username, final Class<?> expected) {
		Comment comment;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			comment = this.commentService.create();
			comment.setBody(body);
			comment = this.commentService.save(comment);
			this.unauthenticate();
			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//Edit comment
	@Test
	public void driveEditComment() {

		final Object testingData[][] = {
			//Editar un comentario correctante
			{
				"comment1", "recycler1", "editado body", null
			}, {
				//Editar comentario incorrectamente por el reciclar 2 que no es el suyo
				"comment1", "recycler2", "body editado", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditComment((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	public void templateEditComment(final String entity, final String username, final String body, final Class<?> expected) {

		Class<?> caught;
		Comment comment;

		caught = null;

		try {
			super.authenticate(username);
			comment = this.commentService.findOne(super.getEntityId(entity));
			comment.setBody(body);

			this.commentService.save(comment);
			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}

	//Delete comment
	@Test
	public void driveDeleteComment() {

		final Object testingData[][] = {
			//Borrar comentario correctamente
			{
				"comment1", "recycler1", null
			}, {
				//borrar comentario incorrectamente
				"comment1", "recycler2", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDeleteComment((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);

	}
	public void templateDeleteComment(final String entity, final String username, final Class<?> expected) {

		Class<?> caught;
		Comment comment;

		caught = null;

		try {
			super.authenticate(username);
			comment = this.commentService.findOne(super.getEntityId(entity));

			this.commentService.delete(comment);
			this.commentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
}
