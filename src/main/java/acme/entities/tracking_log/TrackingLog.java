
package acme.entities.tracking_log;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Past;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import acme.entities.claim.ClaimIndicator;

public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Past()
	@Automapped
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private ClaimIndicator		indicator;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				resolution;

}
