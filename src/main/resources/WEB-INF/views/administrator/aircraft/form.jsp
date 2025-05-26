<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 

	<acme:input-textbox code="administrator.aircraft.form.label.model" path="model"/>
	<acme:input-textbox code="administrator.aircraft.form.label.registrationNumber" path="registrationNumber"/>
	<acme:input-integer code="administrator.aircraft.form.label.capacity" path="capacity"/>
	<acme:input-integer code="administrator.aircraft.form.label.cargoWeight" path="cargoWeight"/>
	<acme:input-select code="administrator.aircraft.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-textarea code="administrator.aircraft.form.label.details" path="details"/>
	<acme:input-select code="administrator.aircraft.form.label.airline" path="airline" choices="${airlines}"/>

	<acme:input-checkbox code="administrator.aircraft.form.label.confirmation" path="confirmation"/>
	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && status != 'MAINTENANCE'}">
			<acme:submit code="administrator.aircraft.form.button.disable" action="/administrator/aircraft/disable"/>
			<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
		</jstl:when>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|disable')}">
			<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>