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
import domain.TabooWord;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TabooWordServiceTest extends AbstractTest {
	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	TabooWordService tabooWordService;
	
	@PersistenceContext
	EntityManager			entityManager;
	
	
	//Edit a taboo word
	@Test
	public void driveEditTabooWord() {

		final Object testingData[][] = {
			//Admin edit name, positive case
			{
				"tabooWord5", "admin", "prueba", null
			},
			//Admin edit blank name, negative case
			{
				"tabooWord5", "admin", "", javax.validation.ConstraintViolationException.class
			},
			//Admin edit null name, negative case
			{
				"tabooWord5", "admin", null, javax.validation.ConstraintViolationException.class
			},
			//Admin edit default taboo word, negative case
			{
				"tabooWord1", "admin", "prueba", java.lang.IllegalArgumentException.class
			},
			//User edit default taboo word, negative case
			{
				"tabooWord5", "user1", "prueba", java.lang.IllegalArgumentException.class
			},
			//Customer edit default taboo word, negative case
			{
				"tabooWord5", "customer1", "prueba", java.lang.IllegalArgumentException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditTabooWord((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);

	}
	
	
	public void templateEditTabooWord(final String entity, final String username, final String name, final Class<?> expected) {

		Class<?> caught;
		TabooWord tabooWord;

		caught = null;
		

		try {
			super.authenticate(username);
			tabooWord = this.tabooWordService.findOne(super.getEntityId(entity));
			tabooWord.setName(name);
			this.tabooWordService.save(tabooWord);
			this.tabooWordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
	
	//Use case 17.1 Admin manages list of taboo words
	@Test
	public void driveListTabooWord() {

		final Object testingData[][] = {
			//Admin list taboo words, positive case
			{
				"admin", null
			},
			//User list taboo words, negative case
			{
				"user1", java.lang.IllegalArgumentException.class
			},
			//Customer lists taboo words, negative case
			{
				"customer1", java.lang.IllegalArgumentException.class
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListTabooWord((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	
	
	public void templateListTabooWord(final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		

		try {
			super.authenticate(username);
			this.tabooWordService.findAll();
			this.tabooWordService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
	

}
