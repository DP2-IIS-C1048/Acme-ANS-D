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

	<acme:input-moment code="technician.maintenanceRecord.list.label.maintenanceMoment" path="maintenanceMoment"/>
	<acme:menu-option code="technician.maintenanceRecord.list.label.status" path="status"/>
	<acme:input-moment code="technician.maintenanceRecord.list.label.inspectionDueDate" path="inspectionDueDate"/>
	<acme:input-money code="technician.maintenanceRecord.list.label.estimatedCost" path="estimatedCost"/>
	<acme:input-textarea code="technician.maintenanceRecord.form.label.notes" path="notes"/>
	
	<jstl:if test="${_command != 'create'}">
	
		<acme:input-moment code="technician.maintenanceRecord.list.label.maintenanceMoment" path="maintenanceMoment" readonly="true"/>
		<acme:menu-option code="technician.maintenanceRecord.list.label.status" path="status" readonly="true"/>
		<acme:input-moment code="technician.maintenanceRecord.list.label.inspectionDueDate" path="inspectionDueDate" readonly="true"/>
		<acme:input-money code="technician.maintenanceRecord.list.label.estimatedCost" path="estimatedCost" readonly="true"/>
		<acme:input-textarea code="technician.maintenanceRecord.form.label.notes" path="notes" readonly="true"/>
	
	</jstl:if>
	<jstl:choose>
	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
		
			<acme:button code="technician.maintenanceRecord.form.button.tasks" action="/technician/task/list?masterId=${id}"/>			
		
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			
			<acme:button code="technician.maintenanceRecord.form.button.tasks" action="/technician/task/list?masterId=${id}"/>
			<acme:submit code="technician.maintenanceRecord.form.button.update" action="/technician/maintenanceRecord/update"/>
			<acme:submit code="technician.maintenanceRecord.form.button.delete" action="/technician/maintenanceRecord/delete"/>
			<acme:submit code="technician.maintenanceRecord.form.button.publish" action="/technician/maintenanceRecord/publish"/>
		
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
		
			<acme:submit code="technician.maintenanceRecord.form.button.create" action="/technician/maintenanceRecord/create"/>
		
		</jstl:when>		
	</jstl:choose>
</acme:form>
