
package acme.entities.assistance_agent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "Identifier must have 2-3 uppercase letters followed by 6 digits.")
	@Column(unique = true)
	@Automapped
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255, message = "List can not be larger than 255 characters")
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@Automapped
	private String				airline;

	@Mandatory
	@Past(message = "Moment on which the agent began to work for the airline must be in the past.")
	@Automapped
	private Date				moment;

	@Optional
	@Automapped
	@ValidString(max = 255, message = "Brief bio can not be larger than 255 characters")
	private String				briefBio;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@Automapped
	private String				photo;

}
