
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Involves> involves;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("masterId", int.class);
		involves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecordId);

		super.getBuffer().addData(involves);
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
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
}
