
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.ExchangeRate;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenance.MaintenanceRecord;
import acme.entities.maintenance.MaintenanceStatus;
import acme.realms.technician.Technician;

@GuiService
public class TechnicianMaintenanceRecordCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

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

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		Date now;

		now = MomentHelper.getCurrentMoment();
		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		maintenanceRecord = new MaintenanceRecord();
		maintenanceRecord.setDraftMode(true);
		maintenanceRecord.setMaintenanceMoment(now);
		maintenanceRecord.setTechnician(technician);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "inspectionDueDate", "estimatedCost", "notes");
		maintenanceRecord.setAircraft(aircraft);
		maintenanceRecord.setStatus(MaintenanceStatus.PENDING);
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
			if (maintenanceRecord.getInspectionDueDate() != null) {
				boolean inspectionDueDateIsAfterMaintenanceMoment = MomentHelper.isAfter(maintenanceRecord.getInspectionDueDate(), maintenanceRecord.getMaintenanceMoment());
				super.state(inspectionDueDateIsAfterMaintenanceMoment, "inspectionDueDate", "acme.validation.maintenance-record.inspectionDueDate-is-after-maintenanceMoment");
			}
		}
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraftChoices;

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "inspectionDueDate", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
