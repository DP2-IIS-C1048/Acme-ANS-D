
package acme.features.flight_crew_member.activity_log;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_log.ActivityLog;
import acme.entities.flight_assignment.FlightAssignment;

@Repository
public interface FlightCrewMemberActivityLogRepository extends AbstractRepository {

	@Query("select al from ActivityLog al where al.flightAssignment.id = :flightAssignmentId")
	Collection<ActivityLog> findActivityLogByFlightAssignmentId(int flightAssignmentId);

	@Query("select fa from FlightAssignment fa where fa.id = :flightAssignmentId")
	FlightAssignment findFlightAssignmentById(int flightAssignmentId);

	@Query("select al from ActivityLog al where al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select al.flightAssignment from ActivityLog al where al.id = :activityLogId")
	FlightAssignment findFlightAssignmentByActivityLogId(int activityLogId);

}
