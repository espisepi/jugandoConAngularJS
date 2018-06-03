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
	name="requests" requestURI="${requestURI}" id="row">

	<!--  CHANGE STATUS -->

	<security:authorize access="hasRole('MANAGER')">

		<spring:message code="request.changeStatus" var="changeStatus" />
		<display:column title="${changeStatus}" sortable="false">
			<jstl:if
				test="${showButtonChangeStatus and (row.status!='FINISHED' and row.status!='CANCELLED')}">
				<spring:url value="request/manager/changeStatus.do" var="editURL">
					<spring:param name="requestId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="request.changeStatus" /></a>
			</jstl:if>
		</display:column>

	</security:authorize>

	<security:authorize access="hasRole('CARRIER')">

		<spring:message code="request.changeStatus" var="changeStatus" />
		<display:column title="${changeStatus}" sortable="false">
			<jstl:if
				test="${showButtonChangeStatus and (row.status!='FINISHED' and row.status!='CANCELLED')}">
				<spring:url value="request/carrier/changeStatus.do" var="editURL">
					<spring:param name="requestId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="request.changeStatus" /></a>
			</jstl:if>
		</display:column>


		<spring:message code="request.addAssessment" var="addAssessment" />
		<display:column title="${addAssessment}" sortable="false">
			<jstl:if
				test="${showButtonAddAssessment and row.assesment==null and (row.status=='FINISHED' or row.status=='CANCELLED')}">
				<spring:url value="assessment/carrier/create.do" var="editURL">
					<spring:param name="requestId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message
						code="request.addAssessment" /></a>
			</jstl:if>
		</display:column>

	</security:authorize>

	<!-- MANAGER ADD PUNTUATION  -->

	<security:authorize access="hasRole('MANAGER')">
		<spring:message code="request.addPuntuation" var="addPuntuation" />
		<display:column title="${addPuntuation}" sortable="false">
			<jstl:if test="${row.status=='FINISHED'}">
				<spring:url value="request/manager/addPuntuation.do" var="editURL">
					<spring:param name="requestId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message
						code="request.addPuntuation" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>

	<!-- ATRIBUTOS -->

	<spring:message code="request.code" var="codeHeader" />
	<display:column property="code" title="${codeHeader}" sortable="true" />

	<spring:message code="request.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="request.observation" var="observationHeader" />
	<display:column property="observation" title="${observationHeader}"
		sortable="true" />

	<spring:message code="request.status" var="statusHeader" />
	<display:column property="status" title="${statusHeader}"
		sortable="true" />



</display:table>


