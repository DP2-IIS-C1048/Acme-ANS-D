
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
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		Claim claim;

		trackingLogId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimByTrackinglogId(trackingLogId);
		status = claim != null && !claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

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
	public void unbind(final TrackingLog trackingLog) {
		Dataset dataset;
		SelectChoices indicatorChoices;

		indicatorChoices = SelectChoices.from(TrackingLogIndicator.class, trackingLog.getIndicator());

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("indicator", indicatorChoices);
		dataset.put("masterId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);

	}

}
