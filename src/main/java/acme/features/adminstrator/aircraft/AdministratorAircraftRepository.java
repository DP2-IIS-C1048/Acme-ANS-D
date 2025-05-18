
package acme.features.adminstrator.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;

@Repository
public interface AdministratorAircraftRepository extends AbstractRepository {

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select air from Airline air")
	Collection<Airline> findAllAirlines();

	@Query("select air from Airline air where air.id = :airlineId")
	Airline findAirlineById(int airlineId);
}
