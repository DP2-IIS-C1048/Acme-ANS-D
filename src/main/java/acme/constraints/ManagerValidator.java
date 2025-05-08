
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.manager.Manager;
import acme.realms.manager.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, Manager> {

	@Autowired
	private ManagerRepository repository;

	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueIdentifierNumber;
			Manager existingManager;

			existingManager = this.repository.findManagerByIndentifier(manager.getIdentifierNumber());
			uniqueIdentifierNumber = existingManager == null || manager.equals(existingManager);

			super.state(context, uniqueIdentifierNumber, "flightNumber",
					"acme.validation.manager.duplicated-identifierNumber.message");
		}
		{
			boolean validIdentifierNumber = false;
			String name = manager.getIdentity().getName();
			String surName = manager.getIdentity().getSurname();
			String identifierNumber = manager.getIdentifierNumber();

			if (name.charAt(0) == identifierNumber.charAt(0) && surName.charAt(0) == identifierNumber.charAt(1))
				validIdentifierNumber = true;
			super.state(context, validIdentifierNumber, "identifier",
					"acme.validation.manager.invalid-identifierNumber.message");

		}
		result = !super.hasErrors(context);

		return result;

	}

}
