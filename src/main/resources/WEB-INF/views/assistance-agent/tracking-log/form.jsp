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
	<jstl:if test="${_command != 'create'}">
		<acme:input-moment code="assistanceAgent.tracking-log.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	</jstl:if>
	<acme:input-textbox code="assistanceAgent.tracking-log.form.label.step" path="step"/>
	<acme:input-textbox code="assistanceAgent.tracking-log.form.label.resolutionPercentage" path="resolutionPercentage"/>
	<acme:input-textbox code="assistanceAgent.tracking-log.form.label.resolution" path="resolution"/>
	<acme:input-select code="assistanceAgent.tracking-log.form.label.indicator" path="indicator" choices="${indicator}"/>
	<jstl:if test="${_command != 'create'}">
		<acme:input-checkbox code="assistanceAgent.tracking-log.form.label.draftMode" path="draftMode" readonly="true"/>
	</jstl:if>
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="assistanceAgent.tracking-log.form.button.update" action="/assistance-agent/tracking-log/update"/>
			<acme:submit code="assistanceAgent.tracking-log.form.button.delete" action="/assistance-agent/tracking-log/delete"/>
			<acme:submit code="assistanceAgent.tracking-log.form.button.publish" action="/assistance-agent/tracking-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistanceAgent.tracking-log.form.button.create" action="/assistance-agent/tracking-log/create?masterId=${masterId}"/>
		</jstl:when>	
	</jstl:choose>
</acme:form>
