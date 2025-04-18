
package acme.features.flight_crew_member.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		ActivityLog activityLog;
		masterId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(masterId);
		status = !activityLog.getFlightAssignment().isDraftMode() && super.getRequest().getPrincipal().hasRealm(activityLog.getFlightAssignment().getFlightCrewMember()) && activityLog != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog activityLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findActivityLogById(id);
		super.getBuffer().addData(activityLog);
	}

	@Override
	public void bind(final ActivityLog activityLog) {
		int activityLogId;
		FlightAssignment flightAssignment;
		activityLogId = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentByActivityLogId(activityLogId);
		activityLog.setFlightAssignment(flightAssignment);
		activityLog.setRegistrationMoment(MomentHelper.getCurrentMoment());
		super.bindObject(activityLog, "typeOfIncident", "description", "severityLevel");
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		int masterId = super.getRequest().getData("masterId", int.class);
		FlightAssignment flightAssignment = this.repository.findFlightAssignmentById(masterId);
		boolean legHasArrive = MomentHelper.isAfter(MomentHelper.getCurrentMoment(), flightAssignment.getLeg().getScheduledArrival());
		super.state(legHasArrive, "*", "flight-crew-member.activity-log.validation.create");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "description", "severityLevel");
		super.getResponse().addData(dataset);
	}
}
