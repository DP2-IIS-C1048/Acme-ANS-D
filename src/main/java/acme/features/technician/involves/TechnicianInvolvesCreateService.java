
package acme.features.technician.involves;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		MaintenanceRecord maintenanceRecord;

		id = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);
		status = super.getRequest().getPrincipal().hasRealm(maintenanceRecord.getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		involves = new Involves();
		involves.setMaintenanceRecord(maintenanceRecord);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		super.bindObject(involves, "task");
	}

	@Override
	public void validate(final Involves involves) {
		{
			Collection<Involves> alreadyCreatedInvolves;
			Collection<Task> involvesTasks;
			boolean involvesTaskIsNew;

			alreadyCreatedInvolves = this.repository.findInvolvesByMaintenanceRecordId(involves.getMaintenanceRecord().getId());
			involvesTasks = alreadyCreatedInvolves == null ? List.of() : alreadyCreatedInvolves.stream().map(Involves::getTask).toList();
			involvesTaskIsNew = !involvesTasks.contains(involves.getTask());

			super.state(involvesTaskIsNew, "*", "acme.validation.involves.involves-is-new");
		}
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		Collection<Task> possibleTasks;
		Collection<Task> alreadyAddedTasks;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		possibleTasks = this.repository.findTasksPublished();
		alreadyAddedTasks = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecordId).stream().map(Involves::getTask).collect(Collectors.toList());
		possibleTasks.removeAll(alreadyAddedTasks);

		taskChoices = SelectChoices.from(possibleTasks, "description", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);
		dataset.put("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}

}
