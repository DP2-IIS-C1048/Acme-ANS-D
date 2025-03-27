/*
 * Dashboard.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.flight_crew_member.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	List<String>				lastFiveAssignedDestinations;
	List<Leg>					legsCountByIncidentSeverity;
	List<FlightCrewMember>		crewMembersLastLeg;
	List<FlightAssignment>		flightAssignmentsByStatus;
	Double						averageFlightAssignmentsLastMonth;
	Double						minFlightAssignmentsLastMonth;
	Double						maxFlightAssignmentsLastMonth;
	Double						stdDevFlightAssignmentsLastMonth;
	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
