
package acme.features.flight_crew_member.flight_assignments;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.CurrentStatus;
import acme.entities.flight_assignment.Duty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flight_crew_member.AvailabilityStatus;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiService
public class FlightCrewMemberFlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment flightAssignment;
		FlightCrewMember flightCrewMember;

		masterId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(masterId);
		flightCrewMember = flightAssignment == null ? null : flightAssignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) || flightAssignment != null; // || flightAssignment.getFlightCrewMember().getId() == flightCrewMemberId;

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
		Collection<FlightCrewMember> members;
		Collection<Leg> legs;
		SelectChoices memberChoices;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		Dataset dataset;

		if (flightAssignment.isDraftMode()) {
			int airlineId = flightAssignment.getFlightCrewMember().getAirline().getId();
			members = this.repository.findAvailableFlightCrewMembersFromAirline(airlineId, AvailabilityStatus.AVAILABLE);
			if (members.isEmpty())
				members = List.of(flightAssignment.getFlightCrewMember());
			legs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());
			if (legs.isEmpty())
				legs = List.of(flightAssignment.getLeg());
		} else {
			members = List.of(flightAssignment.getFlightCrewMember());
			legs = List.of(flightAssignment.getLeg());
		}

		dataset = super.unbindObject(flightAssignment, "lastUpdate", "remarks", "draftMode");

		memberChoices = SelectChoices.from(members, "identity.fullName", flightAssignment.getFlightCrewMember());
		legChoices = SelectChoices.from(legs, "flightNumber", flightAssignment.getLeg());
		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);
		dataset.put("member", memberChoices.getSelected().getKey());
		dataset.put("members", memberChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		boolean legHasArrive = MomentHelper.isAfter(MomentHelper.getCurrentMoment(), flightAssignment.getLeg().getScheduledArrival());
		dataset.put("legHasArrive", legHasArrive);

		super.getResponse().addData(dataset);
	}

}
