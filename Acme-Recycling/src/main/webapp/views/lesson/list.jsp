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
	name="lessons" requestURI="${requestURI}" id="row">

	<!-- ATRIBUTOS -->

	<security:authorize access="hasRole('BUYER')">
	<spring:message code="lesson.edit" var="Edit" />
		<display:column title="${Edit}" sortable="false">
		<jstl:if test="${row.course.draftMode}">
			<spring:url value="lesson/buyer/edit.do" var="editURL">
				<spring:param name="lessonId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="lesson.edit1" /></a>
		</jstl:if>
	</display:column>
	</security:authorize>
	
	<spring:message code="lesson.number" var="numberHeader" />
	<display:column property="number" title="${numberHeader}" sortable="true"/>
	
	<spring:message code="lesson.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="lesson.summary" var="summaryHeader" />
	<display:column property="summary" title="${summaryHeader}" sortable="true"/>
	
		
 </display:table>





