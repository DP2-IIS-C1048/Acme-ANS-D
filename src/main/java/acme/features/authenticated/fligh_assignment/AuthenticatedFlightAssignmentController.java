
package acme.features.authenticated.fligh_assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight_assignment.FlightAssignment;

@GuiController
public class AuthenticatedFlightAssignmentController extends AbstractGuiController<Authenticated, FlightAssignment> {

	@Autowired
	private AuthenticatedFlightAssignmentListService	listService;

	@Autowired
	private AuthenticatedFlightAssignmentShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
