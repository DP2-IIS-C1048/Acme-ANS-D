
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.flight_crew_member.FlightCrewMember;
import acme.realms.flight_crew_member.FlightCrewMemberRepository;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightCrewMemberRepository repository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember flightCrewMember, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (flightCrewMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueFlightCrewMember;
				FlightCrewMember existingFlightCrewMember;

				existingFlightCrewMember = this.repository.findFlightCrewMemberByemployeeCode(flightCrewMember.getEmployeeCode());
				uniqueFlightCrewMember = existingFlightCrewMember == null || existingFlightCrewMember.equals(flightCrewMember);

				super.state(context, uniqueFlightCrewMember, "employee-code", "acme.validation.customer.duplicated-identifier.message");
			}
			{
				boolean validEmployeeCode = false;
				String name = flightCrewMember.getIdentity().getName();
				String surName = flightCrewMember.getIdentity().getSurname();
				String employeeCode = flightCrewMember.getEmployeeCode();

				if (name.charAt(0) == employeeCode.charAt(0) && surName.charAt(0) == employeeCode.charAt(1))
					validEmployeeCode = true;
				super.state(context, validEmployeeCode, "employee-code", "acme.validation.flight-crew-member.invalid-identifier.message");

			}
		}

		result = !super.hasErrors(context);

		return result;
	}
}
