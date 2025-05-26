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
	<acme:input-textbox code="authenticated.flight-assignment.form.label.flight-number" path="leg.legLabel"/>
	<acme:input-textbox code="authenticated.flight-assignment.form.label.member" path="flightCrewMember.identity.fullName"/>
	<acme:input-textbox code="authenticated.flight-assignment.form.label.duty" path="duty"/>
	<acme:input-textbox code="authenticated.flight-assignment.form.label.last-update" path="lastUpdate"/>
	<acme:input-textbox code="authenticated.flight-assignment.form.label.current-status" path="currentStatus"/>
	<acme:input-textarea code="authenticated.flight-assignment.form.label.remarks" path="remarks"/>
</acme:form>
