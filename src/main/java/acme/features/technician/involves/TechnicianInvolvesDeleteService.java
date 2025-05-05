
package acme.features.technician.involves;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenance.Involves;
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
		status = involves != null && super.getRequest().getPrincipal().hasRealm(involves.getMaintenanceRecord().getTechnician());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Involves involves;
		int involvesId;

		involvesId = super.getRequest().getData("id", int.class);
		involves = this.repository.findInvolvesById(involvesId);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		super.bindObject(involves);
	}

	@Override
	public void validate(final Involves involves) {
		{
			boolean maintenanceRecordIsAlreadyPublished = involves.getMaintenanceRecord().isDraftMode() == false;
			super.state(maintenanceRecordIsAlreadyPublished, "*", "acme.validation.involves.maintenance-record-is-already-published");
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
