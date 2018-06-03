
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
import domain.Finder;
import domain.Material;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FinderServiceTest extends AbstractTest {

	@Autowired
	FinderService	finderService;

	@Autowired
	BuyerService	buyerService;

	@Autowired
	MaterialService	materialService;

	@PersistenceContext
	EntityManager	entityManager;


	//Caso de uso 6.c: Manejar un buscador sobre los materiales a comprar.
	@Test
	public void driverSearch() {
		final Object testingData[][] = {
			{
				//Se encuentra correctamente el material1
				"buyer1", "finder1", "material1", "", 0.0, 100.0, null
			}, {
				//Se encuentra incorrectamente el material1 porque su precio es de 2.0 y hemos filtrado como precio mas bajo 2.1
				"buyer1", "finder1", "material1", "", 2.1, 100.0, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateSearch((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), super.getEntityId((String) testingData[i][2]), (String) testingData[i][3], (double) testingData[i][4], (double) testingData[i][5],
				(Class<?>) testingData[i][6]);
	}

	private void templateSearch(final String username, final int finderId, final int materialId, final String keyWord, final double lowPrice, final double highPrice, final Class<?> expected) {
		Class<?> caught;
		Finder finder;
		Material material;
		Collection<Material> materials;

		caught = null;
		try {
			super.authenticate(username);
			finder = this.finderService.findOne(finderId);
			finder.setKeyWord(keyWord);
			finder.setLowPrice(lowPrice);
			finder.setHighPrice(highPrice);
			finder = this.finderService.search(finder);

			material = this.materialService.findOne(materialId);
			materials = finder.getMaterials();
			Assert.isTrue(materials.contains(material));

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
