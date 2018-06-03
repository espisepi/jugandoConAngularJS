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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="laws" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="configurationSystem.edit" var="Edit" />
		
		<display:column title="${Edit}" sortable="true">
		
		
			<spring:url value="legislation/admin/edit.do" var="editURL">
				<spring:param name="lawId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="configurationSystem.edit" /></a>
		
		
		</display:column>
	</security:authorize>

	<acme:column code="configurationSystem.legislation.title" property="title"/>
	
	<acme:column code="configurationSystem.legislation.body" property="body"/>
	
	<spring:message code="configurationSystem.legislation.link" var="Edit" />
	
<display:column title="${Edit}" sortable="true">
	<jstl:if test="${!(row.link=='')}">
	<a href="${row.link}"><spring:message code = "configurationSystem.legislation.link"></spring:message></a>
</jstl:if>
</display:column>

</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="legislation/admin/create.do"> <spring:message
				code="configurationSystem.legislation.create" />
		</a>
	</div>
</security:authorize>