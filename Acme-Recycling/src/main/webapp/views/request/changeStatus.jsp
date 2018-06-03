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

<form:form action="${requestURI}" modelAttribute="requestForm">

	<form:hidden path="request.id" />
	<form:hidden path="request.version" />

	<acme:changeStatus code="request.status" path="request.status"/>
	

	<!-- BOTONES -->
<br>
	<input type="submit" name="saveChangeStatus"
		value="<spring:message code="request.save"/>" />&nbsp;
		
		<security:authorize access="hasRole('MANAGER')">
		<acme:cancel url="request/manager/listMyRequest.do?d-16544-p=1"
		code="request.cancel" />
		</security:authorize>
		
		<security:authorize access="hasRole('CARRIER')">
		<acme:cancel url="request/carrier/listMyRequest.do?d-16544-p=1"
		code="request.cancel" />
		</security:authorize>


</form:form>

