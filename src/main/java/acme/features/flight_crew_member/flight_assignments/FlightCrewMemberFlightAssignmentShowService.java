
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
		status = flightAssignment != null && super.getRequest().getPrincipal().hasRealm(flightCrewMember);

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
		Collection<Leg> legs;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;
		Dataset dataset;
		FlightCrewMember flightCrewMember;
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());
		if (!flightAssignment.isDraftMode()) {
			legs = List.of(flightAssignment.getLeg());
			legChoices = SelectChoices.from(legs, "LegLabel", flightAssignment.getLeg());
		} else if (!legs.contains(flightAssignment.getLeg()))
			legChoices = SelectChoices.from(legs, "LegLabel", null);
		else
			legChoices = SelectChoices.from(legs, "LegLabel", flightAssignment.getLeg());

		dataset = super.unbindObject(flightAssignment, "lastUpdate", "remarks", "draftMode");

		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);
		dataset.put("member", flightCrewMember.getIdentity().getFullName());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		boolean legHasArrive = MomentHelper.isAfter(MomentHelper.getCurrentMoment(), flightAssignment.getLeg().getScheduledArrival());
		dataset.put("legHasArrive", legHasArrive);

		super.getResponse().addData(dataset);
	}

}
