<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="flight-crew-member.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.last-five-destinations"/>
		</th>
		<td>
			<acme:print value="${lastFiveDestinations}"/>
		</td>
	</tr>
	
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.legs-severity-0-to-3"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity0To3}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.legs-severity-4-to-7"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity4To7}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.legs-severity-8-to-10"/>
		</th>
		<td>
			<acme:print value="${legsWithIncidentSeverity8To10}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.last-leg-flight-crew-members"/>
		</th>
		<td>
			<acme:print value="${lastLegFlightCrewMembers}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.pending-flight-assignments"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsPending}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.confirmed-flight-assignments"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsConfirmed}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.cancelled-flight-assignments"/>
		</th>
		<td>
			<acme:print value="${flightAssignmentsCancelled}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.average-assignments-per-month"/>
		</th>
		<td>
			<acme:print value="${averageAssignmentsLastMonth}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.min-assignments-per-month"/>
		</th>
		<td>
			<acme:print value="${minAssignmentsLastMonth}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.max-assignments-per-month"/>
		</th>
		<td>
			<acme:print value="${maxAssignmentsLastMonth}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="flight-crew-member.dashboard.form.label.stddev-assignments-last-month"/>
		</th>
		<td>
			<acme:print value="${stdDevAssignmentsLastMonth}"/>
		</td>
	</tr>
</table>

<acme:return/>