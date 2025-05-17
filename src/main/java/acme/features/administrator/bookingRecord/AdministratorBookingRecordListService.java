
package acme.features.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;

@Repository
public class AdministratorBookingRecordListService extends AbstractGuiService<Administrator, BookingRecord> {

	@Autowired
	private AdministratorBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);
		status = booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<BookingRecord> bookingRecords;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		bookingRecords = this.repository.findBookingRecordsByBookingId(masterId);

		super.getBuffer().addData(bookingRecords);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;

		dataset = super.unbindObject(bookingRecord, "booking");
		dataset.put("passenger", bookingRecord.getPassenger().getPassportNumber());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<BookingRecord> bookingRecords) {
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("masterId", masterId);
	}
}
