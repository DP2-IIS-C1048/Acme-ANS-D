
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidPromotion;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidPromotion
public class Promotion extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString
	@Column(unique = true)
	private String				code;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				moneyToDiscount;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
