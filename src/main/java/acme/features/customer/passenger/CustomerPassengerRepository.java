
package acme.features.customer.passenger;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("Select p from Passenger p where p.customer.id = :customerId")
	Collection<Passenger> findPassengersByCustomerId(int customerId);

	@Query("Select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select p.customer from Passenger p where p.id = :passengerId")
	Customer findCustomerByPassengerId(int passengerId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);
}
