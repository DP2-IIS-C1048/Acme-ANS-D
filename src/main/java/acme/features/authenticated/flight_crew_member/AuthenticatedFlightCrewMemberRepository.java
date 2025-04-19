
package acme.features.authenticated.flight_crew_member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.flight_crew_member.FlightCrewMember;

@Repository
public interface AuthenticatedFlightCrewMemberRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select fcm from FlightCrewMember fcm where fcm.userAccount.id = :id")
	FlightCrewMember findFlightCrewMemberByUserAccountId(int id);
}
