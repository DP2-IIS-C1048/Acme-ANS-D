
package acme.features.authenticated.fligh_assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.FlightAssignment;

@GuiService
public class AuthenticatedFlightAssignmentListService extends AbstractGuiService<Authenticated, FlightAssignment> {

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
		Collection<FlightAssignment> flightAssignments;
		flightAssignments = this.repository.findPublishedFlightAssignments();
		super.getBuffer().addData(flightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "flightCrewMember.identity.fullName", "currentStatus", "leg.legLabel");
		super.getResponse().addData(dataset);
	}

}
