
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("masterId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
		status = maintenanceRecord != null && (!maintenanceRecord.isDraftMode() || super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician()));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Involves> involves;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		boolean isMaintenanceRecordInDraftMode;

		maintenanceRecordId = super.getRequest().getData("masterId", int.class);
		involves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecordId);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		isMaintenanceRecordInDraftMode = maintenanceRecord.isDraftMode();

		super.getBuffer().addData(involves);
		super.getResponse().addGlobal("isMaintenanceRecordInDraftMode", isMaintenanceRecordInDraftMode);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		Task task;

		dataset = super.unbindObject(involves);
		task = involves.getTask();

		dataset.put("type", task.getType());
		dataset.put("priority", task.getPriority());
		dataset.put("estimatedDuration", task.getEstimatedDuration());

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Involves> involves) {
		int maintenanceRecordId;
		maintenanceRecordId = super.getRequest().getData("masterId", int.class);

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);

	}
}
