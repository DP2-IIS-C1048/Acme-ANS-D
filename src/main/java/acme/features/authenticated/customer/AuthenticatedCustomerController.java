
package acme.features.authenticated.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.customer.Customer;

@GuiController
public class AuthenticatedCustomerController extends AbstractGuiController<Authenticated, Customer> {

	@Autowired
	private AuthenticatedCustomerUpdateService updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("update", this.updateService);
	}
}
