
package acme.features.administrator.maintenance.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenance.MaintenanceRecord;

@GuiController
public class AdministratorMaintenanceRecordController extends AbstractGuiController<Administrator, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	AdministratorMaintenanceRecordListService	listService;

	@Autowired
	AdministratorMaintenanceRecordShowService	showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
