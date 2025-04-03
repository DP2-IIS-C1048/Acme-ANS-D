
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.manager.Manager;

@Repository
public interface ManagerFlightRepository extends AbstractRepository {

	@Query("SELECT f from Flight f where f.id = :id")
	Flight findFlightById(int id);

	@Query("SELECT f from Flight f where f.manager.id = :managerId")
	Collection<Flight> findFlightsByManagerId(int managerId);

	@Query("SELECT m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT l from Leg l where l.flight.id =:id")
	Collection<Leg> findAllLegsByFlightId(int id);

}
