
package acme.features.authenticated.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.manager.Manager;

@GuiService
public class AuthenticatedManagerCreateService extends AbstractGuiService<Authenticated, Manager> {

	@Autowired
	private AuthenticatedManagerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Manager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Manager manager;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		manager = new Manager();
		manager.setUserAccount(userAccount);

		super.getBuffer().addData(manager);
	}

	@Override
	public void bind(final Manager manager) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);
		manager.setAirline(airline);

		super.bindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "picture");
	}

	@Override
	public void validate(final Manager manager) {
		;
	}

	@Override
	public void perform(final Manager manager) {
		this.repository.save(manager);
	}

	@Override
	public void unbind(final Manager manager) {
		Collection<Airline> airlines;
		SelectChoices airlineChoices;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "name", manager.getAirline());

		dataset = super.unbindObject(manager, "identifierNumber", "yearsOfExperience", "dateOfBirth", "picture");

		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
