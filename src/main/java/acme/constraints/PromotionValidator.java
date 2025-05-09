
package acme.constraints;

import java.time.LocalDate;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.service.Promotion;
import acme.entities.service.PromotionRepository;

@Validator
public class PromotionValidator extends AbstractValidator<ValidPromotion, Promotion> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private PromotionRepository promotionRepository;

	// ConstraintValidator interface ------------------------------------------


	@Override
	protected void initialise(final ValidPromotion annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Promotion promotion, final ConstraintValidatorContext context) {
		String promotionCode = promotion.getCode();

		boolean result;
		if (promotionCode == null || promotion.getMoneyToDiscount() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				List<String> allPromotionCodes;
				boolean promotionCodeIsUnique;

				allPromotionCodes = this.promotionRepository.findAllPromotionCodes();
				promotionCodeIsUnique = allPromotionCodes.contains(promotionCode);

				super.state(context, promotionCodeIsUnique, "code", "acme.validation.promotion.duplicated-promotion-code.message");
			}
			{
				String pattern;
				boolean promotionCodeMeetsPattern;

				pattern = "^[A-Z]{4}-[0-9]{2}$";
				promotionCodeMeetsPattern = promotionCode.matches(pattern);

				super.state(context, promotionCodeMeetsPattern, "code", "acme.validation.promotion.promotion-code-pattern-mismatch.message");
			}
			{
				String actualYearLastTwoDigits;
				boolean promotionCodeLastDigitsMeetActualYear;
				LocalDate actualDate;

				actualDate = LocalDate.now();
				actualYearLastTwoDigits = String.valueOf(actualDate.getYear()).substring(2);
				promotionCodeLastDigitsMeetActualYear = promotionCode.substring(promotionCode.length() - 2).equals(actualYearLastTwoDigits);

				super.state(context, promotionCodeLastDigitsMeetActualYear, "code", "acme.validation.promotion.promotion-code-last-digits.message");
			}
		}

		result = !super.hasErrors(context);

		return result;
	}

}
