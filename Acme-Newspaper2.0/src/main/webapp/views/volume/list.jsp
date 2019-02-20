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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="volumes" requestURI="${requestURI}" id="row">
	
	<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="volume.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
		<jstl:if test="${!volumeSubscribed.contains(row)}">
		
		<jstl:if test="${!row.isAllPublic(row)}">
		
 			<spring:url value="underwrite/customer/create.do" var="editURL">
					<spring:param name="volumeId" value="${row.id}" />
					<spring:param name="d-16544-p" value="1" />
				</spring:url>
				<a href="${editURL}"><spring:message code="volume.underwrite" /></a>
		</jstl:if> 
		</jstl:if> 
		
		</display:column>
	</security:authorize>
		
	<spring:message code="volume.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="volume.description" var="titleHeader" />
	<display:column property="description" title="${titleHeader}" sortable="true" />
	
	<spring:message code="volume.year" var="titleHeader" />
	<display:column property="year" title="${titleHeader}" sortable="true" />
			
		<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="volume.display" var="Edit" />
		<display:column title="${Edit}" sortable="true">
<%-- 			<jstl:if test="${row.publicationDate==null}">
 --%>				<spring:url value="volume/customer/display.do" var="editURL">
					<spring:param name="volumeId" value="${row.id}" />
					<spring:param name="d-16544-p" value="1" />
				</spring:url>
				<a href="${editURL}"><spring:message code="volume.display" /></a>
	<%-- 		</jstl:if> --%>
		</display:column>
	</security:authorize>
	
	<security:authorize access="isAnonymous()">
		<spring:message code="volume.display.newspaper" var="Edit" />
		<display:column title="${Edit}" sortable="true">

 			<spring:url value="volume/display.do" var="editURL">
					<spring:param name="volumeId" value="${row.id}" />
					<spring:param name="d-16544-p" value="1" />
				</spring:url>
				<a href="${editURL}"><spring:message code="volume.display" /></a>
	
		</display:column>
	</security:authorize>
</display:table>

