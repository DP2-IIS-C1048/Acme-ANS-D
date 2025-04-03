
package acme.features.customer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);
		customer = null;
		if (booking != null)
			customer = booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && booking != null && booking.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int customerId;
		Customer customer;

		customerId = super.getRequest().getData("customer", int.class);
		customer = this.repository.findCustomerById(customerId);

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");
		booking.setCustomer(customer);
	}

	@Override
	public void validate(final Booking booking) {
		boolean status;
		boolean isLastNibbleNull;

		isLastNibbleNull = false;
		if (booking.getLastNibble() == null)
			isLastNibbleNull = true;

		status = isLastNibbleNull;

		super.state(status, "*", "customer.booking.publish.lastNibble-null");
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastNibble");

		super.getResponse().addData(dataset);
	}
}
