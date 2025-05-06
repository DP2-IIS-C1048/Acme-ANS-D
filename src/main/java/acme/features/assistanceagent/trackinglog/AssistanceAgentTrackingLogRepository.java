
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.tracking_log.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT t from TrackingLog t where t.id = :id")
	TrackingLog findTrackingLogById(int id);

	@Query("SELECT t from TrackingLog t where t.claim = :claimId")
	Collection<TrackingLog> findTrackingLog(int claimId);

	@Query("SELECT c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :id")
	Collection<TrackingLog> findAllTrackingLogsByClaimId(int id);

}
