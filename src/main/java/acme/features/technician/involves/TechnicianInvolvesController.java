
package acme.features.technician.involves;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenance.Involves;
import acme.realms.technician.Technician;

@GuiController
public class TechnicianInvolvesController extends AbstractGuiController<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesListService listService;

	//@Autowired
	//private TechnicianInvolvesShowService		showService;

	//@Autowired
	//private TechnicianInvolvesCreateService		createService;

	//@Autowired
	//private TechnicianInvolvesUpdateService		updateService;

	//@Autowired
	//private TechnicianInvolvesDeleteService		deleteService;

	//@Autowired
	//private TechnicianInvolvesPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		//super.addBasicCommand("show", this.showService);
		//super.addBasicCommand("create", this.createService);
		//super.addBasicCommand("update", this.updateService);
		//super.addBasicCommand("delete", this.deleteService);

		//super.addCustomCommand("publish", "update", this.publishService);
	}

}
