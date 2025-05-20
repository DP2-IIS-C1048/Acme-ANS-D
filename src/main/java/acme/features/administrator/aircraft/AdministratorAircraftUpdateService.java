
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftStatus;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAircraftUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		status = aircraft != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		{
			super.state(aircraft.getAirline() != null, "airline", "acme.validation.aircraft.airline-not-found.message");
		}
		{
			boolean confirmation;

			confirmation = super.getRequest().getData("confirmation", boolean.class);
			super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
		}
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		Collection<Airline> airlines;
		SelectChoices choices;
		SelectChoices statusChoices;
		Airline selectedAirline;

		airlines = this.repository.findAllAirlines();
		selectedAirline = aircraft.getAirline();
		statusChoices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());

		if (selectedAirline != null && !airlines.contains(selectedAirline))
			airlines.add(selectedAirline);

		choices = SelectChoices.from(airlines, "iataCode", selectedAirline);

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("statuses", statusChoices);
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
