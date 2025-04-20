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
	<acme:input-textbox code="administrator.airline.form.label.name" path="name"/>	
	<acme:input-textbox code="administrator.airline.form.label.iata-code" path="iataCode"/>	
	<acme:input-textbox code="administrator.airline.form.label.website" path="website"/>	
	<acme:input-textbox code="administrator.airline.form.label.email-address" path="emailAddress"/>	
	<acme:input-textbox code="administrator.airline.form.label.phone-number" path="phoneNumber"/>
	<acme:input-select code="administrator.airline.form.label.type" path="type" choices="${type}"/>
	<acme:input-moment code="administrator.airline.form.label.foundation-moment" path="foundationMoment"/>
	<acme:input-checkbox code="administrator.airline.form.label.confirmation" path="confirmation"/>	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update')}">
			<acme:submit code="administrator.airline.form.button.update" action="/administrator/airline/update"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="administrator.airline.form.button.create" action="/administrator/airline/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>
	