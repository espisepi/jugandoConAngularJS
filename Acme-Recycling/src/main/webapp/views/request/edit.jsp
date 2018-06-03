<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="request/manager/edit.do" modelAttribute="requestForm">

	<form:hidden path="request.id" />
	<form:hidden path="request.version" />
	<%-- 	<form:hidden path="code" />
	<form:hidden path="status" /> --%>
	<form:hidden path="itemId" />

	<acme:textbox code="request.code" path="request.code" readonly="true" />
	<br />
	<acme:textbox code="request.title" path="request.title" />
	<br />
	<acme:textbox code="request.observation" path="request.observation" />
	<br />
	<acme:select items="${carriers}" itemLabel="name"
		code="request.carrier" path="request.carrier" />
	<br />
	<acme:selectmultiple items="${cleanPoints}" itemLabel="address"
		code="request.cleanPoint" path="request.cleanPoints" />
	<br />

	<!-- BOTONES -->

	<input type="submit" name="save"
		value="<spring:message code="request.save"/>" />&nbsp;
		
		<acme:cancel url="request/manager/listItemsNonRequest.do?d-16544-p=1"
		code="request.cancel" />
</form:form>

