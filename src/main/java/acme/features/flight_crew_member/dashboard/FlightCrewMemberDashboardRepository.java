
package acme.features.flight_crew_member.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignment.CurrentStatus;
import acme.entities.leg.Leg;
import acme.realms.flight_crew_member.FlightCrewMember;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	@Query("select fa.leg.arrivalAirport.city from FlightAssignment fa where fa.flightCrewMember.id = :flightCrewMemberId and fa.draftMode = false order by fa.leg.scheduledDeparture desc")
	List<String> findLastFiveDestinations(int flightCrewMemberId);

	@Query("select count(al.flightAssignment.leg) from ActivityLog al where al.severityLevel between :x and :y and al.draftMode = false")
	Integer countLegsWithIncidentSeverityXToY(int x, int y);

	@Query("select fa.leg from FlightAssignment fa where fa.flightCrewMember.id = :flightCrewMemberId and fa.draftMode = false and fa.leg.scheduledArrival < :now order by fa.leg.scheduledArrival desc")
	List<Leg> findLastLegs(int flightCrewMemberId, Date now);

	@Query("select fa.flightCrewMember from FlightAssignment fa where fa.draftMode = false and fa.leg.id = :legId")
	List<FlightCrewMember> findLastLegFlightCrewMembers(int legId);

	@Query("select fa.leg.flightNumber from FlightAssignment fa where fa.flightCrewMember.id = :flightCrewMemberId and fa.currentStatus = :status")
	List<String> findFlightAssignmentsByStatus(int flightCrewMemberId, CurrentStatus status);

	@Query("select count(fa) * 1.0 / count(distinct concat(function('YEAR',  fa.lastUpdate), '-', function('MONTH', fa.lastUpdate))) from FlightAssignment fa where fa.flightCrewMember.id = :flightCrewMemberId and fa.draftMode = false")
	Double findAverageAssignmentsPerActiveMonth(int flightCrewMemberId);

	@Query("select count(fa) from FlightAssignment fa where fa.flightCrewMember.id = :flightCrewMemberId and fa.draftMode = false group by function('YEAR', fa.lastUpdate), function('MONTH', fa.lastUpdate)")
	List<Integer> countFlightAssignmentsPerMonth(int flightCrewMemberId);

}
