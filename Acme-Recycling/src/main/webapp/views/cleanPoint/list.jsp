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

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="cleanPoints" requestURI="${requestURI}" id="row">
	

 <!--  EDIT -->
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="cleanPoint.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			<jstl:if test="${row.mobile==true}">
				<spring:url value="cleanPoint/admin/edit.do" var="editURL">
					<spring:param name="cleanPointId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="cleanPoint.edit" /></a>
				</jstl:if>
		</display:column>
	</security:authorize> 
	
	<!-- DISplay -->
	
		<security:authorize access="hasRole('ADMIN')">
		<spring:message code="cleanPoint.display" var="Edit" />
		<display:column title="${Edit}" sortable="true">
				<spring:url value="cleanPoint/admin/display.do" var="editURL">
					<spring:param name="cleanPointId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="cleanPoint.display" /></a>
		</display:column>
	</security:authorize> 
	<!-- ATRIBUTOS -->

	<spring:message code="cleanPoint.address" var="titleHeader" />
	<display:column property="address" title="${titleHeader}" sortable="true" />
	
	<spring:message code="cleanPoint.province" var="titleHeader" />
	<display:column property="province" title="${titleHeader}" sortable="true" />
	
	<spring:message code="cleanPoint.phone" var="titleHeader" />
	<display:column property="phone" title="${titleHeader}" sortable="true" />
	
</display:table>
<security:authorize access="hasRole('ADMIN')">
	<div>
		<a href="cleanPoint/admin/create.do"> <spring:message
				code="cleanPoint.create" />
		</a>
	</div>
</security:authorize>