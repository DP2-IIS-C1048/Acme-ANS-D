
package acme.features.technician.involves;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenance.Involves;
import acme.realms.technician.Technician;

@GuiController
public class TechnicianInvolvesController extends AbstractGuiController<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesListService	listService;

	@Autowired
	private TechnicianInvolvesCreateService	createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
	}

}
