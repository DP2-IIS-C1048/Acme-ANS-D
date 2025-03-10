
package acme.entities.aircraft;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository {

	@Query("Select a from Aircraft a where a.registrationNumber = :registrationNumber")
	Aircraft findAircraftByregistrationNumber(String registrationNumber);

}
