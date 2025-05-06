
package acme.features.administrator.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airline.Airline;

@GuiController
public class AdministratorAirlineController extends AbstractGuiController<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineListService		listService;

	@Autowired
	private AdministratorAirlineShowService		showService;

	@Autowired
	private AdministratorAirlineCreateService	createService;

	@Autowired
	private AdministratorAirlineUpdateService	updateService;
	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
