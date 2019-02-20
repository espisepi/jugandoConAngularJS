
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

import utilities.AbstractTest;
import domain.Newspaper;
import domain.Volume;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class VolumeServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	VolumeService		volumeService;

	@Autowired
	NewspaperService	newspaperService;

	@Autowired
	UnderwriteService	underwriteService;

	@PersistenceContext
	EntityManager		entityManager;


	//Caso de uso 8.1: List the volumes in the system and browse their newspapers as long as they are authorised (for instance, a private newspaper cannot be fully displayed to unauthenticated actors).
	@Test
	public void driverList() {

		final Object testingData[][] = {
			{
				//Mostrar listado de los no autenticados y mirar que el volume2 contiene el newspaper3
				"volume2", "newspaper3", 0, null
			}, {
				//Mostrar listado de los no autenticados y mirar que el volume2 no contiene el newspaper8
				"volume2", "newspaper8", 1, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave(super.getEntityId((String) testingData[i][0]), super.getEntityId((String) testingData[i][1]), (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateCreateAndSave(final int volumeId, final int newspaperId, final int aux, final Class<?> expected) {
		Collection<Volume> volumes;
		Class<?> caught;
		Volume volume;
		Newspaper newspaper;

		caught = null;
		try {
			volumes = this.volumeService.findAll();
			volume = this.volumeService.findOne(volumeId);
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.isTrue(volumes.contains(volume));
			Assert.isTrue(volume.getNewspapers().contains(newspaper));
			//this.volumeService.flush();
			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	//Caso de uso 10.1 usernCreate a volume with as many published newspapers as he or she wishes.
	//Note that the newspapers in a volume can be added or removed at any time. The same newspaper may be used to create different volumes.
	@Test
	public void driverListEdit() {
		final Object testingData[][] = {
			{
				//Se edita un volume correctamente
				"user1", "volume1", "title nuevo", "description test", 2192, "newspaper1", false, null
			}, {
				//Se edita un volume con un año incorrecto
				"user1", "volume1", "title nuevo", "description test", 3001, "newspaper1", false, ConstraintViolationException.class
			}, {
				//Se edita un newspaper que no es de tu propiedad
				"user1", "volume2", "title nuevo", "description test", 2192, "newspaper1", false, IllegalArgumentException.class
			}, {
				//Editar un volumen para añadir un nuevo newspapers privado
				"user3", "volume5", "title nuevo", "description test", 2192, "newspaper4", false, null
			}, {
				//Editar un volumen para añadir un nuevo newspapers publico
				"user3", "volume5", "title nuevo", "description test", 2192, "newspaper7", false, null
			}, {
				//Editar un volumen para añadir un nuevo newspapers privado que no es tuyo
				"user3", "volume5", "title nuevo", "description test", 2192, "newspaper1", false, IllegalArgumentException.class
			}, {
				//Editar un volumen para añadir un nuevo newspapers publico que no es tuyo
				"user3", "volume5", "title nuevo", "description test", 2192, "newspaper2", false, IllegalArgumentException.class
			}, {
				//Editar un volumen que no es tuyo
				"user1", "volume5", "title nuevo", "description test", 2192, "newspaper2", false, IllegalArgumentException.class
			}, {
				//Editar un volumen con titulo en blanco
				"user1", "volume5", "", "description test", 2192, "newspaper2", false, IllegalArgumentException.class
			}, {
				//Editar un volumen con descripcion en blanco
				"user1", "volume5", "title", "", 2192, "newspaper2", false, IllegalArgumentException.class
			}, {
				//Editar un volumen por un customer
				"customer1", "volume5", "title nuevo", "description test", 2192, "newspaper2", false, IllegalArgumentException.class
			}, {
				//Editar un volumen que no es tuyo comprobando el list
				"user1", "volume5", "title nuevo", "description test", 2192, "newspaper2", true, IllegalArgumentException.class
			},

		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListEdit((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (int) testingData[i][4], super.getEntityId((String) testingData[i][5]),
				(boolean) testingData[i][6], (Class<?>) testingData[i][7]);
	}
	private void templateListEdit(final String username, final int volumeId, final String title, final String description, final int year, final int newspaperId, final boolean checkList, final Class<?> expected) {
		Volume volume;
		Collection<Volume> volumes;
		Newspaper newspaper;
		Class<?> caught;
		caught = null;
		try {
			super.authenticate(username);
			volumes = this.volumeService.myVolumes();
			newspaper = this.newspaperService.findOne(newspaperId);
			volume = this.volumeService.findOne(volumeId);
			if (checkList)
				Assert.isTrue(volumes.contains(volume));
			Assert.isTrue(volume.getNewspapers().contains(newspaper));
			if (!volume.getNewspapers().contains(newspaper))
				volume.getNewspapers().add(newspaper);
			volume.setTitle(title);
			volume.setDescription(description);
			volume.setYear(year);
			volume = this.volumeService.save(volume);
			//this.volumeService.flush();
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
