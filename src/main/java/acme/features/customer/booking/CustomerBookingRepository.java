
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :customerId")
	Collection<Booking> findBookingsByCustomerId(int customerId);

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select br.passenger from BookingRecord br where br.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select c from Customer c where c.id = :customerId")
	Customer findCustomerById(int customerId);

	@Query("select b.flight from Booking b where b.id = :bookingId")
	Flight findFlightByBookingId(int bookingId);

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findPublishedFlights();
}
