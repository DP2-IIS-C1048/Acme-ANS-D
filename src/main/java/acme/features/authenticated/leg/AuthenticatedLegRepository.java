
package acme.features.authenticated.leg;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface AuthenticatedLegRepository extends AbstractRepository {

	@Query("SELECT f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :id ORDER BY l.scheduledDeparture ASC")
	Collection<Leg> findAllLegsByFlightId(int id);

	@Query("SELECT l.flight FROM Leg l WHERE l.id=:legId")
	Flight findFlightByLegId(int legId);

}
