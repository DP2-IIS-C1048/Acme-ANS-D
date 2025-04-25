
package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;
import acme.entities.leg.LegStatus;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("SELECT COUNT(m) +1 FROM Manager m WHERE m.yearsOfExperience > (SELECT m2.yearsOfExperience FROM Manager m2 WHERE m2.id = :managerId)")
	Integer getExperienceRankingPosition(Integer managerId);

	@Query("SELECT 65 - (FUNCTION('YEAR', CURRENT_DATE) - FUNCTION('YEAR', m.dateOfBirth)) FROM Manager m WHERE m.id = :managerId")
	Integer getYearsToRetire(Integer managerId);

	@Query("select 1.0 * count(a) / (select count(b) from Leg b WHERE b.flight.manager.id = :managerId) from Leg a WHERE a.flight.manager.id = :managerId AND a.status = acme.entities.leg.LegStatus.ON_TIME")
	Double getRatioOnTimeLegs(Integer managerId);

	@Query("select 1.0 * count(a) / (select count(b) from Leg b WHERE b.flight.manager.id = :managerId) from Leg a WHERE a.flight.manager.id = :managerId AND a.status = acme.entities.leg.LegStatus.DELAYED")
	Double getRatioDelayedLegs(Integer managerId);

	@Query("""
		    SELECT a FROM Leg l
		    JOIN Airport a ON a = l.departureAirport OR a = l.arrivalAirport
		    WHERE l.flight.manager.id = :managerId
		    GROUP BY a
		    ORDER BY COUNT(a) DESC
		""")
	List<Airport> findAirportsOrderedByPopularityDesc(Integer managerId);

	@Query("""
		    SELECT a FROM Leg l
		    JOIN Airport a ON a = l.departureAirport OR a = l.arrivalAirport
		    WHERE l.flight.manager.id = :managerId
		    GROUP BY a
		    ORDER BY COUNT(a) ASC
		""")
	List<Airport> findAirportsOrderedByPopularityAsc(Integer managerId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight.manager.id = :managerId AND l.status = :status")
	Integer countLegsByStatus(Integer managerId, LegStatus status);

	@Query("SELECT AVG(f.cost.amount) FROM Flight f WHERE f.manager.id = :managerId")
	Double getAverageFlightCost(Integer managerId);

	@Query("SELECT MIN(f.cost.amount) FROM Flight f WHERE f.manager.id = :managerId")
	Double getMinFlightCost(Integer managerId);

	@Query("SELECT MAX(f.cost.amount) FROM Flight f WHERE f.manager.id = :managerId")
	Double getMaxFlightCost(Integer managerId);

	@Query("""
		    SELECT STDDEV(f.cost.amount)
		    FROM Flight f
		    WHERE f.manager.id = :managerId
		""")
	Double getStandardDeviationFlightCost(Integer managerId);

}
