
package acme.features.authenticated.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.features.manager.flight.ManagerFlightRepository;

@GuiService
public class AuthenticatedFlightShowService extends AbstractGuiService<Authenticated, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {

		boolean status;
		boolean draftMode;
		draftMode = super.getRequest().getData("draftMode", boolean.class);
		status = super.getRequest().getPrincipal().isAuthenticated() && !draftMode;
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode", "leg.legLabel");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}
}
