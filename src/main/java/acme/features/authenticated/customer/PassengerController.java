
package acme.features.authenticated.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passenger.Passenger;
import acme.realms.customer.Customer;

@GuiController
public class PassengerController extends AbstractGuiController<Customer, Passenger> {

	@Autowired
	private PassengerListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}
}
