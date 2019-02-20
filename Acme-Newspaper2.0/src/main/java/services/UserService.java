
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.UserRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;
import domain.Volume;
import forms.UserForm;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------

	@Autowired
	UserRepository	userRepository;
	
	@Autowired
	MessageFolderService messageFolderService;
	


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public UserService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public User create() {
		User result;
		UserAccount userAccount;
		Authority authority;
		Collection<Article> articles;
		Collection<User> followers;
		Collection<User> followed;
		Collection<Chirp> chirps;
		Collection<Newspaper> newspapers;

		result = new User();
		userAccount = new UserAccount();
		authority = new Authority();
		articles = new ArrayList<>();
		followers = new ArrayList<>();
		followed = new ArrayList<>();
		chirps = new ArrayList<>();
		newspapers = new ArrayList<>();

		authority.setAuthority(Authority.USER);
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		result.setArticles(articles);
		result.setFollowers(followers);
		result.setFollowed(followed);
		result.setChirps(chirps);
		result.setNewspapers(newspapers);
		return result;

	}

	//Save	------------------------------------------------------------
	public User save(final User user) {
		Assert.notNull(user);
		User result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (user.getId() == 0) {
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(user.getUserAccount().getPassword(), null);
			user.getUserAccount().setPassword(passwordHash);
		}
		result = this.userRepository.save(user);
		Assert.notNull(result);
		
		if(user.getId() == 0)
		
			this.messageFolderService.createDefaultMessageFolder(result);
		
		return result;
	}

	//Delte	------------------------------------------------------------
	public void delete(final User user) {
		Assert.notNull(user);
		Assert.isTrue(user.getId() != 0);
		this.userRepository.delete(user);

	}

	public Collection<User> findAll() {
		Collection<User> result;

		result = this.userRepository.findAll();
		return result;
	}

	public User findOne(final int userId) {
		Assert.isTrue(userId != 0);
		User result;
		result = this.userRepository.findOne(userId);

		return result;
	}

	// Other business methods -------------------------------------------------

	public User findByPrincipal() {
		User result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.userRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority("USER");

		Assert.isTrue(authorities.contains(auth));
	}

	//Follow or unfollow another user.

	public void followUser(final User user) {
		Assert.notNull(user);
		this.checkPrincipal();

		User userConnected;
		userConnected = this.findByPrincipal();
		Assert.isTrue(!(userConnected.getFollowed().contains(user)));

		userConnected.getFollowed().add(user);
		user.getFollowers().add(userConnected);

	}

	public void unFollowUser(final User user) {
		Assert.notNull(user);
		this.checkPrincipal();

		User userConnected;
		userConnected = this.findByPrincipal();

		Assert.isTrue(userConnected.getFollowed().contains(user));

		userConnected.getFollowed().remove(user);
		user.getFollowers().remove(userConnected);

	}


	// Reconstruct ----------------------------------------------------------------
	@Autowired
	private Validator	validator;


	public UserForm reconstruct(final UserForm userForm, final BindingResult binding) {

		UserForm result = null;
		User user;
		user = userForm.getUser();

		if (user.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			Collection<Article> articles;
			Collection<User> followers;
			Collection<User> followed;
			Collection<Chirp> chirps;
			Collection<Newspaper> newspapers;
			Collection<Volume> volumes;

			userAccount = userForm.getUser().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.USER);
			userAccount.addAuthority(authority);
			userForm.getUser().setUserAccount(userAccount);
			articles = new ArrayList<>();
			followers = new ArrayList<>();
			followed = new ArrayList<>();
			chirps = new ArrayList<>();
			newspapers = new ArrayList<>();
			volumes = new ArrayList<>();
			userForm.getUser().setArticles(articles);
			userForm.getUser().setFollowers(followers);
			userForm.getUser().setFollowed(followed);
			userForm.getUser().setChirps(chirps);
			userForm.getUser().setNewspapers(newspapers);
			userForm.getUser().setVolumes(volumes);
			result = userForm;

		} else {

			user = this.userRepository.findOne(userForm.getUser().getId());
			userForm.getUser().setId(user.getId());
			userForm.getUser().setVersion(user.getVersion());
			userForm.getUser().setUserAccount(user.getUserAccount());
			userForm.getUser().setArticles(user.getArticles());
			userForm.getUser().setFollowed(user.getFollowed());
			userForm.getUser().setFollowers(user.getFollowers());
			result = userForm;
		}

		this.validator.validate(result, binding);

		return result;

	}

	public void flush() {
		this.userRepository.flush();
	}
}
