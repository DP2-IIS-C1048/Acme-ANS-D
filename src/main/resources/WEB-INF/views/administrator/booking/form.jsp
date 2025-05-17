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
		<acme:input-textbox code="administrator.booking.form.label.locatorCode" path="locatorCode"/>
		<acme:input-textbox code="administrator.booking.form.label.travelClass" path="travelClass" placeholder="administrator.booking.form.placeholder.travelClass"/>
		<acme:input-money code="administrator.booking.form.label.price" path="price"/>
		<acme:input-textbox code="administrator.booking.form.label.lastNibble" path="lastNibble" placeholder="administrator.booking.form.placeholder.lastNibble"/>
		<acme:input-select code="administrator.booking.form.label.flight" path="flight" choices="${flights}"/>
		<acme:input-textbox code="administrator.booking.form.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
	<jstl:choose>	 
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:button code="administrator.booking.form.button.passengers" action="/administrator/booking-record/list?masterId=${id}"/>	
		</jstl:when>	
	</jstl:choose>
</acme:form>