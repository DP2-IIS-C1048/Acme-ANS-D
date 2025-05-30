
package acme.features.manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.ExchangeRate;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.manager.Manager;

@GuiService
public class ManagerFlightPublishService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private ManagerFlightRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int flightId;
		Flight flight;
		Manager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(flightId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();

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
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "requiresSelfTransfer", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		{
			if (flight.getCost() != null) {
				boolean validCurrency = ExchangeRate.isValidCurrency(flight.getCost().getCurrency());
				super.state(validCurrency, "cost", "acme.validation.currency.message");
			}
		}
		{
			boolean allLegsNoDraftMode;
			Collection<Leg> legs;

			legs = this.repository.findAllLegsByFlightId(flight.getId());

			if (legs.isEmpty())
				allLegsNoDraftMode = false;
			else
				allLegsNoDraftMode = legs.stream().allMatch(leg -> !leg.isDraftMode());

			super.state(allLegsNoDraftMode, "*", "acme.validation.flight.leg.message");
		}
	}
	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}
	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "requiresSelfTransfer", "cost", "description", "draftMode");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("originCity", flight.getOriginCity());
		dataset.put("destinationCity", flight.getDestinationCity());
		dataset.put("numberOfLayovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}

}
