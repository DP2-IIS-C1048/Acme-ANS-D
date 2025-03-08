
package acme.entities.customer;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$", max = 9, min = 8)
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$", min = 6, max = 16)
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255, min = 1)
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@ValidString(max = 50, min = 1)
	@Automapped
	private String				city;

	@Mandatory
	@ValidString(max = 50, min = 1)
	@Automapped
	private String				country;

	@Optional
	@ValidNumber(max = 50000, min = 0)
	@Automapped
	private Integer				earnedPionts;

}
