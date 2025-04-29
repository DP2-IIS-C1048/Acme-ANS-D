
package acme.features.flight_crew_member.flight_assignments;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiController
public class FlightCrewMemberFlightAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private CompletedFlightAssignmentListService			completedFlightAssignmentListService;

	@Autowired
	private UncompletedFlightAssignmentListService			uncompletedFlightAssignmentListService;

	@Autowired
	private FlightCrewMemberFlightAssignmentShowService		showService;

	@Autowired
	private FlightCrewMemberFlightAssignmentCreateService	createService;

	@Autowired
	private FlightCrewMemberFlightAssignmentUpdateService	updateService;

	@Autowired
	private FlightCrewMemberFlightAssignmentDeleteService	deleteService;

	@Autowired
	private FlightCrewMemberFlightAssignmentPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("list-completed", "list", this.completedFlightAssignmentListService);
		super.addCustomCommand("list-uncompleted", "list", this.uncompletedFlightAssignmentListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
