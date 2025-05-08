
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveDestinations;
	Integer						legsWithIncidentSeverity0To3;
	Integer						legsWithIncidentSeverity4To7;
	Integer						legsWithIncidentSeverity8To10;
	List<String>				lastLegFlightCrewMembers;
	List<String>				flightAssignmentsPending;
	List<String>				flightAssignmentsConfirmed;
	List<String>				flightAssignmentsCancelled;
	Double						averageAssignmentsLastMonth;
	Integer						minAssignmentsLastMonth;
	Integer						maxAssignmentsLastMonth;
	Double						stdDevAssignmentsLastMonth;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
