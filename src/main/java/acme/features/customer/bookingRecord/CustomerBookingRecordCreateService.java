
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		Booking booking;
		BookingRecord bookingRecord = new BookingRecord();

		masterId = super.getRequest().getData("masterId", int.class);
		booking = this.repository.findBookingById(masterId);

		bookingRecord.setBooking(booking);

		super.getBuffer().addData(bookingRecord);
	}

	@Override
	public void bind(final BookingRecord bookingRecord) {
		super.bindObject(bookingRecord, "passenger");
	}

	@Override
	public void validate(final BookingRecord bookingRecord) {
		{
			boolean validPassenger = false;
			Passenger passenger = bookingRecord.getPassenger();
			Customer customer = this.repository.findBookingById(bookingRecord.getBooking().getId()).getCustomer();
			Collection<Passenger> customerPassengers = this.repository.findPassengersByCustomerId(customer.getId());

			if (customerPassengers.contains(passenger))
				validPassenger = true;
			super.state(validPassenger, "passenger", "acme.validation.booking-record.create.passenger-not-from-customer.message");

		}
	}

	@Override
	public void perform(final BookingRecord bookingRecord) {
		this.repository.save(bookingRecord);
	}

	@Override
	public void unbind(final BookingRecord bookingRecord) {
		Dataset dataset;
		Collection<Passenger> passengers;
		SelectChoices choices;
		int customerId;
		Passenger selectedPassenger = bookingRecord.getPassenger();

		customerId = bookingRecord.getBooking().getCustomer().getId();
		passengers = this.repository.findPassengersByCustomerId(customerId);

		if (selectedPassenger != null && !passengers.contains(selectedPassenger))
			selectedPassenger = null;

		choices = SelectChoices.from(passengers, "fullName", selectedPassenger);

		dataset = super.unbindObject(bookingRecord, "passenger");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("passenger", choices.getSelected().getKey());
		dataset.put("passengers", choices);

		super.getResponse().addData(dataset);
	}
}
