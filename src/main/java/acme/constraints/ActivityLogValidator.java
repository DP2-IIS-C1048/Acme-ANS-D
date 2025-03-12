
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activity_log.ActivityLog;
import acme.entities.leg.LegStatus;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog activityLog, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (activityLog == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean isLegLanded;
			isLegLanded = activityLog.getFlightAssignment().getLeg().getStatus() == LegStatus.LANDED;
			super.state(context, !isLegLanded, "legStatus", "acme.validation.ActivityLog.statusLeg.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
