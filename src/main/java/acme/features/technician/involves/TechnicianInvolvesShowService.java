
package acme.features.technician.involves;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianInvolvesShowService extends AbstractGuiService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordByInvolvesId(id);
		status = maintenanceRecord != null && (!maintenanceRecord.isDraftMode() || super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int id;
		MaintenanceRecord maintenanceRecord;
		boolean isMaintenanceRecordInDraftMode;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		maintenanceRecord = this.repository.findMaintenanceRecordById(involves.getMaintenanceRecord().getId());
		isMaintenanceRecordInDraftMode = maintenanceRecord.isDraftMode();

		super.getBuffer().addData(involves);
		super.getResponse().addGlobal("isMaintenanceRecordInDraftMode", isMaintenanceRecordInDraftMode);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		Task task;

		task = involves.getTask();

		dataset = super.unbindObject(involves);
		dataset.put("type", task.getType());
		dataset.put("description", task.getDescription());
		dataset.put("priority", task.getPriority());
		dataset.put("estimatedDuration", task.getEstimatedDuration());
		dataset.put("technician", task.getTechnician().getLicense());

		super.getResponse().addData(dataset);
	}

}
