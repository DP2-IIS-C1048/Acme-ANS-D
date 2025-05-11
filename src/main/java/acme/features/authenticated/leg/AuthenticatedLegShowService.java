
package acme.features.authenticated.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.leg.ManagerLegRepository;

@GuiService
public class AuthenticatedLegShowService extends AbstractGuiService<Authenticated, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int legId;
		Flight flight;

		legId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightByLegId(legId);
		status = flight != null && super.getRequest().getPrincipal().isAuthenticated() && !flight.isDraftMode();
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture", "status", "draftMode");
		dataset.put("aircraft", leg.getAircraft().getRegistrationNumber());
		dataset.put("departureAirport", leg.getDepartureAirport().getIataCode());
		dataset.put("arrivalAirport", leg.getArrivalAirport().getIataCode());
		dataset.put("masterId", leg.getFlight().getId());
		super.getResponse().addData(dataset);
	}
}
