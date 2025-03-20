<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="authenticated.customer.list.label.locatorCode" path="locatorCode" width="40%"/>
	<acme:list-column code="authenticated.customer.list.label.travelClass" path="travelClass" width="30%"/>
	<acme:list-column code="authenticated.customer.list.label.price" path="price" width="30%"/>		
	<acme:list-payload path="payload"/>
</acme:list>