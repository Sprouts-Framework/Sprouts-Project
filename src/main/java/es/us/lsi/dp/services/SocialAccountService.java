package es.us.lsi.dp.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.Validator;

import domain.Customer;
import domain.SocialIdentity;

import es.us.lsi.dp.domain.BaseActor;
import es.us.lsi.dp.domain.DomainObject;
import es.us.lsi.dp.domain.SocialAccount;
import es.us.lsi.dp.domain.UserAccount;
import es.us.lsi.dp.forms.BaseRegistrationForm;
import es.us.lsi.dp.repositories.SocialAccountRepository;
import es.us.lsi.dp.services.contracts.CreateService;
import es.us.lsi.dp.validation.contracts.BusinessRule;

@Service
@Transactional
public class SocialAccountService extends AbstractService<SocialAccount, SocialAccountRepository> implements CreateService<SocialAccount> {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private SocialIdentityService socialIdentityService;

	@Override
	public Class<? extends DomainObject> getEntityClass() {
		return SocialAccount.class;
	}

	@Override
	public void createBusinessRules(List<BusinessRule<SocialAccount>> rules, List<Validator> validators) {

	}

	@Override
	public void beforeCreating(SocialAccount validable, List<String> context) {

	}

	@Override
	public void beforeCommitingCreate(SocialAccount validable, List<String> context) {

	}

	@Override
	public void afterCommitingCreate(int id) {

	}

	// Other methods -------------------------

	public SocialAccount findSocialAccount(String providerId, String userId) {
		Assert.notNull(providerId);
		Assert.notNull(userId);

		SocialAccount result;

		result = repository.findSocialAccount(providerId, userId);

		return result;
	}

	public Customer createCustomerAndSocialAccount(Connection<?> connection, UserProfile profile) {
		SocialAccount socialAccount;
		socialAccount = new SocialAccount();
		socialAccount.setProviderId(connection.getKey().getProviderId());
		socialAccount.setUserId(connection.getKey().getProviderUserId());
		SocialAccount socialAccountAux = repository.save(socialAccount);

		SocialIdentity socialIdentity = new SocialIdentity();
		socialIdentity.setNick(profile.getUsername());
		socialIdentity.setPicture(connection.getImageUrl());
		socialIdentity.setHomePage(connection.getProfileUrl());
		socialIdentity.setSocialNetwork(connection.getKey().getProviderId());
		int socialIdentityId = socialIdentityService.save(socialIdentity);
		socialIdentity = socialIdentityService.findById(socialIdentityId);

		BaseRegistrationForm customerForm = customerRegistrationService.create();
		customerForm.setCheckBox(true);
		// customerForm.setContactPhone("None");
		customerForm.setName(profile.getFirstName());
		if (profile.getLastName() != null)
			customerForm.setSurname(profile.getLastName());
		else
			customerForm.setSurname("None");
		String password = RandomStringUtils.random(32, true, true);
		customerForm.setPassword(password);
		customerForm.setPassword2(password);
		if (connection.getKey().getProviderId().equals("google"))
			customerForm.setUsername(profile.getEmail().split("@")[0]);
		else
			customerForm.setUsername(profile.getUsername());

		int customerId = customerRegistrationService.save(customerForm);
		customerRegistrationService.afterCommitingCreate(customerId);
		Customer customer = customerService.findById(customerId);
		customer.setSocialIdentity(socialIdentity);
		customerId = customerService.save(customer);
		customer = customerService.findById(customerId);

		UserAccount userAccount = customer.getUserAccount();
		Collection<SocialAccount> socialAccounts = new ArrayList<SocialAccount>();
		socialAccounts.add(socialAccountAux);
		userAccount.setSocialAccounts(socialAccounts);
		Collection<BaseActor> actors = new ArrayList<BaseActor>();
		actors.add(customer);
		userAccount.setActors(actors);
		userAccountService.save(userAccount);

		return customer;
	}

	public void addSocialAccount(Customer customer, UserProfile profile, Connection<?> connection) {
		SocialAccount socialAccount;
		socialAccount = new SocialAccount();
		socialAccount.setProviderId(connection.getKey().getProviderId());
		socialAccount.setUserId(connection.getKey().getProviderUserId());
		SocialAccount socialAccountAux = repository.save(socialAccount);

		UserAccount userAccount = customer.getUserAccount();
		userAccount.getSocialAccounts().add(socialAccountAux);
		userAccountService.save(userAccount);
	}

	public void updateCustomer(Customer customer, UserProfile profile, Connection<?> connection) {
		customer.setName(profile.getFirstName());
		if (profile.getLastName() != null)
			customer.setSurname(profile.getLastName());

		SocialIdentity socialIdentity = customer.getSocialIdentity() == null ? new SocialIdentity() : customer.getSocialIdentity();
		socialIdentity.setNick(profile.getUsername());
		socialIdentity.setPicture(connection.getImageUrl());
		socialIdentity.setHomePage(connection.getProfileUrl());
		socialIdentity.setSocialNetwork(connection.getKey().getProviderId());
		int socialIdentityId = socialIdentityService.save(socialIdentity);
		socialIdentity = socialIdentityService.findById(socialIdentityId);
		customer.setSocialIdentity(socialIdentity);

		UserAccount userAccount = customer.getUserAccount();
		if (connection.getKey().getProviderId().equals("google"))
			userAccount.setUsername(profile.getEmail().split("@")[0]);
		else
			userAccount.setUsername(profile.getUsername());

		userAccountService.save(userAccount);
		customerService.save(customer);
	}

	public Long countSocialAccount() {
		Long result;
		UserAccount userAccount;

		userAccount = SignInService.getPrincipalOrNull();
		Assert.notNull(userAccount);

		result = repository.countSocialAccount(userAccount.getId());

		return result;
	}

}
