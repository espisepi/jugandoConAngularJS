
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
import domain.Admin;
import domain.CleanPoint;
import domain.GPS;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CleanPointServiceTest extends AbstractTest {

	// Supporting services ----------------------------------------------------

	@Autowired
	CleanPointService	cleanPointService;

	@Autowired
	AdminService		adminService;

	@PersistenceContext
	EntityManager		entityManager;


	//Crate and save cleanPoint
	@Test
	public void driverCreateAndSave() {
		final Collection<GPS> listGPS;
		final Iterator<GPS> iterator;
		GPS gpsOk;

		listGPS = this.createAllGPSForTesting();
		iterator = listGPS.iterator();
		gpsOk = iterator.next();
		final Object testingData[][] = {
			{
				//Se crea un punto limpio correctamente con un gps correcto
				"admin", "addres 1", "", "SEVILLA", gpsOk, true, null
			}, {
				//Se crea un punto limpio incorrectamente porque la dirección es blanco
				"admin", "", "", "SEVILLA", gpsOk, true, javax.validation.ConstraintViolationException.class

			}, {
				//Se crea un punto limpio incorrectamente porque el teléfono no cumple el patrón
				"admin", "adress1", "1", "SEVILLA", gpsOk, true, javax.validation.ConstraintViolationException.class

			}, {
				//Se crea un punto limpio incorrectamente con una credit card no válida ||  Name null
				"admin", "adress1", "", "CORDOBA", iterator.next(), true, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un punto limpio incorrectamente con una credit card no válida ||  Longitud erronea
				"admin", "adress1", "", "JAEN", iterator.next(), true, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un punto limpio incorrectamente con una credit card no válida ||  Latitud erronea
				"admin", "adress1", "652582643", "CORDOBA", iterator.next(), true, javax.validation.ConstraintViolationException.class
			}, {
				//Se crea un punto limpio correctamente en mobile null
				"admin", "adress1", "652582643", "CORDOBA", gpsOk, false, null
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (GPS) testingData[i][4], (boolean) testingData[i][5], (Class<?>) testingData[i][6]);
	}
	private void templateCreateAndSave(final String username, final String address, final String phone, final String province, final GPS location, final boolean mobile, final Class<?> expected) {
		CleanPoint cleanPoint;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			cleanPoint = this.cleanPointService.create();

			cleanPoint.setAddress(address);
			cleanPoint.setLocation(location);
			cleanPoint.setPhone(phone);
			cleanPoint.setProvince(province);
			cleanPoint.setMobile(mobile);

			cleanPoint = this.cleanPointService.save(cleanPoint);

			this.cleanPointService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}
		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	//	//Other Methods additionals---------------------------------------------------------------------------------------
	private Collection<GPS> createAllGPSForTesting() {
		final Collection<GPS> result;
		final GPS gpsOK;
		final GPS gpsNameNull;
		final GPS gpsWrongLongitude;
		final GPS gpsWrongLatitude;
		final GPS gpsWrongLongitudeAndLatitude;

		result = new ArrayList<GPS>();

		gpsOK = new GPS();
		gpsOK.setName("hola");
		gpsOK.setLongitude(-5.98);
		gpsOK.setLatitude(37.35);
		result.add(gpsOK);

		gpsNameNull = new GPS();
		gpsNameNull.setName(null);
		gpsNameNull.setLongitude(-5.98);
		gpsNameNull.setLatitude(37.35);
		result.add(gpsNameNull);

		gpsWrongLongitude = new GPS();
		gpsWrongLongitude.setName("Hola");
		gpsWrongLongitude.setLongitude(181.1);
		gpsWrongLongitude.setLatitude(37.35);
		result.add(gpsWrongLongitude);

		gpsWrongLatitude = new GPS();
		gpsWrongLatitude.setName("Hola");
		gpsWrongLatitude.setLatitude(91.1);
		gpsWrongLatitude.setLongitude(57.9);
		result.add(gpsWrongLatitude);

		gpsWrongLongitudeAndLatitude = new GPS();
		gpsWrongLongitudeAndLatitude.setName("Hola");
		gpsWrongLongitudeAndLatitude.setLatitude(91.1);
		gpsWrongLongitudeAndLatitude.setLongitude(181.1);

		return result;
	}

	//Remove a cleanPoint
	@Test
	public void driverDelete() {
		final Object testingData[][] = {
			{
				//Se elimina el cleanPoint4 correctamente 
				"admin", "cleanPoint4", null
			}, {
				//Se elimina el cleanPoint4 incorrectamente
				"admin", "cleanPoint5", IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateDelete((String) testingData[i][0], super.getEntityId((String) testingData[i][1]), (Class<?>) testingData[i][2]);
	}
	private void templateDelete(final String username, final int cleanPointId, final Class<?> expected) {
		CleanPoint cleanPoint;
		Class<?> caught;

		caught = null;
		try {
			super.authenticate(username);
			cleanPoint = this.cleanPointService.findOne(cleanPointId);
			this.cleanPointService.delete(cleanPoint);

			this.cleanPointService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

		super.unauthenticate();
	}

	//Listar todos los puntos limpios de mi sistema
	@Test
	public void driverListCleanPoints() {
		final Object testingData[][] = {
			{
				//El admin lista las 5 palabras tabú que hay en el sistema
				"admin", 6, null
			}, {
				//No hay un total de 3 si no de 5 
				"admin", 3, IllegalArgumentException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateListTabooWords(super.getEntityId((String) testingData[i][0]), (int) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	private void templateListTabooWords(final int usernameId, final int size, final Class<?> expected) {
		Class<?> caught;
		Admin adminConnected;
		adminConnected = this.adminService.findOne(usernameId);
		Collection<CleanPoint> cleanPoints;

		caught = null;
		try {
			super.authenticate(adminConnected.getUserAccount().getUsername());
			cleanPoints = this.cleanPointService.findAll();
			Assert.isTrue(cleanPoints.size() == size);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}
		this.checkExceptions(expected, caught);
	}

	//Edit cleanPoint
	@Test
	public void driveEditcleanPoint() {
		final Collection<GPS> listGPS;
		final Iterator<GPS> iterator;
		GPS gpsOk;

		listGPS = this.createAllGPSForTesting();
		iterator = listGPS.iterator();
		gpsOk = iterator.next();

		final Object testingData[][] = {
			//Editar el punto limpio 4 sin problema
			{
				"cleanPoint4", "admin", "address", "", "SEVILLA", gpsOk, false, null
			}, {
				//Editar el punto 6 incorrectamente dejando la dirección en blanco
				"cleanPoint6", "admin", "", "", "SEVILLA", gpsOk, false, javax.validation.ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditNewscast((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (GPS) testingData[i][5], (boolean) testingData[i][6],
				(Class<?>) testingData[i][7]);

	}
	public void templateEditNewscast(final String entity, final String username, final String address, final String phone, final String province, final GPS location, final boolean mobile, final Class<?> expected) {

		Class<?> caught;
		CleanPoint cleanpoint;

		caught = null;

		try {
			super.authenticate(username);
			cleanpoint = this.cleanPointService.findOne(super.getEntityId(entity));
			cleanpoint.setAddress(address);
			cleanpoint.setLocation(location);
			cleanpoint.setPhone(phone);
			cleanpoint.setProvince(province);
			cleanpoint.setMobile(mobile);

			this.cleanPointService.save(cleanpoint);
			this.cleanPointService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);

	}
}
