
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.ExchangeRate;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.Involves;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceStatus;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;


	@Override
	public void authorise() {

		boolean status;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		Technician technician;

		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		technician = maintenanceRecord == null ? null : maintenanceRecord.getTechnician();
		status = maintenanceRecord != null && maintenanceRecord.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		if (status) {
			String method;
			int aircraftId;
			Aircraft aircraft;

			method = super.getRequest().getMethod();

			if (method.equals("GET"))
				status = true;
			else {
				aircraftId = super.getRequest().getData("aircraft", int.class);
				aircraft = this.repository.findAircraftById(aircraftId);
				status = aircraft != null || aircraftId == 0;
			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		{
			if (maintenanceRecord.getEstimatedCost() != null && maintenanceRecord.getEstimatedCost().getCurrency() != null) {
				boolean validCurrency = ExchangeRate.isValidCurrency(maintenanceRecord.getEstimatedCost().getCurrency());
				super.state(validCurrency, "estimatedCost", "acme.validation.currency.message");
			}
		}
		{
			boolean isMaintenanceRecordCompleted = maintenanceRecord.getStatus().equals(MaintenanceStatus.COMPLETED);
			super.state(isMaintenanceRecordCompleted, "status", "acme.validation.maintenance-record.valid-publication-status");
		}
		{
			Collection<Involves> maintenanceRecordInvolves;
			boolean maintenanceRecordHasTasks;
			boolean maintenanceRecordHasAllTasksPublished;
			boolean maintenanceRecordHasNotDuplicatedTasks;

			maintenanceRecordInvolves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecord.getId());
			maintenanceRecordHasTasks = !maintenanceRecordInvolves.isEmpty();
			maintenanceRecordHasAllTasksPublished = maintenanceRecordInvolves.stream().map(Involves::getTask).allMatch(t -> !t.isDraftMode());
			maintenanceRecordHasNotDuplicatedTasks = maintenanceRecordInvolves.stream().map(Involves::getTask).toList().size() == maintenanceRecordInvolves.stream().map(Involves::getTask).collect(Collectors.toSet()).size();

			super.state(maintenanceRecordHasTasks, "*", "acme.validation.maintenance-record.has-tasks");
			super.state(maintenanceRecordHasAllTasksPublished, "*", "acme.validation.maintenance-record.all-tasks-are-published");
			super.state(maintenanceRecordHasNotDuplicatedTasks, "*", "acme.validation.maintenance-record.tasks-are-not-duplicated");
		}
		{
			if (maintenanceRecord.getInspectionDueDate() != null) {
				boolean inspectionDueDateIsAfterMaintenanceMoment = MomentHelper.isAfter(maintenanceRecord.getInspectionDueDate(), maintenanceRecord.getMaintenanceMoment());
				super.state(inspectionDueDateIsAfterMaintenanceMoment, "inspectionDueDate", "acme.validation.maintenance-record.inspectionDueDate-is-after-maintenanceMoment");
			}
		}
		{
			boolean maintenanceRecordIsInDraftMode = maintenanceRecord.isDraftMode();
			super.state(maintenanceRecordIsInDraftMode, "*", "acme.validation.maintenance-record.is-in-draft-mode");
		}
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraftChoices;

		choices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getStatus());
		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "inspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("statuses", choices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
