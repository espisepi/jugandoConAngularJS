package services;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.ConfigurationSystem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationSystemServiceTest extends AbstractTest{
	
	// Supporting services ----------------------------------------------------
	
	@Autowired
	private ConfigurationSystemService configurationSystemService;

	@PersistenceContext
	EntityManager entityManager;
	
	
	//B17.1 A admin manage a list of taboo words
	@Test
	public void driveListConfiguration() {

		final Object testingData[][] = {
			//admin trys introducing a taboo word, positive case
			{
				"admin", 4, null
			}
			
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListConfiguration((String)testingData[i][0], (Integer) testingData[i][1], (Class<?>)testingData[i][2]);

	}
	
	public void templateListConfiguration(String username, Integer numWords, Class<?> expected) {
		
		Class<?> caught;
		ConfigurationSystem configurationSystem;

		caught = null;
		configurationSystem = this.configurationSystemService.findConfigurationSystem();
		

		try {
			this.authenticate(username);
			Assert.isTrue(configurationSystem.getTabooWords().size()==numWords);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
	}
	
//	//B17.1 A admin manage a list of taboo words
//	@Test
//	public void driveEditConfiguration() {
//
//		final Object testingData[][] = {
//			//admin trys introducing a taboo word, positive case
//			{
//				"admin", "configurationSystem", "palabraPrueba", null
//			}, 
//			//User trys introducing a taboo word, negative case
//			{
//				"user1", "configurationSystem", "palabraPrueba", IllegalArgumentException.class
//			}, 
//			//User trys introducing a blank taboo word, negative case
//			{
//				"admin", "configurationSystem", "", IllegalArgumentException.class
//			},
//			////User trys introducing a null taboo word, negative case
//			{
//				"admin", "configurationSystem", null, IllegalArgumentException.class
//			}
//			
//		};
//
//		for (int i = 0; i < testingData.length; i++)
//			this.templateEditConfiguration((String)testingData[i][0], (String)testingData[i][1], (String)testingData[i][2], (Class<?>)testingData[i][3]);
//
//	}
//	
//	
//	public void templateEditConfiguration(String username, String entity, String tabooWord, Class<?> expected) {
// 
//		Class<?> caught;
//		ConfigurationSystem configurationSystem;
//
//		caught = null;
//		configurationSystem = this.configurationSystemService.findOne(super.getEntityId(entity));
//		
//
//		try {
//			this.authenticate(username);
//			this.configurationSystemService.addTabooWord(configurationSystem, tabooWord);
//			this.unauthenticate();
//		} catch (final Throwable oops) {
//			caught = oops.getClass();
//			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
//			this.entityManager.clear();
//		}
//
//		this.checkExceptions(expected, caught);
//	}
}
