<%--
- list.jsp
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
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.flight-number" path="leg.legLabel" width="60%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.duty" path="duty" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.current-status" path="currentStatus" width="10%"/>
	<acme:list-column code="flight-crew-member.flight-assignment.list.label.last-update" path="lastUpdate" width="15%"/>
</acme:list>

<jstl:if test="${_command == 'list-completed' or _command == 'list-uncompleted'}">
	<acme:button code="flight-crew-member.flight-assignment.list.button.create" action="/flight-crew-member/flight-assignment/create"/>
</jstl:if>		
	

