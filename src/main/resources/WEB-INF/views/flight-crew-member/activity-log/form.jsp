<%--
- form.jsp
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

<acme:form> 
	<acme:input-textbox code="flight-crew-member.activity-log.form.label.type-of-incident" path="typeOfIncident"/>	
	<acme:input-integer code="flight-crew-member.activity-log.form.label.severity-level" path="severityLevel"/>
	<acme:input-textarea code="flight-crew-member.activity-log.form.label.description" path="description"/>
	<jstl:if test="${_command != 'create' and draftMode == false}">
		<acme:input-moment code="flight-crew-member.activity-log.list.label.registration-moment" path="registrationMoment"/>
	</jstl:if>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="flight-crew-member.activity-log.form.button.update" action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.delete" action="/flight-crew-member/activity-log/delete"/>
			<acme:submit code="flight-crew-member.activity-log.form.button.publish" action="/flight-crew-member/activity-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.activity-log.form.button.create" action="/flight-crew-member/activity-log/create?masterId=${masterId}"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
