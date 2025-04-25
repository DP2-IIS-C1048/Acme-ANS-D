
package acme.features.manager.dashboard;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.leg.LegStatus;
import acme.realms.manager.Manager;
import forms.ManagerDashboard;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerDashboardRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		int managerId = manager.getId();

		Integer rankingByExperience = this.repository.getExperienceRankingPosition(managerId);
		Integer yearsToRetire = this.repository.getYearsToRetire(managerId);

		Double ratioOnTimeLegs = this.repository.getRatioOnTimeLegs(managerId);
		Double ratioDelayedLegs = this.repository.getRatioDelayedLegs(managerId);

		Integer onTimeLegs = this.repository.countLegsByStatus(managerId, LegStatus.ON_TIME);
		Integer delayedLegs = this.repository.countLegsByStatus(managerId, LegStatus.DELAYED);
		Integer cancelledLegs = this.repository.countLegsByStatus(managerId, LegStatus.CANCELLED);
		Integer landedLegs = this.repository.countLegsByStatus(managerId, LegStatus.LANDED);

		Double averageCost = this.repository.getAverageFlightCost(managerId);
		Double minCost = this.repository.getMinFlightCost(managerId);
		Double maxCost = this.repository.getMaxFlightCost(managerId);
		Double stdDevCost = this.repository.getStandardDeviationFlightCost(managerId);

		Airport mostPopularAirport = this.repository.findAirportsOrderedByPopularityDesc(managerId).stream().findFirst().orElse(null);
		Airport leastPopularAirport = this.repository.findAirportsOrderedByPopularityAsc(managerId).stream().findFirst().orElse(null);

		ManagerDashboard dashboard = new ManagerDashboard();

		dashboard.setRankingPositionManager(rankingByExperience);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOnTimeLegs(ratioOnTimeLegs);
		dashboard.setRatioDelayedLegs(ratioDelayedLegs);
		dashboard.setNumberOfMyLegsWithStatusOnTime(onTimeLegs);
		dashboard.setNumberOfMyLegsWithStatusDelayed(delayedLegs);
		dashboard.setNumberOfMyLegsWithStatusCancelled(cancelledLegs);
		dashboard.setNumberOfMyLegsWithStatusLanded(landedLegs);
		dashboard.setAverageCostFlight(averageCost);
		dashboard.setMinCostFlight(minCost);
		dashboard.setMaxCostFlight(maxCost);
		dashboard.setStandardDeviationCostFlight(stdDevCost);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setLeastPopularAirport(leastPopularAirport);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "rankingPositionManager", "yearsToRetire", "ratioOnTimeLegs", "ratioDelayedLegs", "numberOfMyLegsWithStatusOnTime", "numberOfMyLegsWithStatusDelayed", "numberOfMyLegsWithStatusCancelled",
			"numberOfMyLegsWithStatusLanded", "averageCostFlight", "minCostFlight", "maxCostFlight", "standardDeviationCostFlight", "mostPopularAirport", "leastPopularAirport");

		super.getResponse().addData(dataset);
	}

}
