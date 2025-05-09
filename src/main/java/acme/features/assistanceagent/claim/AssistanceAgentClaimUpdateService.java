
package acme.features.assistanceagent.claim;

import java.util.Arrays;
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
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "leg");
	}

	@Override
	public void validate(final Claim claim) {
		Collection<Leg> legs;
		Collection<ClaimType> claimTypes;
		ClaimType type;
		int legId;
		Leg leg;
		int assistanceAgentId;
		AssistanceAgent assistanceAgent;
		boolean isNotWrongLeg = true;
		boolean isNotWrongType = true;
		boolean isNotWrongLeg2 = true;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		assistanceAgent = this.repository.findAssistanceAgentById(assistanceAgentId);
		legs = this.repository.findAllPublishedLegsByAirlineId(MomentHelper.getCurrentMoment(), assistanceAgent.getAirline().getId());

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.finLegById(legId);
		isNotWrongLeg2 = legs.contains(leg);

		claimTypes = Arrays.asList(ClaimType.values());
		type = super.getRequest().getData("type", ClaimType.class);
		isNotWrongType = claimTypes.contains(type);

		if (claim.getLeg() != null && claim.getRegistrationMoment() != null)
			isNotWrongLeg = claim.getRegistrationMoment().after(claim.getLeg().getScheduledArrival());

		super.state(isNotWrongLeg2, "leg", "acme.validation.claim.wrongLeg2.message");
		super.state(isNotWrongType, "type", "acme.validation.claim.wrongType.message");
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
		legChoices = SelectChoices.from(legs, "flightNumber", claim.getLeg());

		dataset = super.unbindObject(claim, "passengerEmail", "description", "type", "draftMode", "leg");
		dataset.put("types", typeChoices);
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);

	}

}
