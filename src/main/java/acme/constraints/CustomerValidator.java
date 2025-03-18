/*
 * JobValidator.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.customer.Customer;
import acme.realms.customer.CustomerRepository;

@Validator
public class CustomerValidator extends AbstractValidator<ValidCustomer, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidCustomer annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (customer == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueCustomer;
				Customer existingCustomer;

				existingCustomer = this.repository.findCustomerByIdentifier(customer.getIdentifier());
				uniqueCustomer = existingCustomer == null || existingCustomer.equals(customer);

				super.state(context, uniqueCustomer, "identifier", "acme.validation.customer.duplicated-identifier.message");
			}
			{
				boolean validIdentifier = false;
				String name = customer.getIdentity().getName();
				String surName = customer.getIdentity().getSurname();
				String identifier = customer.getIdentifier();

				if (name.charAt(0) == identifier.charAt(0) && surName.charAt(0) == identifier.charAt(1))
					validIdentifier = true;
				super.state(context, validIdentifier, "identifier", "acme.validation.customer.invalid-identifier.message");

			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
