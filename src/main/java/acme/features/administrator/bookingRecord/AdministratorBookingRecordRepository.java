
package acme.features.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorBookingRecordRepository extends AbstractRepository {

	@Query("select b from BookingRecord b where b.booking.id = :bookingId")
	Collection<BookingRecord> findBookingRecordsByBookingId(int bookingId);

	@Query("select b from BookingRecord b where b.id = :bookingRecordId")
	BookingRecord findBookingRecordById(int bookingRecordId);

	@Query("select p from Passenger p where p.customer.id = :customerId")
	Collection<Passenger> findPassengersByCustomerId(int customerId);
}
