
package acme.entities.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("select a from Airline a where a.iataCode = :iataCode")
	Airline findAirlineByIataCode(String iataCode);

}
