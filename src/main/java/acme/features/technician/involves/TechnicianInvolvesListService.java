/*
 * package acme.features.technician.involves;
 * 
 * import java.util.Collection;
 * 
 * import org.springframework.beans.factory.annotation.Autowired;
 * 
 * import acme.client.components.models.Dataset;
 * import acme.entities.maintenance.Task;
 * import acme.features.technician.task.TechnicianTaskRepository;
 * 
 * public class TechnicianInvolvesListService {
 * 
 * // Internal state ---------------------------------------------------------
 * 
 * @Autowired
 * private TechnicianTaskRepository repository;
 * 
 * // AbstractGuiService interface -------------------------------------------
 * 
 * 
 * @Override
 * public void authorise() {
 * super.getResponse().setAuthorised(true);
 * }
 * 
 * @Override
 * public void load() {
 * Collection<Task> tasks;
 * int maintenanceRecordId;
 * 
 * maintenanceRecordId = super.getRequest().getData("masterId", int.class);
 * tasks = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);
 * 
 * super.getBuffer().addData(tasks);
 * }
 * 
 * @Override
 * public void unbind(final Task task) {
 * Dataset dataset;
 * 
 * dataset = super.unbindObject(task, "type", "priority", "estimatedDuration");
 * 
 * super.getResponse().addData(dataset);
 * 
 * }
 * 
 * }
 */
