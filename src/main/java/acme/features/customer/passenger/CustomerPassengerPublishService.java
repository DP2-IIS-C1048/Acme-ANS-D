
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiService
public class CustomerPassengerPublishService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Passenger passenger;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(masterId);
		customer = passenger == null ? null : passenger.getCustomer();
		status = passenger != null && passenger.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		super.getBuffer().addData(passenger);
	}

	@Override
	public void bind(final Passenger passenger) {
	}

	@Override
	public void validate(final Passenger passenger) {
		{
			super.state(passenger.isDraftMode(), "*", "acme.validation.customer.passenger.update.is-not-dratMode.message");
		}
	}

	@Override
	public void perform(final Passenger passenger) {
		passenger.setDraftMode(false);
		this.repository.save(passenger);
	}

}
