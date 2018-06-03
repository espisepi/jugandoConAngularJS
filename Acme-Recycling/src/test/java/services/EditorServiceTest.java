
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Editor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class EditorServiceTest extends AbstractTest {

	// Supporting services ---------------------------------------------------
	@Autowired
	private EditorService	editorService;

	@PersistenceContext
	EntityManager			entityManager;


	//Login 
	@Test
	public void driveLoginEditor() {

		final Object testingData[][] = {
			//Editor is registered
			{
				"editor1", null
			},
			//Editor isn't registered
			{
				"editorNoRegistrado", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateLoginEditor((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	public void templateLoginEditor(final String username, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {
			this.authenticate(username);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
	}

	//Test caso de uso 8.a: Crear cuentas para nuevos editores
	@Test
	public void driverCreateAndSave() {

		final Object testingData[][] = {
			{
				//Registrar editor correctamente(poniendo teléfono!=null)
				"editortest1", "passwordtest1", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", null
			}, {
				//Registrar editor incorrectamente con name en blanco
				"editortest2", "passwordtest2", "", "ternero", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar editor con surname en blanco
				"editortest3", "passwordtest3", "miguel", "", "calle Huertas nº 3", "662657322", "Email@email.com", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar editor incorrectamente con un email no válido
				"editortest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email", "SEVILLA", javax.validation.ConstraintViolationException.class
			}, {
				//Registrar editor incorrectamente ya que está registrado
				"editortest4", "passwordtest4", "miguel", "ternero", "calle Huertas nº 3", "662657322", "Email@gmail.com", "SEVILLA", org.springframework.dao.DataIntegrityViolationException.class
			}, {
				//Registrar editor correctamente con número de teléfono vacío
				"editortest9", "passwordtest9", "name1", "surname1", "calle Huertas nº 3", "662657322", "maria@gmail.com", "SEVILLA", null

			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateCreateAndSave((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);
	}
	private void templateCreateAndSave(final String username, final String password, final String name, final String surname, final String postalAdress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Editor editor;
		UserAccount userAccount;

		caught = null;
		try {
			editor = this.editorService.create();
			editor.setName(name);
			editor.setSurname(surname);
			editor.setAddress(postalAdress);
			editor.setPhone(phone);
			editor.setEmail(email);
			editor.setProvince(province);
			userAccount = editor.getUserAccount();
			userAccount.setUsername(username);
			userAccount.setPassword(password);
			editor.setUserAccount(userAccount);
			editor = this.editorService.save(editor);
			this.editorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);

	}

	//Test caso de uso extra: Edit personal data of editor
	@Test
	public void driverEditeditor() {

		final Object testingData[][] = {
			{
				//Edito mi nombre
				"editor1", "editor1 name edited", "surname editor 1", "Adress editor 1", "+34622222222", "editor1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mis apellidos
				"editor1", "editor 1", "surname editor 1 edited", "Adress user 1", "+34622222222", "editor1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi dirección
				"editor1", "editor 1", "surname editor 1", "Adress editor 1 edited", "+34622222222", "editor1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi email
				"editor1", "editor 1", "surname editor 1", "Adress editor 1", "+34622222222", "carrieEDITEDr1@acmerecycling.com", "SEVILLA", null
			}, {
				//Edito mi nombre por uno en blanco
				"editor1", "", "surname editor 1", "Adress editor 1", "+34622222222", "editor1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mis apellidos por uno en blanco
				"editor1", "editor 1", "", "Adress editor 1", "+34622222222", "editor1@acmerecycling.com", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno que no cumple el formato
				"editor1", "editor 1", "surname editor 1", "Adress editor 1", "+34622222222", "editor1", "SEVILLA", ConstraintViolationException.class
			}, {
				//Edito mi email por uno en blanco
				"editor1", "", "surname editor 1", "Adress editor 1", "+34622222222", "", "SEVILLA", ConstraintViolationException.class
			}
		};
		for (int i = 0; i < testingData.length; i++)
			this.templateEditeditor(super.getEntityId((String) testingData[i][0]), (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}
	private void templateEditeditor(final int userNameId, final String name, final String surname, final String Adress, final String phone, final String email, final String province, final Class<?> expected) {
		Class<?> caught;
		Editor editor;
		editor = this.editorService.findOne(userNameId);

		caught = null;
		try {
			super.authenticate(editor.getUserAccount().getUsername());
			editor.setName(name);
			editor.setSurname(surname);
			editor.setAddress(Adress);
			editor.setPhone(phone);
			editor.setEmail(email);
			editor.setProvince(province);
			editor = this.editorService.save(editor);
			this.unauthenticate();
			this.editorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			//Se borra la cache para que no salte siempre el error del primer objeto que ha fallado en el test
			this.entityManager.clear();

		}

		this.checkExceptions(expected, caught);
	}
}
