
package acme.features.administrator.maintenance.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenance.Task;

@Repository
public interface AdministratorTaskRepository extends AbstractRepository {

	@Query("select t from Task t where t.draftMode = false")
	Collection<Task> findAllTasksPublished();

}
