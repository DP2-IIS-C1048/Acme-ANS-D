
package acme.features.authenticated.flight_crew_member;

import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.flight_crew_member.AvailabilityStatus;
import acme.realms.flight_crew_member.FlightCrewMember;

@GuiService
public class AuthenticatedFlightCrewMemberCreateService extends AbstractGuiService<Authenticated, FlightCrewMember> {

	@Autowired
	private AuthenticatedFlightCrewMemberRepository repository;


	@Override
	public void authorise() {
		boolean status;
		status = !super.getRequest().getPrincipal().hasRealmOfType(FlightCrewMember.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrewMember flightCrewMember;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		flightCrewMember = new FlightCrewMember();
		flightCrewMember.setUserAccount(userAccount);

		super.getBuffer().addData(flightCrewMember);
	}

	@Override
	public void bind(final FlightCrewMember flightCrewMember) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);
		flightCrewMember.setAirline(airline);
		flightCrewMember.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

		super.bindObject(flightCrewMember, "phoneNumber", "languageSkills", "salary", "yearsOfExperience");
		flightCrewMember.setEmployeeCode(this.generateEmployeeCode(flightCrewMember));

	}

	private String generateEmployeeCode(final FlightCrewMember flightCrewMember) {
		String employeeCode;
		char firstLetter = flightCrewMember.getIdentity().getName().toUpperCase().charAt(0);
		char secondLetter = flightCrewMember.getIdentity().getSurname().toUpperCase().charAt(0);
		Random rnd = new Random();
		int numero = 100000 + rnd.nextInt(900000);
		employeeCode = "" + firstLetter + secondLetter + Integer.toString(numero);
		FlightCrewMember fcm = this.repository.findFlightCrewMemberByEmployeeCode(employeeCode);
		if (fcm != null)
			employeeCode = this.generateEmployeeCode(flightCrewMember);
		return employeeCode;

	}

	@Override
	public void validate(final FlightCrewMember flightCrewMember) {
		;
	}

	@Override
	public void perform(final FlightCrewMember flightCrewMember) {
		this.repository.save(flightCrewMember);
	}

	@Override
	public void unbind(final FlightCrewMember flightCrewMember) {
		Collection<Airline> airlines;
		SelectChoices airlineChoices;
		Dataset dataset;

		airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "name", null);

		dataset = super.unbindObject(flightCrewMember, "phoneNumber", "languageSkills", "salary", "yearsOfExperience");

		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
		dataset.put("availabilityStatus", AvailabilityStatus.AVAILABLE);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
