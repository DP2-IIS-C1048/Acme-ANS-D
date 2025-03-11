
package acme.realms.assistanceagent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AssistanceAgentRepository extends AbstractRepository {

	@Query("Select a from AssistanceAgent where a.employeeCode = :employeeCode")
	AssistanceAgent findAssistanceAgentByEmployeeCode(String employeeCode);
}
