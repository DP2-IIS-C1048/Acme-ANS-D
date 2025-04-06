<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.bookingRecord.list.label.booking" path="booking" width="50%"/>	
	<acme:list-column code="customer.bookingRecord.list.label.passenger" path="passenger" width="50%"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="customer.bookingRecord.list.button.create" action="/customer/bookingRecord/create?masterId=${masterId}"/>
</jstl:if>