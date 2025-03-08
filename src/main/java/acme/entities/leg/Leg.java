
package acme.entities.leg;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	//Pendiente custom validator pero necesito airport
	@ValidString(min = 7, max = 7)
	@Automapped
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;


	@Transient
	private Double getDuration() {
		Double duration;
		Duration aux = Duration.between(this.scheduledDeparture.toInstant(), this.scheduledArrival.toInstant());
		duration = aux.toMinutes() / 60.0;
		return duration;
	}


	@Mandatory
	@Valid
	@Automapped
	private LegStatus status;

	//		@Mandatory
	//		@Valid
	//		@ManyToOne(optional = false)
	//		private Airport		departureAirport;
	//	
	//		@Mandatory
	//		@Valid
	//		@ManyToOne(optional = false)
	//		private Airport		arrivalAirport;
	//	
	//		@Mandatory
	//		@Valid
	//		@ManyToOne(optional = false)
	//		private Aircraft	aircraft;

}
