
package acme.features.assistanceagent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.tracking_log.TrackingLog;
import acme.realms.assistanceagent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int claimId;
		Claim claim;
		AssistanceAgent assistanceAgent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(id);

		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {
		super.bindObject(claim, "passengerEmail", "description", "type", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		boolean thereIsNotAnyTrackingLogs = true;
		Collection<TrackingLog> trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());
		thereIsNotAnyTrackingLogs = trackingLogs.isEmpty();
		super.state(thereIsNotAnyTrackingLogs, "*", "acme.validation.claim.thereIsTrackingLogs.message");

	}

	@Override
	public void perform(final Claim claim) {
		Collection<TrackingLog> trackingLogs;
		trackingLogs = this.repository.findAllTrackingLogsByClaimId(claim.getId());
		this.repository.deleteAll(trackingLogs);
		this.repository.delete(claim);

	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		Collection<Leg> legs;
		SelectChoices typeChoices;
		SelectChoices legChoices;
		int assistanceAgentId;
		AssistanceAgent assistanceAgent;

		typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
		legs = this.repository.findAllPublishedLegsByAirlineId(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());
		if (!legs.contains(claim.getLeg()))
			claim.setLeg(null);
		legChoices = SelectChoices.from(legs, "LegLabel", claim.getLeg());

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "draftMode", "leg");
		dataset.put("types", typeChoices);
		dataset.put("legs", legChoices);
		dataset.put("trackingLogType", claim.getIndicator());

		super.getResponse().addData(dataset);

	}
}
