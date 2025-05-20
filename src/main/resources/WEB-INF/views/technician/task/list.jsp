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

	<acme:list-column code="technician.task.list.label.type" path="type" width="10%"/>
	<acme:list-column code="technician.task.list.label.priority" path="priority" width="10%"/>
	<acme:list-column code="technician.task.list.label.estimatedDuration" path="estimatedDuration" width="10%"/>
	
	<jstl:if test="${_command == 'list'}">
		<acme:list-column code="technician.task.list.label.draftMode" path="draftMode" width="10%"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'listAllPublished'}">
		<acme:list-column code="technician.task.list.label.technician" path="technician" width="10%"/>
	</jstl:if>
	
	<acme:list-column code="technician.task.list.label.description" path="description" width="10%"/>
		
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="technician.task.list.button.create" action="/technician/task/create"/>
</jstl:if>