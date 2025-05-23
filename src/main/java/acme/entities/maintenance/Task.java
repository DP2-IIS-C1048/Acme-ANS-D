
package acme.entities.maintenance;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.technician.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draft_mode")
})
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
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				priority;

	@Mandatory
	@ValidNumber(min = 0, max = 6000)
	@Automapped
	private Integer				estimatedDuration;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
