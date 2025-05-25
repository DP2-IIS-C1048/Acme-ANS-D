
package acme.entities.leg;

import java.time.Duration;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidLeg
@Table(indexes = {
	//Feature Leg
	@Index(columnList = "flight_id,draftMode,scheduledDeparture,scheduledArrival"), // 
	@Index(columnList = "aircraft_id,draftMode,scheduledDeparture,scheduledArrival"),// 
	@Index(columnList = "flight_id,draftMode,scheduledArrival"),// 
	@Index(columnList = "flight_id,draftMode,scheduledDeparture"), // 
	@Index(columnList = "flight_id,draftMode"),//
	//Feature Claim
	@Index(columnList = "flight_id,draftMode,scheduledArrival,aircraft_id"),//
	@Index(columnList = "draftMode"),//
	@Index(columnList = "draftMode, scheduledDeparture")

})
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}\\d{4}$")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment(past = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	private boolean				draftMode;


	@Transient
	private double getDuration() {
		double duration;
		Duration aux = Duration.between(this.scheduledDeparture.toInstant(), this.scheduledArrival.toInstant());
		duration = aux.toMinutes() / 60.0;
		return duration;
	}

	@Transient
	public String getLegLabel() {
		return this.flightNumber + ": " + this.getDepartureAirport().getCity() + " " + this.getScheduledDeparture() + " - " + this.getArrivalAirport().getCity() + " " + this.getScheduledArrival();
	}


	@Mandatory
	@Valid
	@Automapped
	private LegStatus	status;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

}
