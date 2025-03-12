
package acme.realms.technician;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface TechnicianRepository extends AbstractRepository {

	@Query("select t from Technician t where t.license = :license")
	Technician findTechnicianByLicense(String license);

}
