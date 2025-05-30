
package acme.features.assistanceagent.trackinglog;

import java.util.Collection;
import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.tracking_log.TrackingLog;
import acme.entities.tracking_log.TrackingLogIndicator;
import acme.realms.assistanceagent.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int trackingLogId;
		TrackingLog trackingLog;

		trackingLogId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(trackingLogId);
		status = trackingLog != null && trackingLog.getClaim() != null && trackingLog.isDraftMode() && !trackingLog.getClaim().isDraftMode() && super.getRequest().getPrincipal().hasRealm(trackingLog.getClaim().getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id;
		TrackingLog trackingLog;

		id = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findTrackingLogById(id);

		super.getBuffer().addData(trackingLog);
	}

	@Override
	public void bind(final TrackingLog trackinglog) {
		super.bindObject(trackinglog, "step", "resolutionPercentage", "indicator", "resolution");
	}

	@Override
	public void validate(final TrackingLog trackingLog) {
		boolean isNotWrongResolutionPercentage = true;
		boolean isNotWrongResolutionPercentage2 = true;
		boolean isNotWrongResolution = true;
		boolean isNotWrongResolution2 = true;
		boolean isNotMaxCompleted = true;
		boolean isWrongResolutionPercentage4 = true;

		if (trackingLog.getResolutionPercentage() != null && trackingLog.getResolutionPercentage() < 100.0 && trackingLog.getIndicator() != null)
			isNotWrongResolutionPercentage = trackingLog.getIndicator().equals(TrackingLogIndicator.PENDING);
		else if (trackingLog.getIndicator() != null)
			isNotWrongResolutionPercentage2 = !trackingLog.getIndicator().equals(TrackingLogIndicator.PENDING);

		if (trackingLog.getIndicator() != null && trackingLog.getIndicator().equals(TrackingLogIndicator.PENDING))
			isNotWrongResolution = trackingLog.getResolution() == null || trackingLog.getResolution().isBlank();
		else
			isNotWrongResolution2 = trackingLog.getResolution() != null && !trackingLog.getResolution().isBlank();

		if (trackingLog.getClaim() != null) {
			TrackingLog highestTrackingLog;
			Collection<TrackingLog> trackingLogs = this.repository.findOrderTrackingLogs(trackingLog.getClaim().getId());
			if (trackingLog.getResolutionPercentage() != null && trackingLogs.size() > 0) {
				highestTrackingLog = trackingLogs.stream().max(Comparator.comparingDouble(TrackingLog::getResolutionPercentage)).get();
				long completedTrackingLogs = trackingLogs.stream().filter(t -> t.getResolutionPercentage().equals(100.00)).count();
				if (highestTrackingLog.getId() != trackingLog.getId()) {
					if (highestTrackingLog.getResolutionPercentage() < trackingLog.getResolutionPercentage())
						isWrongResolutionPercentage4 = false;
					if (highestTrackingLog.getResolutionPercentage() == 100 && trackingLog.getResolutionPercentage() == 100)
						isNotMaxCompleted = !highestTrackingLog.isDraftMode() && completedTrackingLogs < 2;
				}
			}

		}

		super.state(!trackingLog.getClaim().isDraftMode(), "draftMode", "acme.validation.trackingLog.claimDraftMode.message");
		super.state(isNotWrongResolutionPercentage, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage.message");
		super.state(isNotWrongResolutionPercentage2, "resolutionPercentage", "acme.validation.trackingLog.resolutionPercentage2.message");
		super.state(isNotWrongResolution, "resolution", "acme.validation.trackingLog.isNotWrongResolution.message");
		super.state(isNotWrongResolution2, "resolution", "acme.validation.trackingLog.isNotWrongResolution2.message");
		super.state(isNotMaxCompleted, "resolutionPercentage", "acme.validation.trackingLog.isNotMaxCompleted.message");
		super.state(isWrongResolutionPercentage4, "resolutionPercentage", "acme.validation.trackingLog.isWrongResolutionPercentage4.message");

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

		dataset = super.unbindObject(trackingLog, "lastUpdateMoment", "step", "resolutionPercentage", "indicator", "resolution", "draftMode");
		dataset.put("indicator", indicatorChoices);
		dataset.put("masterId", trackingLog.getClaim().getId());

		super.getResponse().addData(dataset);
	}

}
