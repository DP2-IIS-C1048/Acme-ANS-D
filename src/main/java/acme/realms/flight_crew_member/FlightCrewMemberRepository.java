
package acme.realms.flight_crew_member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberRepository extends AbstractRepository {

	@Query("Select f from FlightCrewMember f where f.employeeCode = :employeeCode")
	FlightCrewMember findFlightCrewMemberByemployeeCode(String employeeCode);
}
