
package acme.features.assistanceagent.trackinglog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.tracking_log.TrackingLog;
import acme.entities.tracking_log.TrackingLogIndicator;
import acme.realms.assistanceagent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;
		Claim claim;
		AssistanceAgent assistanceAgent;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		claim = trackingLog == null ? null : trackingLog.getClaim();
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && trackingLog != null && !claim.isDraftMode() && trackingLog.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog trackingLog;
		int id;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackinglog) {
		super.bindObject(trackinglog, "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		super.state(trackingLog.isDraftMode(), "draftMode", "acme.validation.trackingLog.draftMode.message");
	}

	@Override
	public void perform(final TrackingLog trackingLog) {
		trackingLog.setDraftMode(false);
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
