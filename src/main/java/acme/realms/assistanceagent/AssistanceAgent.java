
package acme.realms.assistanceagent;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidAssistanceAgent;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidAssistanceAgent
public class AssistanceAgent extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(min = 8, max = 9, pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(min = 1, max = 255)
	@Automapped
	private String				spokenLanguages;

	@Mandatory
	@ValidMoment(min = "2000/01/01 00:00:00", past = true)
	@Automapped
	private Date				moment;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				briefBio;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@Valid
	@Automapped
	private String				photo;

	@Mandatory
	@Valid
	@ManyToOne
	private Airline				airline;

}
