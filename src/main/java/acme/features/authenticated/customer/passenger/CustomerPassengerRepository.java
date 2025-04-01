
package acme.features.authenticated.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("Select p from Passenger p join p.bookingRecords br where br.booking.customer.id = :customerId")
	List<Passenger> findPassengersByCustomerId(int customerId);

	@Query("Select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select br.booking.customer from BookingRecord br where br.passenger.id = :passengerId")
	Customer findCustomerByPassengerId(int passengerId);
}
