
package acme.entities.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.locatorCode = :locatorCode")
	Booking findBookingByLocatorCode(String locatorCode);

	//Given a customer id, return the bookings of that customer
	@Query("select b from Booking b where b.customer.id = :customerId")
	List<Booking> findBookingsByCustomerId(int customerId);

}
