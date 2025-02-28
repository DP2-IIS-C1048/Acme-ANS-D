
package acme.entities.maintenance_records;

import java.util.Date;

import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;

public class MaintenanceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	private Date				moment;

	@Mandatory
	@Valid
	private MaintenanceStatus	status;

	@Mandatory
	@ValidMoment
	private Date				inspectionDueDate;

	@Mandatory
	@ValidMoney
	private Money				estimatedCost;

	@Optional
	@ValidString
	private String				notes;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;
}
