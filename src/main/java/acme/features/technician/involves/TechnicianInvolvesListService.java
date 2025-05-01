
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("masterId", int.class);
		tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		int masterId;

		dataset = super.unbindObject(involves, "tasks");
		dataset.put("maintenanceRecord", involves.getMaintenanceRecord());

		masterId = super.getRequest().getData("masterId", int.class);
		super.getResponse().addGlobal("masterId", masterId);

		super.getResponse().addData(dataset);

	}
}
