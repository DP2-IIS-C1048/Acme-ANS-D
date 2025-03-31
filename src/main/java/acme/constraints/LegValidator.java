
package acme.constraints;

import java.util.Date;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
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
				String airlineIataCode = leg.getAircraft() != null && leg.getAircraft().getAirline() != null ? leg.getAircraft().getAirline().getIataCode() : null;

				correctIATA = flightNumber != null && airlineIataCode != null && flightNumber.startsWith(airlineIataCode);
				super.state(context, correctIATA, "flightNumber", "acme.validation.constraints.leg.flightNumber-IATACode.message");
			}
			{

				boolean correctArrivalDepartureDates = departureDate != null && arrivalDate != null && departureDate.before(arrivalDate);
				super.state(context, correctArrivalDepartureDates, "scheduledDeparture", "acme.validation.constraints.leg.arrivalDepartureDates.message");
			}
			{
				boolean invalidAirports;
				if(leg.getArrivalAirport().getIataCode().equals(leg.getDepartureAirport().getIataCode())) {
					invalidAirports = false;
				} else {
					invalidAirports = true;
				}
				super.state(context, invalidAirports, "arrivalAirport", "acme.validation.constraints.leg.invalid-airports.message");
			}
			//			{
			//				boolean uniqueArrivalAndDepartureDate;
			//				List<Leg> existingLegs;
			//
			//				existingLegs = this.repository.findLegsByArrivalDepartureDate(departureDate, arrivalDate);
			//				uniqueArrivalAndDepartureDate = existingLegs.isEmpty() || existingLegs.size() == 1 && existingLegs.contains(leg);
			//
			//				super.state(context, uniqueArrivalAndDepartureDate, "*", "acme.validation.leg.duplicated-leg-arrivalDepartureDates.message");
			//			}
			//			{
			//				boolean validLegDates;
			//				Integer totalLegs;
			//
			//				totalLegs = leg.getFlight().getLayovers();
			//				if (totalLegs > 0) {
			//					if (leg.getScheduledArrival().before(leg.getFlight().getScheduledDeparture()))
			//						validLegDates = true;
			//					else if (leg.getScheduledDeparture().after(leg.getFlight().getScheduledArrival()))
			//						validLegDates = true;
			//					else
			//						validLegDates = false;
			//				} else
			//					validLegDates = true;
			//
			//				super.state(context, validLegDates, "*", "acme.validation.leg.invalid-leg-dates.message");
			//
			//			}
			//			{
			//				boolean validAirports;
			//				Integer totalLegs;
			//				Leg firstLeg;
			//				Leg lastLeg;
			//
			//				totalLegs = leg.getFlight().getLayovers();
			//				if (totalLegs > 0) {
			//					firstLeg = leg.getFlight().getFirstLeg();
			//					lastLeg = leg.getFlight().getLastLeg();
			//					if (leg.getArrivalAirport().getIataCode().equals(firstLeg.getDepartureAirport().getIataCode()))
			//						validAirports = true;
			//					else if (leg.getDepartureAirport().getIataCode().equals(lastLeg.getDepartureAirport().getIataCode()))
			//						validAirports = true;
			//					else
			//						validAirports = false;
			//				} else
			//					validAirports = true;
			//
			//				super.state(context, validAirports, "*", "acme.validation.leg.invalid-leg-airports.message");
			//
			//			}
		}
		result = !super.hasErrors(context);
		return result;

	}
}
