
package acme.features.authenticated.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;

@GuiService
public class AuthenticatedFlightListService extends AbstractGuiService<Authenticated, Flight> {

	@Autowired
	private AuthenticatedFlightRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().isAuthenticated();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Flight> flights;

		flights = this.repository.findPublishedFlights();

		super.getBuffer().addData(flights);

	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost");
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getLayovers());

		super.getResponse().addData(dataset);

	}
}
