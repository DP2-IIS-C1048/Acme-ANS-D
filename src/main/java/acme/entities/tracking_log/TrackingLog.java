
package acme.entities.tracking_log;

import java.util.Date;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long		serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00:00", past = true)
	@Automapped
	private Date					lastUpdateMoment;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String					step;

	@Mandatory
	@ValidScore
	@Automapped
	private Double					resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private TrackingLogIndicator	indicator;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String					resolution;

}
