
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Validacion;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ValidacionServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------
	@Autowired
	private ValidacionService	validacionService;

	@PersistenceContext
	EntityManager				entityManager;


	@Test
	public void testSave() {
		final Validacion val = this.validacionService.create();
		val.setTextMaxLength("testing");
		val.setNumberMax(14);
		val.setNumberMin(2);
		this.validacionService.save(val);
		this.entityManager.flush();
	}

}
