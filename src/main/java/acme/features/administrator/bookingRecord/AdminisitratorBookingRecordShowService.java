
package acme.features.administrator.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;

@GuiService
public class AdminisitratorBookingRecordShowService extends AbstractGuiService<Administrator, BookingRecord> {

	@Autowired
	private AdministratorBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingRecordId;
		Booking booking;

		bookingRecordId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingByBookingRecordId(bookingRecordId);
		status = booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord bookingRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		bookingRecord = this.repository.findBookingRecordById(id);

		super.getBuffer().addData(bookingRecord);

	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		Collection<Passenger> passengers;
		SelectChoices choices;
		int customerId;

		customerId = bookingRecord.getBooking().getCustomer().getId();
		passengers = this.repository.findPassengersByCustomerId(customerId);
		choices = SelectChoices.from(passengers, "passportNumber", bookingRecord.getPassenger());

		dataset = super.unbindObject(bookingRecord, "passenger");
		dataset.put("masterId", bookingRecord.getBooking().getId());
		dataset.put("passenger", choices.getSelected().getKey());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);

	}
}
