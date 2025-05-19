
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
import acme.realms.assistanceagent.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		AssistanceAgent assistanceAgent;
		Claim claim;
		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimById(claimId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		if (status) {
			String method;
			int legId, assistanceAgentId;
			Leg leg;
			Collection<Leg> legs;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
				assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
				claimId = super.getRequest().getData("id", int.class);
				claim = this.repository.findClaimById(claimId);
				legId = super.getRequest().getData("leg", int.class);
				legs = this.repository.findAllPublishedLegsByAirlineId(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());
				leg = this.repository.finLegById(legId);
				status = (legId == 0 || legs.contains(leg)) && claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);
			}
		}

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
		boolean isNotWrongLeg = true;

		if (claim.getLeg() != null && claim.getRegistrationMoment() != null)
			isNotWrongLeg = claim.getRegistrationMoment().after(claim.getLeg().getScheduledArrival());

		super.state(isNotWrongLeg, "leg", "acme.validation.claim.wrongLeg.message");
	}

	@Override
	public void perform(final Claim claim) {
		this.repository.save(claim);
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

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "draftMode", "leg");
		dataset.put("types", typeChoices);
		dataset.put("legs", legChoices);
		dataset.put("trackingLogType", claim.getIndicator());

		super.getResponse().addData(dataset);

	}

}
