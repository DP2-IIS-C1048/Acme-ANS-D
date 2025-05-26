
package acme.features.manager.leg;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegCreateService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flightId", int.class);
		flight = this.repository.findFlightById(flightId);
		status = flight != null && super.getRequest().getPrincipal().hasRealm(flight.getManager()) && flight.isDraftMode();

		if (status) {
			String method;
			int aircraftId, departureAirportId, arrivalAirportId, legId;
			Aircraft aircraft;
			Airport departureAirport, arrivalAirport;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				legId = super.getRequest().getData("id", int.class);

				if (legId == 0) {
					aircraftId = super.getRequest().getData("aircraft", int.class);
					departureAirportId = super.getRequest().getData("departureAirport", int.class);
					arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
					aircraft = this.repository.findAircraftById(aircraftId);
					departureAirport = this.repository.findAirportById(departureAirportId);
					arrivalAirport = this.repository.findAirportById(arrivalAirportId);
					status = (aircraftId == 0 || aircraft != null && aircraft.getStatus().equals(AircraftStatus.ACTIVE)) && (arrivalAirportId == 0 || arrivalAirport != null) && (departureAirportId == 0 || departureAirport != null);

				} else
					status = false;

			}
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int flightId;
		Leg leg;

		flightId = super.getRequest().getData("flightId", int.class);
		flight = this.repository.findFlightById(flightId);

		leg = new Leg();
		leg.setFlight(flight);
		leg.setDraftMode(true);
		leg.setStatus(LegStatus.ON_TIME);

		super.getBuffer().addData(leg);
	}

	@Override
	public void bind(final Leg leg) {

		int aircraftId;
		Aircraft aircraft;

		int arrivalAirportId;
		Airport arrivalAirport;

		int departureAirportId;
		Airport departureAirport;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.repository.findAirportById(arrivalAirportId);

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.repository.findAirportById(departureAirportId);

		super.bindObject(leg, "flightNumber", "scheduledArrival", "scheduledDeparture");

		leg.setAircraft(aircraft);
		leg.setArrivalAirport(arrivalAirport);
		leg.setDepartureAirport(departureAirport);
	}

	@Override
	public void validate(final Leg leg) {

		boolean validAircraft;

		if (leg.getAircraft() != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
			validAircraft = this.repository.findLegsWithAircraftInUse(leg.getAircraft().getId(), leg.getScheduledDeparture(), leg.getScheduledArrival()).isEmpty();

			super.state(validAircraft, "aircraft", "acme.validation.leg.invalid-aircraft.message");
		}
		if (leg.getScheduledDeparture() != null && !MomentHelper.isFuture(leg.getScheduledDeparture()))
			super.state(false, "scheduledDeparture", "acme.validation.leg.invalid-futureDates.message");

		if (leg.getScheduledArrival() != null && !MomentHelper.isFuture(leg.getScheduledArrival()))
			super.state(false, "scheduledArrival", "acme.validation.leg.invalid-futureDates.message");

	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
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
		dataset.put("duration", leg.getDuration());
		dataset.put("flightId", super.getRequest().getData("flightId", int.class));

		super.getResponse().addData(dataset);
	}

}
