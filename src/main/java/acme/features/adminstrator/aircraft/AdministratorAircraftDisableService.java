
package acme.features.adminstrator.aircraft;

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
public class AdministratorAircraftDisableService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		status = aircraft != null && aircraft.getStatus().equals(AircraftStatus.ACTIVE);

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
		;
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
		{
			Aircraft originalAircraft = this.repository.findAircraftById(aircraft.getId());
			if (!originalAircraft.getStatus().equals(AircraftStatus.ACTIVE))
				super.state(false, "status", "acme.validation.administrator.aircraft.status-MAINTENANCE.message");
		}
	}

	@Override
	public void perform(final Aircraft aircraft) {
		aircraft.setStatus(AircraftStatus.MAINTENANCE);
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
