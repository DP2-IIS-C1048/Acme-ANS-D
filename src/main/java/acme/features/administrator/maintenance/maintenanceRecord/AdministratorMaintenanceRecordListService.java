
package acme.features.administrator.maintenance.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractService;
import acme.client.services.GuiService;
import acme.entities.maintenance.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordListService extends AbstractService<Administrator, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorMaintenanceRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecords;

		maintenanceRecords = this.repository.findMaintenanceRecordsPublished();

		super.getBuffer().addData(maintenanceRecords);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "maintenanceMoment", "status", "inspectionDueDate", "estimatedCost");
		dataset.put("technician", maintenanceRecord.getTechnician().getLicense());
		dataset.put("aircraft", maintenanceRecord.getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);

	}

}
