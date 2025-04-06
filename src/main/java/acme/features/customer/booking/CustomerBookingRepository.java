
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;
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
	Collection<Flight> findFlightByBookingId(int bookingId);

	@Query("select f from Flight f where f.draftMode = false")
	Collection<Flight> findPublishedFlights();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select f from Flight f join Leg l on l.flight.id = f.id where l.scheduledDeparture > :scheduledDeparture and l.scheduledDeparture = (select min(l2.scheduledDeparture) from Leg l2 where l2.flight.id = f.id)")
	Collection<Flight> findFlightsWithFirstLegAfter(Date scheduledDeparture);

	@Query("select f from Flight f")
	Collection<Flight> findAllFlights();

}
