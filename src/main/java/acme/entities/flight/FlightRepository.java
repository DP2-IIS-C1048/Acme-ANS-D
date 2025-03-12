
package acme.entities.flight;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface FlightRepository extends AbstractRepository {

	@Query("""
		    SELECT l
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND (l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight.id = :flightId))
		""")
	Leg findFirstLegByFlightId(int flightId);

	@Query("""
		    SELECT l
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND (l.scheduledArrival = (SELECT MAX(l2.scheduledArrival) FROM Leg l2 WHERE l2.flight.id = :flightId))
		""")
	Leg findLastLegByFlightId(int flightId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId")
	Integer getNumbersOfLegsByFlightId(int flightId);

}
