
package acme.realms;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Automapped
	private String				employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languageSkills; //Set of languages ​​separated by ";"

	@Mandatory
	@Valid
	@Automapped
	private AvailabilityStatus	availabilityStatus;

	/*
	 * @Mandatory
	 * 
	 * @Valid
	 * 
	 * @OneToOne
	 * private Airlane airlane;
	 */

	@Mandatory
	@ValidMoney(min = 0, max = 150000)
	@Automapped
	private Money				salary;

	@Optional
	@ValidNumber(min = 1, max = 80)
	@Automapped
	private Integer				yearsOfExperience;
}
