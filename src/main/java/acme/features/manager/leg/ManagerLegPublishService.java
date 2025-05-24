
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
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.manager.Manager;

@GuiService
public class ManagerLegPublishService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private ManagerLegRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int legId;
		Leg leg;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(legId);
		status = leg != null && super.getRequest().getPrincipal().hasRealm(leg.getFlight().getManager()) && leg.isDraftMode() && leg.getFlight().isDraftMode();

		if (status) {
			String method;
			int aircraftId, departureAirportId, arrivalAirportId;
			Aircraft aircraft;
			Airport departureAirport, arrivalAirport;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {

				aircraftId = super.getRequest().getData("aircraft", int.class);
				departureAirportId = super.getRequest().getData("departureAirport", int.class);
				arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
				aircraft = this.repository.findAircraftById(aircraftId);
				departureAirport = this.repository.findAirportById(departureAirportId);
				arrivalAirport = this.repository.findAirportById(arrivalAirportId);
				status = (aircraftId == 0 || aircraft != null && aircraft.getStatus().equals(AircraftStatus.ACTIVE)) && (arrivalAirportId == 0 || arrivalAirport != null) && (departureAirportId == 0 || departureAirport != null);
			}
		}
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
		if (leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {

			boolean uniqueArrivalAndDepartureDate;
			Collection<Leg> existingLegs;

			existingLegs = this.repository.findLegsPublishedByArrivalDepartureDate(leg.getScheduledDeparture(), leg.getScheduledArrival(), leg.getFlight().getId());
			uniqueArrivalAndDepartureDate = existingLegs.isEmpty();

			super.state(uniqueArrivalAndDepartureDate, "scheduledDeparture", "acme.validation.leg.duplicated-leg-arrivalDepartureDates.message");
			super.state(uniqueArrivalAndDepartureDate, "scheduledArrival", "acme.validation.leg.duplicated-leg-arrivalDepartureDates.message");

			Integer totalLegs;

			totalLegs = this.repository.getNumbersOfLegsPublishedByFlightId(leg.getFlight().getId());
			if (totalLegs > 0) {
				Leg firstLegPublished = this.repository.findFirstLegPublishedByFlightId(leg.getFlight().getId());
				Leg lastLegPublished = this.repository.findLastLegPublishedByFlightId(leg.getFlight().getId());

				if (!MomentHelper.isBefore(leg.getScheduledArrival(), firstLegPublished.getScheduledDeparture()) && !MomentHelper.isAfter(leg.getScheduledDeparture(), lastLegPublished.getScheduledArrival())) {
					super.state(false, "scheduledDeparture", "acme.validation.leg.invalid-leg-dates.message");
					super.state(false, "scheduledArrival", "acme.validation.leg.invalid-leg-dates.message");
				}
			}

			Leg firstLegPublished;
			Leg lastLegPublished;

			if (totalLegs > 0 && leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
				firstLegPublished = this.repository.findFirstLegPublishedByFlightId(leg.getFlight().getId());
				lastLegPublished = this.repository.findLastLegPublishedByFlightId(leg.getFlight().getId());

				if (MomentHelper.isBefore(leg.getScheduledArrival(), firstLegPublished.getScheduledDeparture()) && MomentHelper.isBefore(leg.getScheduledDeparture(), firstLegPublished.getScheduledDeparture())) {
					if (!leg.getFlight().isRequiresSelfTransfer() && !leg.getArrivalAirport().getIataCode().equals(firstLegPublished.getDepartureAirport().getIataCode()))
						super.state(false, "arrivalAirport", "acme.validation.leg.invalid-leg-arrivalAirport.message");
					if (leg.getFlight().isRequiresSelfTransfer() && leg.getArrivalAirport().getIataCode().equals(firstLegPublished.getDepartureAirport().getIataCode()))
						super.state(false, "arrivalAirport", "acme.validation.leg.invalid-leg-arrivalAirport-selfTransfer.message");
				}
				if (MomentHelper.isAfter(leg.getScheduledArrival(), lastLegPublished.getScheduledArrival()) && MomentHelper.isAfter(leg.getScheduledDeparture(), lastLegPublished.getScheduledArrival())) {
					if (!leg.getFlight().isRequiresSelfTransfer() && !leg.getDepartureAirport().getIataCode().equals(lastLegPublished.getArrivalAirport().getIataCode()))
						super.state(false, "departureAirport", "acme.validation.leg.invalid-leg-departureAirport.message");
					if (leg.getFlight().isRequiresSelfTransfer() && leg.getDepartureAirport().getIataCode().equals(lastLegPublished.getArrivalAirport().getIataCode()))
						super.state(false, "departureAirport", "acme.validation.leg.invalid-leg-departureAirport-selfTransfer.message");
				}

			}

			boolean validAircraft;

			if (leg.getAircraft() != null && leg.getScheduledArrival() != null && leg.getScheduledDeparture() != null) {
				validAircraft = this.repository.findLegsWithAircraftInUse(leg.getAircraft().getId(), leg.getScheduledDeparture(), leg.getScheduledArrival()).isEmpty();

				super.state(validAircraft, "aircraft", "acme.validation.leg.invalid-aircraft.message");
			}
		}
		if (leg.getScheduledDeparture() != null)
			super.state(MomentHelper.isFuture(leg.getScheduledDeparture()), "scheduledDeparture", "acme.validation.leg.invalid-futureDates.message");

		if (leg.getScheduledArrival() != null)
			super.state(MomentHelper.isFuture(leg.getScheduledArrival()), "scheduledArrival", "acme.validation.leg.invalid-futureDates.message");

	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
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

		super.getResponse().addData(dataset);
	}

}
