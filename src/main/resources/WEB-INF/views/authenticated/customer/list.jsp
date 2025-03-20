<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.customer.list.label.fullName" path="fullName" width="40%"/>
	<acme:list-column code="authenticated.customer.list.label.email" path="email" width="30%"/>
	<acme:list-column code="authenticated.customer.list.label.passportNumber" path="passportNumber" width="30%"/>		
	<acme:list-payload path="payload"/>
</acme:list>