
package acme.components;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CurrencyExchange extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@Automapped
	private String				fromCurrency;

	@Mandatory
	@Valid
	@Automapped
	private String				toCurrency;

	@Mandatory
	@Valid
	@Automapped
	private Double				rate;
}
