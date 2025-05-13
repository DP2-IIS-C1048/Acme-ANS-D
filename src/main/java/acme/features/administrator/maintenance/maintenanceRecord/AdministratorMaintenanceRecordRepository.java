
package acme.features.administrator.maintenance.maintenanceRecord;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;

@Repository
public interface AdministratorMaintenanceRecordRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where draftMode = false")
	Collection<MaintenanceRecord> findMaintenanceRecordsPublished();

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findMaintenanceRecordById(int id);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

}
