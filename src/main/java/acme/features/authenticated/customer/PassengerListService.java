
package acme.features.authenticated.customer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.PassengerRepository;
import acme.realms.customer.Customer;

@GuiService
public class PassengerListService extends AbstractService<Customer, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	public void authorise() {

	}

	@Override
	public void load() {
		List<Passenger> passengers = new ArrayList<>();
		int id;

		id = super.getRequest().getPrincipal().getActiveRealm().getId();
		passengers = this.repository.findPassengersByCustomerId(id);

		super.getBuffer().addData(passengers);
	}
}
