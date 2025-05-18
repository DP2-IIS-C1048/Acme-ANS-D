
package acme.features.authenticated.customer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.customer.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.userAccount.id = :id")
	Customer findCustomerByUserAccountId(int id);
}
