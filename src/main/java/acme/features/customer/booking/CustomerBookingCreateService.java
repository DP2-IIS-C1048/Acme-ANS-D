
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.ExchangeRate;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.realms.customer.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		booking.setPurchaseMoment(moment);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "price", "lastNibble");
		booking.setFlight(flight);

	}

	@Override
	public void validate(final Booking booking) {

		boolean validCurrency = ExchangeRate.isValidCurrency(booking.getPrice().getCurrency());
		super.state(validCurrency, "price", "acme.validation.currency.message");

	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		Collection<Flight> flights;
		SelectChoices choices;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		flights = this.repository.findFlightsWithFirstLegAfter(moment);
		choices = SelectChoices.from(flights, "tag", booking.getFlight());

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "lastNibble");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);

		super.getResponse().addData(dataset);
	}
}
