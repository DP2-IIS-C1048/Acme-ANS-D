
package acme.features.authenticated.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;

@GuiController
public class AuthenticatedLegController extends AbstractGuiController<Authenticated, Leg> {

	@Autowired
	private AuthenticatedLegListService	listService;

	@Autowired
	private AuthenticatedLegShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
