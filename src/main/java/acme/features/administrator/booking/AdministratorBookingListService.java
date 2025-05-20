
package acme.features.administrator.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.entities.booking.Booking;

@Repository
public class AdministratorBookingListService extends AbstractGuiService<Administrator, Booking> {

	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Booking> bookings;
		bookings = this.repository.findPublishedBookings();

		super.getBuffer().addData(bookings);

	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "draftMode");
		super.getResponse().addData(dataset);
	}

}
