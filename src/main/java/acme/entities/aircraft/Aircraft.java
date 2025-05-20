
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidAircraft;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAircraft
@Table(indexes = {
	//Feature Leg
	@Index(columnList = "status")//

})
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Automapped
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@ValidNumber(min = 1, max = 255)
	@Automapped
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	@Automapped
	private Integer				cargoWeight;

	@Mandatory
	@Valid
	@Automapped
	private AircraftStatus		status;

	@Optional
	@ValidString(min = 0, max = 255)
	@Automapped
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
