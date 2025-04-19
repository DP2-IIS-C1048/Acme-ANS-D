
package acme.features.authenticated.flight_crew_member;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberUpdateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedFlightCrewMemberRepository repository;

	// AbstractService interface ----------------------------------------------ç


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember object;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		object = this.repository.findFlightCrewMemberByUserAccountId(userAccountId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final FlightCrewMember object) {
		assert object != null;

		super.bindObject(object, "company", "sector"); //TODO
	}

	@Override
	public void validate(final FlightCrewMember object) {
		assert object != null;
	}

	@Override
	public void perform(final FlightCrewMember object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final FlightCrewMember object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "company", "sector"); //TODO
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
