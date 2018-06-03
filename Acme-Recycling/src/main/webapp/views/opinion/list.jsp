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
	name="opinions" requestURI="${requestURI}" id="row">

	<!--  EDIT -->

	<jstl:if test="${showButtonEdit}">
		<security:authorize access="isAuthenticated()">
			<spring:message code="opinion.edit" var="Edit" />
			<display:column title="${Edit}" sortable="false">

				<spring:url value="opinion/actor/edit.do" var="editURL">
					<spring:param name="opinionId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="opinion.edit" /></a>
			</display:column>
		</security:authorize>
	</jstl:if>


	<!-- ATRIBUTOS -->

	<spring:message code="opinion.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="opinion.comment" var="commentHeader" />
	<display:column property="comment" title="${commentHeader}"
		sortable="true" />

	<spring:message code="opinion.format.createdMoment" var="pattern"></spring:message>
	<spring:message code="opinion.createdMoment" var="postedHeader" />
	<display:column property="createdMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="opinion.photo" var="photoVar" />
	<display:column title="${photoVar}" sortable="true">
		<jstl:if test="${row.photo!=''}">
		<div
			style="position: relative; width: 100px; height: 100px; margin-left: auto; margin-right: auto;">

			<img src="${row.photo}" width="100" height="100">
		</div>
		</jstl:if>
	</display:column>

	<spring:message code="opinion.caption" var="captionHeader" />
	<display:column property="caption" title="${captionHeader}"
		sortable="true" />

	<!-- MOSTRAR ITEM O COURSE DE ESA OPINION -->

	<spring:message code="opinion.displayOpinable" var="displayOpinable" />
	<display:column title="${displayOpinable}" sortable="false">

		<spring:url value="opinion/displayOpinable.do" var="displayURL">
			<spring:param name="opinionId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message
				code="opinion.displayOpinable" /></a>
	</display:column>






</display:table>


