
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRecord;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {

	@Autowired
	private CustomerBookingRecordRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingRecordId;
		Booking booking;

		bookingRecordId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingByBookingRecordId(bookingRecordId);
		status = booking != null && super.getRequest().getPrincipal().hasRealm(booking.getCustomer());

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
		Passenger passenger;

		passenger = bookingRecord.getPassenger();

		dataset = super.unbindObject(bookingRecord);
		dataset.put("passportNumber", passenger.getPassportNumber());
		dataset.put("dateOfBirth", passenger.getDateOfBirth());
		dataset.put("fullName", passenger.getFullName());
		dataset.put("email", passenger.getEmail());
		dataset.put("specialNeeds", passenger.getSpecialNeeds());

		super.getResponse().addData(dataset);

	}
}
