
package acme.entities.leg;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import acme.client.repositories.AbstractRepository;

public interface LegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l WHERE l.flightNumber =:flightNumber")
	Leg findLegByFlightNumber(String flightNumber);

	@Query("""
		    SELECT l
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND (
		        (l.scheduledDeparture BETWEEN :departureDate AND :arrivalDate)
		        OR
		        (l.scheduledArrival BETWEEN :departureDate AND :arrivalDate)
		    )
		""")
	List<Leg> findLegsByFlightIdAndArrivalDepartureDate(int flightId, Date departureDate, Date arrivalDate);

}
