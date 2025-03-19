
package acme.entities.maintenance;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private TaskType			type;

	@Mandatory
	@ValidString(min = 1)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 0)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 6000, integer = 4, fraction = 0)
	@Automapped
	private Integer				estimatedDuration;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Optional
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
