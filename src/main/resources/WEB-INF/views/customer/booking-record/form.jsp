<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>	

	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.booking-record.form.label.passenger" path="passenger" choices="${passengers}"/>
			<acme:submit code="customer.booking-record.form.button.create" action="/customer/booking-record/create?masterId=${masterId}"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'show'}">
			<acme:input-textbox code="customer.booking-record.form.label.fullName" path="fullName"/>
			<acme:input-textbox code="customer.booking-record.form.label.email" path="email"/>
			<acme:input-textbox code="customer.booking-record.form.label.passportNumber" path="passportNumber"/>
			<acme:input-textbox code="customer.booking-record.form.label.dateOfBirth" path="dateOfBirth"/>
			<acme:input-textarea code="customer.booking-record.form.label.specialNeeds" path="specialNeeds"/>
		</jstl:when>
	</jstl:choose>
</acme:form>