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

	<jstl:choose>
	
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="technician.involves.form.label.tasksInvolved" path="task" choices="${tasks}"/>
			<acme:submit code="technician.involves.form.button.create" action="/technician/involves/create?maintenanceRecordId=${maintenanceRecordId}"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|delete')}">
			<acme:input-textbox code="technician.involves.form.label.type" path="type" readonly="true"/>
			<acme:input-textarea code="technician.involves.form.label.description" path="description" readonly="true"/>
			<acme:input-integer code="technician.involves.form.label.priority" path="priority" readonly="true"/>
			<acme:input-integer code="technician.involves.form.label.estimatedDuration" path="estimatedDuration" readonly="true"/>
			
			<acme:submit code="technician.involves.form.button.delete" action="/technician/involves/delete"/>
		</jstl:when>
	
		
	
	</jstl:choose>
	
</acme:form>
