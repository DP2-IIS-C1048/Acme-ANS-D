
package acme.features.authenticated.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@GuiService
public class AuthenticatedLegListService extends AbstractGuiService<Authenticated, Leg> {

	@Autowired
	private AuthenticatedLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flightId", int.class);
		flight = this.repository.findFlightById(flightId);
		status = super.getRequest().getPrincipal().isAuthenticated() && !flight.isDraftMode();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Leg> legs;
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);
		legs = this.repository.findAllLegsByFlightId(flightId);

		super.getBuffer().addData(legs);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture", "departureAirport.iataCode", "arrivalAirport.iataCode", "aircraft.registrationNumber", "status");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Leg> legs) {
		int flightId;

		flightId = super.getRequest().getData("flightId", int.class);

		super.getResponse().addGlobal("flightId", flightId);

	}

}
