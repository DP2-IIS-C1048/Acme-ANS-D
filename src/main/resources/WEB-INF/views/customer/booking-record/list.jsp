<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking-record.list.label.fullName" path="fullName" width="40%"/>
	<acme:list-column code="customer.booking-record.list.label.passportNumber" path="passportNumber" width="30%"/>
	<acme:list-column code="customer.booking-record.list.label.dateOfBirth" path="dateOfBirth" width="30%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="customer.booking-record.list.button.create" action="/customer/booking-record/create?masterId=${masterId}"/>
</jstl:if>