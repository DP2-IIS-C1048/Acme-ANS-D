
package acme.features.flight_crew_member.dashboard;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.CurrentStatus;
import acme.entities.leg.Leg;
import acme.forms.FlightCrewMemberDashboard;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiService
public class FlightCrewMemberDashboardShowService extends AbstractGuiService<FlightCrewMember, FlightCrewMemberDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();
		int flightCrewMemberId;
		flightCrewMemberId = flightCrewMember.getId();
		Integer lastLegId;
		Integer minAssignmentsLastMonth;
		Integer maxAssignmentsLastMonth;
		List<String> lastFiveDestination = this.repository.findLastFiveDestinations(flightCrewMemberId);
		Integer legsWithIncidentSeverity0To3 = this.repository.countLegsWithIncidentSeverityXToY(0, 3);
		Integer legsWithIncidentSeverity4To7 = this.repository.countLegsWithIncidentSeverityXToY(4, 7);
		Integer legsWithIncidentSeverity8To10 = this.repository.countLegsWithIncidentSeverityXToY(8, 10);
		List<Leg> findLastLegs = this.repository.findLastLegs(flightCrewMemberId, MomentHelper.getCurrentMoment());
		List<String> flightAssignmentsPending = this.repository.findFlightAssignmentsByStatus(flightCrewMemberId, CurrentStatus.PENDING);
		List<String> flightAssignmentsConfirmed = this.repository.findFlightAssignmentsByStatus(flightCrewMemberId, CurrentStatus.CONFIRMED);
		List<String> flightAssignmentsCancelled = this.repository.findFlightAssignmentsByStatus(flightCrewMemberId, CurrentStatus.CANCELLED);
		Double averageAssignmentsLastMonth = this.repository.findAverageAssignmentsPerActiveMonth(flightCrewMemberId);
		List<Integer> flightAssignmentsPerMonth = this.repository.countFlightAssignmentsPerMonth(flightCrewMemberId);
		if (flightAssignmentsPerMonth.isEmpty()) {
			minAssignmentsLastMonth = 0;
			maxAssignmentsLastMonth = 0;
		} else {
			minAssignmentsLastMonth = flightAssignmentsPerMonth.stream().min(Integer::compare).get().intValue();
			maxAssignmentsLastMonth = flightAssignmentsPerMonth.stream().max(Integer::compare).get().intValue();
		}
		Double stddevAssignmentsLastMonth;
		Double media = flightAssignmentsPerMonth.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);
		Double sumaCuadrados = flightAssignmentsPerMonth.stream().mapToDouble(cnt -> Math.pow(cnt - media, 2)).sum();
		stddevAssignmentsLastMonth = Math.sqrt(sumaCuadrados / flightAssignmentsPerMonth.size());
		if (!findLastLegs.isEmpty())
			lastLegId = findLastLegs.get(0).getId();
		else
			lastLegId = 0;
		List<String> lastLegFlightCrewMembers = this.repository.findLastLegFlightCrewMembers(lastLegId).stream().filter(fcm -> fcm.getId() != flightCrewMemberId).map(fcm -> fcm.getIdentity().getFullName()).collect(Collectors.toList());

		if (lastFiveDestination.size() >= 5)
			lastFiveDestination = lastFiveDestination.subList(0, 5);
		else
			lastFiveDestination = lastFiveDestination.subList(0, lastFiveDestination.size());

		FlightCrewMemberDashboard dashboard = new FlightCrewMemberDashboard();

		dashboard.setLastFiveDestinations(lastFiveDestination);
		dashboard.setLegsWithIncidentSeverity0To3(legsWithIncidentSeverity0To3);
		dashboard.setLegsWithIncidentSeverity4To7(legsWithIncidentSeverity4To7);
		dashboard.setLegsWithIncidentSeverity8To10(legsWithIncidentSeverity8To10);
		dashboard.setLastLegFlightCrewMembers(lastLegFlightCrewMembers);
		dashboard.setFlightAssignmentsPending(flightAssignmentsPending);
		dashboard.setFlightAssignmentsConfirmed(flightAssignmentsConfirmed);
		dashboard.setFlightAssignmentsCancelled(flightAssignmentsCancelled);
		dashboard.setAverageAssignmentsLastMonth(averageAssignmentsLastMonth);
		dashboard.setMinAssignmentsLastMonth(minAssignmentsLastMonth);
		dashboard.setMaxAssignmentsLastMonth(maxAssignmentsLastMonth);
		dashboard.setStdDevAssignmentsLastMonth(stddevAssignmentsLastMonth);
		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final FlightCrewMemberDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "lastFiveDestinations", "legsWithIncidentSeverity0To3", "legsWithIncidentSeverity4To7", "legsWithIncidentSeverity8To10", "lastLegFlightCrewMembers", "flightAssignmentsPending",
			"flightAssignmentsConfirmed", "flightAssignmentsCancelled", "averageAssignmentsLastMonth", "minAssignmentsLastMonth", "maxAssignmentsLastMonth", "stdDevAssignmentsLastMonth");

		super.getResponse().addData(dataset);
	}

}
