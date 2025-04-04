
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.assistanceagent.AssistanceAgent;
import acme.realms.assistanceagent.AssistanceAgentRepository;

@Validator
public class AssistanceAgentValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Autowired
	private AssistanceAgentRepository repository;


	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent assistanceAgent, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (assistanceAgent == null || assistanceAgent.getEmployeeCode() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			{
				boolean uniqueAssitanceAgent;
				AssistanceAgent existingAssistanceAgent;

				existingAssistanceAgent = this.repository.findAssistanceAgentByEmployeeCode(assistanceAgent.getEmployeeCode());
				uniqueAssitanceAgent = existingAssistanceAgent == null || existingAssistanceAgent.equals(assistanceAgent);

				super.state(context, uniqueAssitanceAgent, "employeeCode", "acme.validation.assistanceagent.duplicated-employeeCode.message");
			}
			{
				boolean validEmployeeCode = false;
				String name = assistanceAgent.getIdentity().getName();
				String surName = assistanceAgent.getIdentity().getSurname();
				String employeeCode = assistanceAgent.getEmployeeCode();
				String[] surnames = surName.split(" ");
				if (surnames.length > 1) {
					if (name.charAt(0) == employeeCode.charAt(0) && surnames[0].charAt(0) == employeeCode.charAt(1) && surnames[1].charAt(0) == employeeCode.charAt(2))
						validEmployeeCode = true;
					super.state(context, validEmployeeCode, "employeeCode", "acme.validation.assistanceagent.invalid-employeeCode.message");
				} else {
					if (name.charAt(0) == employeeCode.charAt(0) && surnames[0].charAt(0) == employeeCode.charAt(1))
						validEmployeeCode = true;
					super.state(context, validEmployeeCode, "employeeCode", "acme.validation.assistanceagent.invalid-employeeCode.message");
				}
			}
		}

		result = !super.hasErrors(context);

		return result;

	}

}
