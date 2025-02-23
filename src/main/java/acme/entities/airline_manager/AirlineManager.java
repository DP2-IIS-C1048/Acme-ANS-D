
package acme.entities.airline_manager;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AirlineManager extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Identifier must have 2-3 uppercase letters followed by 6 digits.")
	@Column(unique = true)
	@Automapped
	private String				identifierNumber;

	@Mandatory
	@Min(value = 0, message = "Years of experience cannot be negative.")
	@Automapped
	private Integer				yearsOfExperience;

	@Mandatory
	@Past(message = "Birth date must be in the past.")
	@Automapped
	private Date				birth;

	@Optional
	@Automapped
	private String				picture;
}
