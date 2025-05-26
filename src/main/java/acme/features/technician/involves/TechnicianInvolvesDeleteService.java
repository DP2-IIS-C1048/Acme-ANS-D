
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
public class TechnicianInvolvesDeleteService extends AbstractGuiService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		Involves involves;

		id = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(id);
		status = involves != null && involves.getMaintenanceRecord().isDraftMode() && super.getRequest().getPrincipal().hasRealm(involves.getMaintenanceRecord().getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		MaintenanceRecord maintenanceRecord;
		int involvesId;
		boolean isMaintenanceRecordInDraftMode;

		involvesId = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(involvesId);
		maintenanceRecord = this.repository.findMaintenanceRecordById(involves.getMaintenanceRecord().getId());
		isMaintenanceRecordInDraftMode = maintenanceRecord.isDraftMode();

		super.getBuffer().addData(involves);
		super.getResponse().addGlobal("isMaintenanceRecordInDraftMode", isMaintenanceRecordInDraftMode);
	}

	@Override
	public void bind(final Involves involves) {
		super.bindObject(involves);
	}

	@Override
	public void validate(final Involves involves) {
		{
			boolean maintenanceRecordIsNotPublishedYet = involves.getMaintenanceRecord().isDraftMode();
			super.state(maintenanceRecordIsNotPublishedYet, "*", "acme.validation.involves.maintenance-record-is-not-published-yet");
		}
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.delete(involves);
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
