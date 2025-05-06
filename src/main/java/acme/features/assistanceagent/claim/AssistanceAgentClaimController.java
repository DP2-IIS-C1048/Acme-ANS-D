
package acme.features.assistanceagent.claim;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claim.Claim;
import acme.realms.assistanceagent.AssistanceAgent;

@GuiController
public class AssistanceAgentClaimController extends AbstractGuiController<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentCompletedClaimListService	listService;

	@Autowired
	private AssistanceAgentPendingClaimListService		pendingListService;

	@Autowired
	private AssistanceAgentClaimShowService				showService;

	@Autowired
	private AssistanceAgentClaimCreateService			createService;

	@Autowired
	private AssistanceAgentClaimDeleteService			deleteService;

	@Autowired
	private AssistanceAgentClaimPublishService			publishService;

	@Autowired
	private AssistanceAgentClaimUpdateService			updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("pending", "list", this.pendingListService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
