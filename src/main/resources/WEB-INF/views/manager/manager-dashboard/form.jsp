<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.ranking-position"/></th>
		<td><acme:print value="${rankingPositionManager}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.years-to-retire"/></th>
		<td><acme:print value="${yearsToRetire}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.average-cost"/></th>
		<td><acme:print value="${averageCostFlight}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.min-cost"/></th>
		<td><acme:print value="${minCostFlight}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.max-cost"/></th>
		<td><acme:print value="${maxCostFlight}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.standard-deviation"/></th>
		<td><acme:print value="${standardDeviationCostFlight}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.most-popular-airport"/></th>
		<td><acme:print value="${mostPopularAirport}"/></td>
	</tr>
	<tr>
		<th scope="row"><acme:print code="manager.dashboard.form.label.least-popular-airport"/></th>
		<td><acme:print value="${leastPopularAirport}"/></td>
	</tr>
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.legs-status-ratio"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function () {
		var data = {
			labels: ["ON_TIME", "DELAYED", "CANCELLED", "LANDED"],
			datasets: [{
				data: [
					<jstl:out value="${numberOfMyLegsWithStatusOnTime}" />,
					<jstl:out value="${numberOfMyLegsWithStatusDelayed}" />,
					<jstl:out value="${numberOfMyLegsWithStatusCancelled}" />,
					<jstl:out value="${numberOfMyLegsWithStatusLanded}" />
				]
			}]
		};

		var options = {
			scales: {
				yAxes: [{
					ticks: {
						beginAtZero: true
					}
				}]
			},
			legend: {
				display: false
			}
		};

		var canvas = document.getElementById("canvas");
		var context = canvas.getContext("2d");
		new Chart(context, {
			type: "bar",
			data: data,
			options: options
		});
	});
</script>

<acme:return/>
