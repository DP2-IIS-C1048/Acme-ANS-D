
package acme.entities.maintenance_records;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;

public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	private TaskType			type;

	@Mandatory
	@ValidString
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 24)
	private Integer				duration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private MaintenanceRecord	maintenanceRecord;

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Technician			technician;

}
