
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
public class CustomerBookingUpdateService extends AbstractGuiService<Customer, Booking> {

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
		customer = booking == null ? null : booking.getCustomer();
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
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "locatorCode", "travelClass", "price", "lastNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		{
			Date moment;
			moment = MomentHelper.getCurrentMoment();

			if (booking.getFlight() != null) {
				boolean flightDepartureFuture = booking.getFlight().getScheduledDeparture().after(moment);
				super.state(flightDepartureFuture, "flight", "acme.validation.booking.departure-not-in-future.message");
			}

		}
		{
			if (booking.getPrice() != null) {
				boolean validCurrency = ExchangeRate.isValidCurrency(booking.getPrice().getCurrency());
				super.state(validCurrency, "price", "acme.validation.currency.message");
			}
		}
		{
			super.state(booking.isDraftMode(), "*", "acme.validation.booking.is-not-draft-mode.message");
		}
		{
			super.state(booking.getFlight() != null, "flight", "acme.validation.booking.flight.not-found.messasge");
		}
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
		Flight selectedFlight = booking.getFlight();

		moment = MomentHelper.getCurrentMoment();
		flights = this.repository.findFlightsWithFirstLegAfter(moment);

		if (selectedFlight != null && !flights.contains(selectedFlight))
			flights.add(selectedFlight);

		choices = SelectChoices.from(flights, "flightRoute", selectedFlight);

		dataset = super.unbindObject(booking, "locatorCode", "travelClass", "price", "lastNibble");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);

		super.getResponse().addData(dataset);
	}

}
