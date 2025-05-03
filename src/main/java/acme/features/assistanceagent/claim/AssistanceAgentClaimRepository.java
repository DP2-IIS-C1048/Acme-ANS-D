
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c from Claim c where c.id = :id")
	Claim findClaimById(int id);

	@Query("SELECT c from Claim c where c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findClaimsByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLeg();

}
