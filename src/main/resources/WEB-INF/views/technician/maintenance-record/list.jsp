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

	<acme:list-column code="technician.maintenance-record.list.label.aircraft" path="aircraft" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.maintenanceMoment" path="maintenanceMoment" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.status" path="status" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.inspectionDueDate" path="inspectionDueDate" width="10%"/>
	<acme:list-column code="technician.maintenance-record.list.label.estimatedCost" path="estimatedCost" width="10%"/>
	
	<jstl:if test="${_command == 'list'}">
		<acme:list-column code="technician.maintenance-record.list.label.draftMode" path="draftMode" width="10%"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'listAllPublished'}">
		<acme:list-column code="technician.maintenance-record.list.label.technician" path="technician" width="10%"/>
	</jstl:if>
	
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.maintenance-record.list.button.create" action="/technician/maintenance-record/create"/>
</jstl:if>