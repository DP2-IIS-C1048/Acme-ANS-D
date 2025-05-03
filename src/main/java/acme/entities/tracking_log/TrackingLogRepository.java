
package acme.entities.tracking_log;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.id = :claimId order by t.lastUpdateMoment desc")
	Optional<List<TrackingLog>> findOrderedTrackingLogs(@Param("claimId") int claimId);

}
