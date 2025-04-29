
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Task;
import acme.realms.technician.Technician;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select t from Task t where t.technician.id = :id")
	Collection<Task> findTasksByTechnicianId(int id);

	@Query("select t from Technician t")
	Collection<Technician> findAllTechnicians();

	@Query("select t from Technician t where t.id = :id")
	Technician findTechnicianById(int id);

	@Query("select t from Technician t where t.license = :license")
	Technician findTechnicianByLicense(String license);

}
