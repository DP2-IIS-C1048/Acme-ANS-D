
package acme.entities.booking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.realms.customer.Customer;

@GuiService
public class ListingBookingByCustomerService extends AbstractService<Customer, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	public void authorise() {
	}

	@Override
	public void load() {
		List<Booking> bookings = new ArrayList<>();
		int id;
		id = super.getRequest().getPrincipal().getAccountId();
		bookings = this.repository.findBookingsByCustomerId(id);

		super.getBuffer().addData(bookings);

	}
}
