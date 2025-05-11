
package acme.features.adminstrator.aircraft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.aircraft.Aircraft;

@GuiController
public class AdministratorAircraftController extends AbstractGuiController<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftListService	listService;

	@Autowired
	private AdministratorAircraftShowService	showService;

	@Autowired
	private AdministratorAircraftCreateService	createService;

	@Autowired
	private AdministratorAircraftUpdateService	updateService;

	@Autowired
	private AdministratorAircraftDisableService	disableService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("disable", "update", this.disableService);
	}
}
