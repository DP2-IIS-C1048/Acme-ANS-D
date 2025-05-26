
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		Passenger passenger;

		dataset = super.unbindObject(bookingRecord);
		passenger = bookingRecord.getPassenger();

		dataset.put("passportNumber", passenger.getPassportNumber());
		dataset.put("dateOfBirth", passenger.getDateOfBirth());
		dataset.put("fullName", passenger.getFullName());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<BookingRecord> bookingRecords) {
		final boolean showCreate;
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);
		showCreate = booking.isDraftMode() && booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addGlobal("showCreate", showCreate);
	}
}
