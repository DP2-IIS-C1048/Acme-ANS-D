
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Task;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :id")
	Collection<Task> findTasksByMaintenanceRecordId(int id);

}
