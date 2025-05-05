
package acme.features.flight_crew_member.flight_assignments;

import java.util.Collection;
import java.util.Date;
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
public class FlightCrewMemberFlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		status = super.getRequest().getPrincipal().hasRealm(flightCrewMember) && flightAssignment != null;

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
	public void bind(final FlightAssignment flightAssignment) {
		FlightCrewMember flightCrewMember;
		int legId;
		Leg leg;

		flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks");
		flightAssignment.setFlightCrewMember(flightCrewMember);
		flightAssignment.setLeg(leg);
		flightAssignment.setLastUpdate(MomentHelper.getCurrentMoment());
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		if (flightAssignment.getCurrentStatus() != null && flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			if (flightAssignment.getDuty() == Duty.PILOT || flightAssignment.getDuty() == Duty.COPILOT) {
				Collection<FlightAssignment> assignmentsOfLeg = this.repository.findFlightAssignmentsByLegId(flightAssignment.getLeg().getId());
				for (FlightAssignment fa : assignmentsOfLeg)
					if (fa.getDuty() == Duty.PILOT && flightAssignment.getDuty() == Duty.PILOT || fa.getDuty() == Duty.COPILOT && flightAssignment.getDuty() == Duty.COPILOT) {
						super.state(false, "duty", "flight-crew-member.flight-assignment.validation.duty.publish");
						break;
					}
			}
			if (flightAssignment.getFlightCrewMember().getAvailabilityStatus() != AvailabilityStatus.AVAILABLE)
				super.state(false, "members", "flight-crew-member.flight-assignment.validation.availability-status.publish");
			Collection<FlightAssignment> currentUserAssignments = this.repository.findPublishedUncompletedFlightAssignmentsByFlightCrewMemberId(MomentHelper.getCurrentMoment(), flightAssignment.getFlightCrewMember().getId());
			for (FlightAssignment fa : currentUserAssignments)
				if (fa.getLeg().getFlightNumber() == flightAssignment.getLeg().getFlightNumber()) {
					super.state(false, "leg", "flight-crew-member.flight-assignment.validation.flight-number.publish");
					break;
				}
			Date newDeparture = flightAssignment.getLeg().getScheduledDeparture();
			Date newArrival = flightAssignment.getLeg().getScheduledArrival();
			Collection<FlightAssignment> overlapping = this.repository.findOverlappingPublishedFlightAssignments(flightAssignment.getFlightCrewMember().getId(), newDeparture, newArrival);
			if (!overlapping.isEmpty())
				super.state(false, "leg", "flight-crew-member.flight-assignment.validation.overlapping-leg.publish");
			if (flightAssignment.getCurrentStatus() == CurrentStatus.PENDING)
				super.state(false, "currentStatus", "flight-crew-member.flight-assignment.validation.current-status.publish");
		}
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
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

		if (legs.isEmpty())
			legs = List.of(flightAssignment.getLeg());

		legChoices = SelectChoices.from(legs, "LegLabel", flightAssignment.getLeg());
		dutyChoices = SelectChoices.from(Duty.class, flightAssignment.getDuty());
		statusChoices = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());

		dataset = super.unbindObject(flightAssignment, "lastUpdate", "remarks", "draftMode");
		dataset.put("member", flightCrewMember.getIdentity().getFullName());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);
		dataset.put("currentStatus", statusChoices);

		super.getResponse().addData(dataset);
	}

}
