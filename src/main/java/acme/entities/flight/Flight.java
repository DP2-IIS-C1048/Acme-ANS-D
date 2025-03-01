
package acme.entities.flight;

import javax.persistence.Entity;

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
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String						tag;

	@Mandatory
	@Automapped
	private Boolean						indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money						cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String						description;

	// Relationships ----------------------------------------------------------

}
