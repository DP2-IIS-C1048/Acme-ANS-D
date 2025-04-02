
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegRepository extends AbstractRepository {

	@Query("SELECT l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("""
		        SELECT l
		        FROM Leg l
		        WHERE l.flight.id = :flightId AND l.draftMode = false AND (
		            (l.scheduledDeparture < :arrivalDate AND l.scheduledArrival > :departureDate)
		        )
		""")
	Collection<Leg> findLegsPublishedByArrivalDepartureDate(Date departureDate, Date arrivalDate, int flightId);

	@Query("""
		        SELECT COUNT(l) = 0
		        FROM Leg l
		        WHERE l.aircraft.id = :aircraftId AND l.draftMode = false AND (
		            (l.scheduledDeparture < :arrivalDate AND l.scheduledArrival > :departureDate)
		        )
		""")
	boolean isAircraftNotInUse(int aircraftId, Date departureDate, Date arrivalDate);

	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

	@Query("SELECT f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findAllLegsByFlightId(int id);

	@Query("SELECT l.flight FROM Leg l WHERE l.id=:legId")
	Flight findFlightByLegId(int legId);

	@Query("SELECT a FROM Airport a WHERE a.id = :id")
	Airport findAirportById(int id);

	@Query("SELECT a FROM Aircraft a WHERE a.id = :id")
	Aircraft findAircraftById(int id);

	@Query("""
		    SELECT l
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight.id = :flightId AND l2.draftMode = false)
		""")
	Leg findFirstLegPublishedByFlightId(int flightId);

	@Query("""
		        SELECT l
		        FROM Leg l
		        WHERE l.flight.id = :flightId
		        AND l.draftMode = false
		        AND l.scheduledArrival =
		            (SELECT MAX(l2.scheduledArrival)
		             FROM Leg l2
		             WHERE l2.flight.id = :flightId
		             AND l2.draftMode = false)
		""")
	Leg findLastLegPublishedByFlightId(int flightId);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id AND l.draftMode = false ORDER BY l.scheduledDeparture")
	Collection<Leg> findAllLegsPublishedByFlightId(int id);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.id = :flightId AND l.draftMode = false")
	Integer getNumbersOfLegsPublishedByFlightId(int flightId);

}
