
package acme.entities.activity_log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidActivityLog;
import acme.entities.flight_assignment.FlightAssignment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true, min = "2000/01/01 00:00")
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				typeOfIncident;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10)
	@Automapped
	private Integer				severityLevel;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

}
