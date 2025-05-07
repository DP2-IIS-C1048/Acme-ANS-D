
package acme.features.assistanceagent.trackinglog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.tracking_log.TrackingLog;
import acme.entities.tracking_log.TrackingLogIndicator;
import acme.realms.assistanceagent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int masterId;
		TrackingLog trackingLog;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findClaimById(masterId);

		trackingLog = new TrackingLog();
		trackingLog.setClaim(claim);
		trackingLog.setDraftMode(true);
		trackingLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackinglog) {

		super.bindObject(trackinglog, "step", "resolutionPercentage", "indicator", "resolution");

	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		;
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		this.repository.save(trackingLog);
	}

	@Override
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicatorChoices;

		indicatorChoices = SelectChoices.from(TrackingLogIndicator.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("indicator", indicatorChoices);
		dataset.put("masterId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);
	}

}
