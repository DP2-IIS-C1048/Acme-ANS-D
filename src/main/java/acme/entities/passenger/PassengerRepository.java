
package acme.entities.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("Select p from Passenger p join p.bookingRecords br where br.booking.customer.id = :customerId")
	List<Passenger> findPassengersByCustomerId(int customerId);
}
