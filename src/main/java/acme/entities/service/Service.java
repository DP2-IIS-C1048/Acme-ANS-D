
package acme.entities.service;

import javax.persistence.Column;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;

public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				picture;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$") // Los dos ultimos digitos deben pertenecer al año actual
	@Column(unique = true)
	private String				promotionCode;

	@Optional
	@ValidMoney
	@Column(unique = true) // Tal vez deba ser una entidad a parte de tipo Discount
	private Money				moneyToDiscount;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
