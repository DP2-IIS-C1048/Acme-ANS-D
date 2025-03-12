
package acme.entities.maintenance;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.realms.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment
	@Automapped
	private Date				maintenanceMoment;

	@Mandatory
	@Valid
	@Automapped
	private MaintenanceStatus	status;

	@Mandatory
	@ValidMoment
	@Automapped
	private Date				inspectionDueDate;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				estimatedCost;

	@Optional
	@ValidString
	@Automapped
	private String				notes;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
