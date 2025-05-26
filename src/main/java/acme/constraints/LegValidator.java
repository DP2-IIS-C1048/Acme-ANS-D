
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.MomentHelper;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Autowired
	private LegRepository repository;


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
			Date arrivalDate = leg.getScheduledArrival();
			Date departureDate = leg.getScheduledDeparture();

			{
				boolean uniqueFlightNumber;
				Leg existingLeg;

				existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				uniqueFlightNumber = existingLeg == null || leg.equals(existingLeg);

				super.state(context, uniqueFlightNumber, "flightNumber", "acme.validation.leg.duplicated-flightNumber.message");
			}
			{
				boolean correctIATA;

				String flightNumber = leg.getFlightNumber();
				String airlineIataCode = leg.getFlight().getManager().getAirline().getIataCode();

				correctIATA = !flightNumber.isBlank() && flightNumber.startsWith(airlineIataCode);
				super.state(context, correctIATA, "flightNumber", "acme.validation.constraints.leg.flightNumber-IATACode.message");
			}
			{
				boolean correctArrivalDepartureDates = true;
				if (departureDate != null && arrivalDate != null)
					correctArrivalDepartureDates = MomentHelper.isBefore(departureDate, arrivalDate);
				super.state(context, correctArrivalDepartureDates, "scheduledDeparture", "acme.validation.constraints.leg.arrivalDepartureDates.message");
				super.state(context, correctArrivalDepartureDates, "scheduledArrival", "acme.validation.constraints.leg.arrivalDepartureDates.message");
			}
			{
				boolean invalidAirports = true;

				if (leg.getArrivalAirport() != null && leg.getDepartureAirport() != null && leg.getArrivalAirport().getIataCode().equals(leg.getDepartureAirport().getIataCode()))
					invalidAirports = false;
				super.state(context, invalidAirports, "arrivalAirport", "acme.validation.constraints.leg.invalid-airports.message");
			}
			{
				Date minDate = MomentHelper.parse("2000-01-01 00:00", "yyyy-MM-dd HH:mm");
				Date maxDate = MomentHelper.parse("2200-12-31 23:59", "yyyy-MM-dd HH:mm");

				if (departureDate != null && !MomentHelper.isAfterOrEqual(departureDate, minDate))
					super.state(context, false, "scheduledDeparture", "acme.validation.constraints.leg.invalid-min-date.message");

				if (arrivalDate != null && !MomentHelper.isBeforeOrEqual(arrivalDate, maxDate))
					super.state(context, false, "scheduledArrival", "acme.validation.constraints.leg.invalid-max-date.message");

				if (arrivalDate != null && !MomentHelper.isAfterOrEqual(arrivalDate, minDate))
					super.state(context, false, "scheduledArrival", "acme.validation.constraints.leg.invalid-min-date.message");

				if (departureDate != null && !MomentHelper.isBeforeOrEqual(departureDate, maxDate))
					super.state(context, false, "scheduledDeparture", "acme.validation.constraints.leg.invalid-max-date.message");
			}
		}
		result = !super.hasErrors(context);
		return result;

	}
}
