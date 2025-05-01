
package acme.features.flight_crew_member.flight_assignments;

import java.util.Collection;

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
public class FlightCrewMemberFlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberFlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment flightAssignment = new FlightAssignment();
		flightAssignment.setDraftMode(true);
		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		int flightCrewMemberId;
		FlightCrewMember flightCrewMember;
		int legId;
		Leg leg;

		flightCrewMemberId = super.getRequest().getData("member", int.class);
		flightCrewMember = this.repository.findFlightCrewMemberById(flightCrewMemberId);
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLeg(leg);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks");
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Collection<FlightCrewMember> members;
		Collection<Leg> legs;

		SelectChoices memberChoices;
		SelectChoices legChoices;
		SelectChoices dutyChoices;
		SelectChoices statusChoices;

		FlightCrewMember flightCrewMember;
		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		Dataset dataset;
		int airlineId = flightCrewMember.getAirline().getId();

		members = this.repository.findAvailableFlightCrewMembersFromAirline(airlineId, AvailabilityStatus.AVAILABLE);
		legs = this.repository.findUncompletedLegs(MomentHelper.getCurrentMoment());

		memberChoices = SelectChoices.from(members, "identity.fullName", null);
		legChoices = SelectChoices.from(legs, "flightNumber", null);
		dutyChoices = SelectChoices.from(Duty.class, null);
		statusChoices = SelectChoices.from(CurrentStatus.class, null);

		dataset = super.unbindObject(flightAssignment, "draftMode");
		dataset.put("member", memberChoices.getSelected().getKey());
		dataset.put("members", memberChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);

		super.getResponse().addData(dataset);
	}
}
