
package acme.features.authenticated.manager;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.manager.Manager;

@GuiController
public class AuthenticatedManagerController extends AbstractGuiController<Authenticated, Manager> {

	@Autowired
	private AuthenticatedManagerCreateService	createService;

	@Autowired
	private AuthenticatedManagerUpdateService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
