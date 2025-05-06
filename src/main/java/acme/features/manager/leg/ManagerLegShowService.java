
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegShowService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Flight flight;

		legId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightByLegId(legId);
		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager());

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
		Collection<Airport> airports;
		Collection<Aircraft> aircrafts;
		SelectChoices choiceDepartureAirports;
		SelectChoices choiceArrivalAirports;
		SelectChoices choiceAircrafts;
		SelectChoices choiceStatuses;

		airports = this.repository.findAllAirports();
		aircrafts = this.repository.findActiveAircrafts();
		dataset = super.unbindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture", "status", "draftMode");
		choiceAircrafts = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		choiceDepartureAirports = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		choiceArrivalAirports = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		choiceStatuses = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset.put("aircraft", choiceAircrafts.getSelected().getKey());
		dataset.put("aircrafts", choiceAircrafts);
		dataset.put("departureAirport", choiceDepartureAirports.getSelected().getKey());
		dataset.put("departureAirports", choiceDepartureAirports);
		dataset.put("arrivalAirport", choiceArrivalAirports.getSelected().getKey());
		dataset.put("arrivalAirports", choiceArrivalAirports);
		dataset.put("statuses", choiceStatuses);
		dataset.put("masterId", leg.getFlight().getId());

		super.getResponse().addData(dataset);
	}

}
