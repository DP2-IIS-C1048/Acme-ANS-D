
package acme.forms.customer;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<String>				lastFiveDestinations;
	Double						moneySpentInLastYear; //In EUR
	Map<String, Long>			bookingsGroupedByTravelClass;
	Long						bookingCountInLastFiveYears;
	Double						bookingAverageCostInLastFiveYears; //In EUR
	Double						bookingMinCostInLastFiveYears; //In EUR
	Double						bookingMaxCostInLastFiveYears; //In EUR
	Double						bookingCostStdDevInLastFiveYears; //In EUR
	Long						passengerBookingCount;
	Double						passengerAverage;
	Long						passengerMin;
	Long						passengerMax;
	Double						passengerStdDev;
}
