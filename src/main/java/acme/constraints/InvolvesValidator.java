
package acme.constraints;

import java.util.Collection;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.maintenance.Involves;
import acme.features.technician.involves.TechnicianInvolvesRepository;

@Validator
public class InvolvesValidator extends AbstractValidator<ValidInvolves, Involves> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository involvesRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidInvolves annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Involves involves, final ConstraintValidatorContext context) {
		boolean result;

		if (involves == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniqueInvolves;
			Collection<Involves> existingInvolves;

			existingInvolves = this.involvesRepository.findInvolvesByMaintenanceRecordId(involves.getMaintenanceRecord().getId());
			uniqueInvolves = existingInvolves == null || !existingInvolves.stream().filter(inv -> inv.getId() != involves.getId()).map(Involves::getTask).toList().contains(involves.getTask());

			super.state(context, uniqueInvolves, "task", "acme.validation.technician.duplicated-involves.message");
		}

		result = !super.hasErrors(context);
		return result;
	}

}
