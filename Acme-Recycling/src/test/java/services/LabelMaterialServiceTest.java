
package services;

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
import domain.Actor;
import domain.LabelMaterial;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LabelMaterialServiceTest extends AbstractTest {

	//Supporting services ----------------------------------------------------
	@Autowired
	LabelMaterialService	labelMaterialService;

	@Autowired
	ActorService			actorService;

	@PersistenceContext
	EntityManager			entityManager;

	@Autowired
	ManagerService			managerService;


	@Test
	public void driverCreateAndSave() {
		final Object testingData[][] = {
			{
				//Se crea un chirp correctamenre estando logeado como user1
				"manager1", "prueba labelMaterial", false, null

			}, {
				//Se crea un chirp incorrectamente ya que estamos logeados como admin
				"admin", "prueba labelMaterial", false, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);
	}
	private void templateCreateAndSave(final String username, final String name, final Boolean byDefault, final Class<?> expected) {
		Class<?> caught;
		LabelMaterial labelMaterial;
		caught = null;

		try {
			super.authenticate(username);
			labelMaterial = this.labelMaterialService.create();

			labelMaterial.setName(name);
			labelMaterial.setByDefault(byDefault);
			labelMaterial = this.labelMaterialService.save(labelMaterial);
			this.labelMaterialService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{

				"manager1", "labelMaterial4", null
			}, {

				"manager1", "labelMaterial1", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int labelMaterialId, final Class<?> expected) {
		LabelMaterial labelMaterial;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			labelMaterial = this.labelMaterialService.findOne(labelMaterialId);
			this.labelMaterialService.delete(labelMaterial);

			this.labelMaterialService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	@Test
	public void driverList() {
		final Object testingData[][] = {
			{

				"manager1", 6, null
			}, {

				"manager1", 7, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateList(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateList(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		Actor actorConnected;
		actorConnected = this.actorService.findOne(usernameId);
		Collection<LabelMaterial> labelMaterials;

		caught = null;
		try {
			super.authenticate(actorConnected.getUserAccount().getUsername());
			this.managerService.checkPrincipal();
			labelMaterials = this.labelMaterialService.findAll();
			Assert.isTrue(labelMaterials.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverEdit() {
		final Object testingData[][] = {
			{
				//Se edita un product correctamente
				"manager1", "labelMaterial4", "Label edited positive", null
			}, {
				//Se edita un product correctamente
				"manager1", "labelMaterial1", "Label edited negative", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateEdit(final String username, final int labelMaterialId, final String name, final Class<?> expected) {
		Class<?> caught;
		LabelMaterial labelMaterial;

		caught = null;
		try {
			super.authenticate(username);
			labelMaterial = this.labelMaterialService.findOne(labelMaterialId);
			labelMaterial.setName(name);

			this.labelMaterialService.save(labelMaterial);

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

}
