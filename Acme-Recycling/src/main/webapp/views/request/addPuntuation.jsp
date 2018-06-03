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

<form:form action="request/manager/edit.do" modelAttribute="item">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<spring:message code="item.puntuation" var="puntuation"/>
	<jstl:out value="${puntuation}"></jstl:out>
	<form:input path="value" type="number" value = "${item.value}" max="500" min ="0"/>

	<!-- BOTONES -->

	<input type="submit" name="saveAddPuntuation"
		value="<spring:message code="request.save"/>" />&nbsp;
		
		<acme:cancel url="request/manager/listMyRequest.do?d-16544-p=1"
		code="request.cancel" />

</form:form>

