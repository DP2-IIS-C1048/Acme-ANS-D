
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Task;
import acme.entities.maintenance.TaskType;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Task Task;
		int id;

		id = super.getRequest().getData("id", int.class);
		Task = this.repository.findTaskById(id);

		super.getBuffer().addData(Task);
	}

	@Override
	public void unbind(final Task Task) {
		Dataset dataset;
		SelectChoices typeChoices;
		Collection<Technician> technicians;
		SelectChoices technicianChoices;

		typeChoices = SelectChoices.from(TaskType.class, Task.getType());
		technicians = this.repository.findAllTechnicians();
		technicianChoices = SelectChoices.from(technicians, "license", Task.getTechnician());

		dataset = super.unbindObject(Task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", typeChoices);
		dataset.put("technician", technicianChoices.getSelected().getKey());
		dataset.put("technicians", technicianChoices);

		super.getResponse().addData(dataset);
	}

}
