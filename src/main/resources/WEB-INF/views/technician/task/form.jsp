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

	<acme:input-select code="technician.task.form.label.type" path="type" choices="${types}"/>
	<acme:input-textarea code="technician.task.form.label.description" path="description"/>
	<acme:input-integer code="technician.task.form.label.priority" path="priority"/>
	<acme:input-integer code="technician.task.form.label.estimatedDuration" path="estimatedDuration"/>
	
	<jstl:if test="${_command != 'create'}">
		
		<acme:input-textbox code="technician.task.form.label.technician" path="technician" readonly="true"/>
	
	</jstl:if>
	
	<jstl:choose>
	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			
			<acme:submit code="technician.task.form.button.update" action="/technician/task/update"/>
			<acme:submit code="technician.task.form.button.delete" action="/technician/task/delete"/>
			<acme:submit code="technician.task.form.button.publish" action="/technician/task/publish"/>
		
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
		
			<acme:submit code="technician.task.form.button.create" action="/technician/task/create"/>
		
		</jstl:when>	
			
	</jstl:choose>
	
</acme:form>
