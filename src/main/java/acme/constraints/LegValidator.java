
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;
		boolean result;
		if (leg == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				//				boolean uniqueFlightNumber;
				//				Leg existingLeg;
				//
				//				existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				//				uniqueFlightNumber = existingLeg == null || leg.equals(existingLeg);
				//
				//				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.job.duplicated-flightNumber.message");
			}
			{
				//				boolean correctIATA;
				//
				//				String flightNumber = leg.getFlightNumber();
				//				String airlineIataCode = leg.getFlight() != null && 
				//											leg.getFlight().getAircraft() != null && 
				//											leg.getFlight().getAircraft().getAirline() 
				//												? leg.getFlight().getAircraft().getAirline().getIataCode() : null;
				//
				//				correctIATA = flightNumber != null && airlineIataCode != null && flightNumber.startsWith(airlineIataCode);
				//				super.state(context, correctIATA, "*", "javax.validation.constraints.IATACode.message");
			}
			{
				Date arrivalDate = leg.getScheduledArrival();
				Date departureDate = leg.getScheduledDeparture();

				boolean correctArrivalDepartureDates = departureDate.before(arrivalDate);
				super.state(context, correctArrivalDepartureDates, "*", "javax.validation.constraints.arrivalDepartureDates.message");
			}
		}
		result = !super.hasErrors(context);
		return result;

	}
}
