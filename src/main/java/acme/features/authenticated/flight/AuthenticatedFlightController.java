
package acme.features.authenticated.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;

@GuiController
public class AuthenticatedFlightController extends AbstractGuiController<Authenticated, Flight> {

	@Autowired
	private AuthenticatedFlightListService	listService;

	@Autowired
	private AuthenticatedFlightShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
