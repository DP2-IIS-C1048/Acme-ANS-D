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

	<acme:input-textbox code="assistanceAgent.claim.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-textbox code="assistanceAgent.claim.form.label.passengerEmail" path="passengerEmail"/>
	<acme:input-textbox code="assistanceAgent.claim.form.label.type" path="type"/>
	<acme:input-textarea code="assistanceAgent.claim.form.label.description" path="description"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-textbox code="assistanceAgent.claim.form.label.trackingLogType" path="trackingLogType" readonly="true"/>
		<acme:input-checkbox code="assistanceAgent.claim.form.label.draftMode" path="draftMode" readonly="true"/>
	</jstl:if>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="assistanceAgent.claim.form.button.trackingLogs" action="/assistanceagent/trackinglog/list?masterId=${id}"/>			
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:button code="assistanceAgent.claim.form.button.trackingLogs" action="/assistanceagent/trackinglog/list?masterId=${id}"/>
			<acme:submit code="assistanceAgent.claim.form.button.update" action="/assistanceagent/trackinglog/update"/>
			<acme:submit code="assistanceAgent.claim.form.button.delete" action="/assistanceagent/trackinglog/delete"/>
			<acme:submit code="assistanceAgent.claim.form.button.publish" action="/assistanceagent/trackinglog/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.claim.form.button.create" action="/assistanceagent/trackinglog/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
