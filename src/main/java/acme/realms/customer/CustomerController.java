
package acme.realms.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.Booking;
import acme.entities.booking.ListingBookingByCustomerService;

@GuiController
public class CustomerController extends AbstractGuiController<Customer, Booking> {

	@Autowired
	private ListingBookingByCustomerService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
	}

}
