
package acme.entities.tracking_log;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :id ORDER BY t.lastUpdateMoment DESC")
	Collection<TrackingLog> findOrderedTrackingLogs(int id);

}
