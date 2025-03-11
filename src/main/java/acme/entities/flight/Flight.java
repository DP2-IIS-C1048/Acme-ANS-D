
package acme.entities.flight;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				description;


	@Transient
	public Leg getFirstLeg() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findFirstLegByFlightId(this.getId());
	}

	@Transient
	public Leg getLastLeg() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		return repository.findLastLegByFlightId(this.getId());
	}

	@Transient
	public Date getScheduledDeparture() {
		Leg firstLeg = this.getFirstLeg();
		return firstLeg != null ? firstLeg.getScheduledDeparture() : null;
	}

	@Transient
	public Date getScheduledArrival() {
		Leg lastLeg = this.getLastLeg();
		return lastLeg != null ? lastLeg.getScheduledArrival() : null;
	}

	@Transient
	public String getOriginCity() {
		Leg firstLeg = this.getFirstLeg();
		return firstLeg != null && firstLeg.getAirport() != null ? firstLeg.getAirport().getCity() : null;
	}

	@Transient
	public String getDestinationCity() {
		Leg lastLeg = this.getLastLeg();
		return lastLeg != null && lastLeg.getAirport() != null ? lastLeg.getAirport().getCity() : null;
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository;

		repository = SpringHelper.getBean(FlightRepository.class);
		return repository.getNumbersOfLegsByFlightId(this.getId());
	}

	// Relationships ----------------------------------------------------------


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline airline;

}
