
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.tracking_log.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("SELECT t from TrackingLog t where t.claim = :claimId")
	Collection<TrackingLog> findTrackingLog(int claimId);

}
