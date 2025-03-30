
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("""
		    SELECT l
		    FROM Leg l
		    WHERE l.flight.id = :flightId AND (
		        (l.scheduledDeparture < :arrivalDate AND l.scheduledArrival > :departureDate)
		    )
		""")
	List<Leg> findLegsByArrivalDepartureDate(Date departureDate, Date arrivalDate, int flightId);

	@Query("""
		    SELECT a
		    FROM Aircraft a
		    WHERE NOT EXISTS (
		        SELECT 1
		        FROM Leg l
		        WHERE l.aircraft = a
		        AND (l.scheduledDeparture < :arrivalDate AND l.scheduledArrival > :departureDate)
		    )
		""")
	Collection<Aircraft> findAvailableAircrafts(Date departureDate, Date arrivalDate);

	@Query("SELECT f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduledDeparture")
	Collection<Leg> findAllLegsByFlightId(int id);

	@Query("SELECT l.flight FROM Leg l WHERE l.id=:legId")
	Flight findFlightByLegId(int legId);

}
