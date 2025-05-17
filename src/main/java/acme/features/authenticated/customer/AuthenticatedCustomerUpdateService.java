
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.customer.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer customer;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		customer = this.repository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(customer);
	}

	@Override
	public void bind(final Customer customer) {

		super.bindObject(customer, "identifier", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");
	}

	@Override
	public void validate(final Customer customer) {
		if (customer.getIdentifier() != null && customer.getIdentifier().length() >= 2 && customer.getIdentity() != null && customer.getIdentity().getName() != null && customer.getIdentity().getName().length() >= 1
			&& customer.getIdentity().getSurname() != null && customer.getIdentity().getSurname().length() >= 1) {

			String name = customer.getIdentity().getName();
			String surname = customer.getIdentity().getSurname();
			String identifier = customer.getIdentifier();

			boolean validIdentifier = name.charAt(0) == identifier.charAt(0) && surname.charAt(0) == identifier.charAt(1);

			super.state(validIdentifier, "identifier", "acme.validation.customer.invalid-identifierNumber.message");
		}
	}

	@Override
	public void perform(final Customer customer) {
		this.repository.save(customer);
	}

	@Override
	public void unbind(final Customer customer) {
		Dataset dataset;

		dataset = super.unbindObject(customer, "identifier", "phoneNumber", "physicalAddress", "city", "country", "earnedPoints");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
