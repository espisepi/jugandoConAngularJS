<%--
 * list.jsp
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


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="assessments" requestURI="${requestURI}" id="row">

	<!--  EDIT -->
	<security:authorize access="hasRole('CARRIER')">
		<spring:message code="assessment.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">

			<spring:url value="assessment/carrier/edit.do" var="editURL">
				<spring:param name="assessmentId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="assessment.edit" /></a>
		</display:column>
	</security:authorize>

	<!-- ATRIBUTOS -->

	<spring:message code="assessment.format.moment" var="pattern"></spring:message>
	<spring:message code="assessment.moment" var="momentHeader" />
	<display:column property="moment" title="${momentHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="assessment.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />

	<spring:message code="assessment.valuation" var="valuationHeader" />
	<display:column property="valuation" title="${valuationHeader}"
		sortable="true" />








</display:table>


