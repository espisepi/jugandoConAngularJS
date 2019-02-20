
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

import repositories.CustomerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Customer;
import domain.Subscription;
import domain.Underwrite;
import forms.CustomerForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	CustomerRepository	customerRepository;


	// Supporting services ----------------------------------------------------
	
	@Autowired
	MessageFolderService messageFolderService;

	// Constructors -----------------------------------------------------------
	public CustomerService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------
	public Customer create() {
		Customer result;
		UserAccount userAccount;
		Authority authority;
		Collection<Subscription> subscriptions;
		Collection<Underwrite> underwrites;

		result = new Customer();
		userAccount = new UserAccount();
		authority = new Authority();
		subscriptions = new ArrayList<>();
		underwrites = new ArrayList<>();

		authority.setAuthority(Authority.CUSTOMER);
		userAccount.addAuthority(authority);
		result.setUserAccount(userAccount);
		result.setSubcriptions(subscriptions);
		result.setUnderwrites(underwrites);

		return result;
	}

	//Save	------------------------------------------------------------
	public Customer save(final Customer customer) {
		Assert.notNull(customer);
		Customer result;
		Md5PasswordEncoder encoder;
		String passwordHash;

		if (customer.getId() == 0) {
			encoder = new Md5PasswordEncoder();
			passwordHash = encoder.encodePassword(customer.getUserAccount().getPassword(), null);
			customer.getUserAccount().setPassword(passwordHash);
		}
		result = this.customerRepository.save(customer);
		Assert.notNull(result);
		
		if(customer.getId() == 0)
			
			this.messageFolderService.createDefaultMessageFolder(result);
		
		return result;
	}

	//Delete	------------------------------------------------------------
	public void delete(final Customer customer) {
		Assert.notNull(customer);
		Assert.isTrue(customer.getId() != 0);
		this.customerRepository.delete(customer);
	}

	public Collection<Customer> findAll() {
		Collection<Customer> result;

		result = this.customerRepository.findAll();
		return result;
	}

	public Customer findOne(final int customerId) {
		Assert.isTrue(customerId != 0);
		Customer result;
		result = this.customerRepository.findOne(customerId);

		return result;
	}

	// Other business methods -------------------------------------------------
	public Collection<Customer> customerWithUnderwriteToVolumeId(int volumeId) {
		Collection<Customer> res;
		res = this.customerRepository.customerWithUnderwriteToVolumeId(volumeId);
		return res;
	}
	public Customer findByPrincipal() {

		Customer result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = this.customerRepository.findByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public void checkPrincipal() {

		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		final Collection<Authority> authorities = userAccount.getAuthorities();
		Assert.notNull(authorities);

		final Authority auth = new Authority();
		auth.setAuthority("CUSTOMER");

		Assert.isTrue(authorities.contains(auth));
	}


	// Reconstruct ----------------------------------------------------------------
	@Autowired
	private Validator	validator;


	public CustomerForm reconstruct(final CustomerForm customerForm, final BindingResult binding) {

		CustomerForm result = null;
		Customer customer;
		customer = customerForm.getCustomer();

		if (customer.getId() == 0) {
			UserAccount userAccount;
			Authority authority;
			Collection<Subscription> subscriptions;
			Collection<Underwrite> underwrites;

			userAccount = customerForm.getCustomer().getUserAccount();
			authority = new Authority();
			authority.setAuthority(Authority.CUSTOMER);
			userAccount.addAuthority(authority);
			customerForm.getCustomer().setUserAccount(userAccount);
			subscriptions = new ArrayList<>();
			underwrites = new ArrayList<>();

			customerForm.getCustomer().setSubcriptions(subscriptions);
			customerForm.getCustomer().setUnderwrites(underwrites);

			result = customerForm;

		} else {
			customer = this.customerRepository.findOne(customerForm.getCustomer().getId());
			customerForm.getCustomer().setId(customer.getId());
			customerForm.getCustomer().setVersion(customer.getVersion());
			customerForm.getCustomer().setUserAccount(customer.getUserAccount());
			customerForm.getCustomer().setSubcriptions(customer.getSubcriptions());
			customerForm.getCustomer().setUnderwrites(customer.getUnderwrites());

			result = customerForm;
		}

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.customerRepository.flush();
	}
}
