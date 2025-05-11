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
	<acme:list-column code="flight-crew-member.activity-log.list.label.type-of-incident" path="typeOfIncident" width="40%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.severity-level" path="severityLevel" width="30%"/>
	<acme:list-column code="flight-crew-member.activity-log.list.label.registration-moment" path="registrationMoment" width="10%"/>	

</acme:list>
	
<jstl:if test="${_command == 'list'}">
	<acme:button code="flight-crew-member.activity-log.list.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
</jstl:if>	
	

