
package acme.features.authenticated.fligh_assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.FlightAssignment;

@GuiService
public class AuthenticatedFlightAssignmentShowService extends AbstractGuiService<Authenticated, FlightAssignment> {

	@Autowired
	private AuthenticatedFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(id);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;
		dataset = super.unbindObject(flightAssignment, "duty", "currentStatus", "flightCrewMember.identity.fullName", "lastUpdate", "remarks", "leg.legLabel");
		super.getResponse().addData(dataset);
	}

}
