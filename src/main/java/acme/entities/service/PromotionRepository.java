
package acme.entities.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PromotionRepository extends AbstractRepository {

	@Query("select p.promotionCode from Promotion p")
	List<String> findAllPromotionCodes();

}
