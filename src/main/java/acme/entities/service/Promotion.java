
package acme.entities.service;

import javax.persistence.Column;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;

public class Promotion extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$") // Los dos ultimos digitos deben pertenecer al año actual
	@Column(unique = true)
	private String				code;

	@Optional
	@ValidMoney
	@Automapped
	private Money				moneyToDiscount;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
