
package acme.features.authenticated.fligh_assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface AuthenticatedFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.draftMode = false")
	Collection<FlightAssignment> findPublishedFlightAssignments();

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select l from Leg l where l.draftMode = false")
	Collection<Leg> findPublishedLegs();

}
