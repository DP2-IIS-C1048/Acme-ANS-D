
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.Task;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select i from Involves i where i.maintenanceRecord.id = :id")
	Collection<Involves> findInvolvesByMaintenanceRecordId(int id);

	@Query("select i.maintenanceRecord from Involves i where i.id = :id")
	MaintenanceRecord findMaintenanceRecordByInvolvesId(int id);

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findTasksPublished();

	@Query("select i from Involves i where i.id = :id")
	Involves findInvolvesById(int id);

}
