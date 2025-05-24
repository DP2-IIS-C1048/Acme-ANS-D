
package acme.features.administrator.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select f from Flight f join Leg l on l.flight.id = f.id where l.scheduledDeparture > :scheduledDeparture and l.scheduledDeparture = (select min(l2.scheduledDeparture) from Leg l2 where l2.flight.id = f.id)")
	Collection<Flight> findFlightsWithFirstLegAfter(Date scheduledDeparture);

	@Query("select b from Booking b where b.draftMode = false")
	Collection<Booking> findPublishedBookings();
}
