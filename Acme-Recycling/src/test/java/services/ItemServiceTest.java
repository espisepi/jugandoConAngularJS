
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
import domain.Item;
import domain.LabelProduct;
import domain.Recycler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ItemServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	ItemService			itemService;

	@Autowired
	LabelProductService	categoryProductService;

	@Autowired
	ManagerService		managerService;

	@PersistenceContext
	EntityManager		entityManager;

	@Autowired
	ActorService		actorService;
	
	@Autowired
	RecyclerService recyclerService;

	@Autowired
	LabelProductService	labelItemService;


	//3.e. El reciclador podrá subir sus productos(items) al sistema. Una vez que esté subido al sistema no se 
	//podrán editar sus atributos, pero sí se podrá eliminar siempre y cuando no haya un transportista asociado 
	//ya a ese producto. También hay que tener en cuenta que tampoco se podrá borrar si todo el proceso ha sido finalizado.

	@Test
	public void driverCreateAndEdit() {
		final Object testingData[][] = {
			{
				//Se edita un product correctamente, caso positivo
				"recycler1", "labelProduct1", "title test", "description test", 20.0, "http://www.photoTest.com", null
			}, {
				//No se edita el producto correctamente con el título nulo, caso negativo
				"recycler1", "labelProduct1", null, "description test", 20.0, "http://www.photoTest.com", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (String) testingData[i][2], (String) testingData[i][3], (double) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	private void templateCreateAndSave(final String username, final int labelItemId, final String title, final String description, final double quantity, final String photo, final Class<?> expected) {
		Item item;
		Class<?> caught;
		LabelProduct labelItem;

		caught = null;
		try {
			super.authenticate(username);
			item = this.itemService.create();
			labelItem = this.labelItemService.findOne(labelItemId);

			item.setTitle(title);
			item.setDescription(description);
			item.setQuantity(quantity);
			item.setPhoto(photo);
			item.setLabelProduct(labelItem);
			item = this.itemService.save(item);
			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}
	
	
	//3.e. El reciclador podrá subir sus productos(items) al sistema. Una vez que esté subido al sistema no se 
	//podrán editar sus atributos, pero sí se podrá eliminar siempre y cuando no haya un transportista asociado 
	//ya a ese producto. También hay que tener en cuenta que tampoco se podrá borrar si todo el proceso ha sido finalizado.
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Borrar un producto, caso positivo
				"recycler1", "item1", null
			}, {
				//Borrar un producto que no es suyo, caso negativo
				"recycler1", "item3", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}

	private void templateDelete(final String username, final int itemId, final Class<?> expected) {
		Item item;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			item = this.itemService.findOne(itemId);
			this.itemService.delete(item);

			this.entityManager.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	
	//3.e. El reciclador podrá subir sus productos(items) al sistema. Una vez que esté subido al sistema no se 
	//podrán editar sus atributos, pero sí se podrá eliminar siempre y cuando no haya un transportista asociado 
	//ya a ese producto. También hay que tener en cuenta que tampoco se podrá borrar si todo el proceso ha sido finalizado.
	@Test
	public void driverFindItemsByRecycler() {
		final Object testingData[][] = {
			//Encontrar los productos de ese reciclador, caso positivo
			{

				"recycler1", "item1", 2, null
			}, 
			//No encuentra los productos de ese reciclador, caso negativo
			{

				"recycler5", "item1", 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateFindItemsByRecycler((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	private void templateFindItemsByRecycler(final String recycler, final String itemEntity, final int size, final Class<?> expected) {
		Class<?> caught;
		Item item;
		Collection<Item> items;
		Recycler recyclerId;

		caught = null;
		try {
			item = this.itemService.findOne(super.getEntityId(itemEntity));
			recyclerId = this.recyclerService.findOne(super.getEntityId(recycler));
			items = this.itemService.findItemsByRecycler(recyclerId.getId());
			Assert.isTrue(items.size() == size);
			Assert.isTrue(items.contains(item));
			
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}
}
