<%--
- menu.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.student1" action="https://wordly.org/es"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.student2" action="https://mega.io/es/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.student3" action="https://refactoring.guru/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.student4" action="https://palosfrontera.com/"/>
			<acme:menu-suboption code="master.menu.anonymous.favourite-link.student5" action="https://ev.us.es/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.authenticated" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.authenticated.list-flight" action="/authenticated/flight/list" />
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-suboption code="master.menu.administrator.list-aircrafts" action="/administrator/aircraft/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.administrator.airline" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-airlines" action="/administrator/airline/list"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator.airport" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-airports" action="/administrator/airport/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.manager" access="hasRealm('Manager')">
			<acme:menu-suboption code="master.menu.manager.list-my-flights" action="/manager/flight/list"/>
			<acme:menu-suboption code="master.menu.manager.show-dashboard" action="/manager/manager-dashboard/show" />
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.technician" access="hasRealm('Technician')">
			<acme:menu-suboption code="master.menu.technician.list-my-maintenance-records" action="/technician/maintenance-record/list"/>
			<acme:menu-suboption code="master.menu.technician.list-my-tasks" action="/technician/task/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.technician.list-all-maintenance-records-published" action="/technician/maintenance-record/listAllPublished"/>
			<acme:menu-suboption code="master.menu.technician.list-all-tasks-published" action="/technician/task/listAllPublished"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.flight-crew-member" access="hasRealm('FlightCrewMember')">
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list-completed" action="/flight-crew-member/flight-assignment/list-completed"/>
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.list-uncompleted" action="/flight-crew-member/flight-assignment/list-uncompleted"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.flight-crew-member.flight-assignment.show-dashboard" action="/flight-crew-member/flight-crew-member-dashboard/show"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
 			<acme:menu-suboption code="master.menu.customer.list-my-passengers" action="/customer/passenger/list"/>
			<acme:menu-suboption code="master.menu.customer.list-my-bookings" action="/customer/booking/list"/>
 		</acme:menu-option>
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-manager" action="/authenticated/manager/create" access="!hasRealm('Manager')"/>
			<acme:menu-suboption code="master.menu.user-account.manager-profile" action="/authenticated/manager/update" access="hasRealm('Manager')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

