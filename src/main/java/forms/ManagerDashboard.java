
package forms;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Integer						rankingPositionManager;
	Integer						yearsToRetire;
	Double						ratioOnTimeLegs;
	Double						ratioDelayedLegs;
	Airport						mostPopularAirport;
	Airport						leastPopularAirport;
	Integer						numberOfMyLegsWithStatusOnTime;
	Integer						numberOfMyLegsWithStatusDelayed;
	Integer						numberOfMyLegsWithStatusCancelled;
	Integer						numberOfMyLegsWithStatusLanded;
	Double						averageCostFlight;
	Double						minCostFlight;
	Double						maxCostFlight;
	Double						standardDeviationCostFlight;

}
