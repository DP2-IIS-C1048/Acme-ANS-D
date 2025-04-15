<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.booking-record.list.label.passenger" path="passenger" width="50%"/>
</acme:list>

<jstl:if test="${showCreate}"> <%-- El botón se muestr aunque el booking esté ya publicado, aun así no t edeja acceder en ese caso --%>
	<acme:button code="customer.booking-record.list.button.create" action="/customer/booking-record/create?masterId=${masterId}"/>
</jstl:if>