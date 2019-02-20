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
	name="tabooWords" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="configurationSystem.edit" var="Edit" />
		
		<display:column title="${Edit}" sortable="true">
		
		<jstl:if test="${row.default_word==false}">
			<spring:url value="tabooWord/admin/edit.do" var="editURL">
				<spring:param name="tabooWordId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="configurationSystem.edit" /></a>
		
		</jstl:if>
		</display:column>
	</security:authorize>

	<acme:column code="configurationSystem.tabooWord" property="name"/>


</display:table>


<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="tabooWord/admin/create.do"> <spring:message
				code="configurationSystem.create" />
		</a>
	</div>
</security:authorize>