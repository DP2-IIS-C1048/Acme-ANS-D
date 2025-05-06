
package acme.realms.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerRepository extends AbstractRepository {

	@Query("Select c from Customer c where c.identifier = :identifier")
	Customer findCustomerByIdentifier(String identifier);

}
