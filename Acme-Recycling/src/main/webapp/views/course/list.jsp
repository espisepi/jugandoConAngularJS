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

<script type="text/javascript">
	function confirmDelete(courseId) {
		confirm=confirm('<spring:message code="course.confirm.delete"/>');
		if (confirm)
		  window.location.href ="course/admin/delete.do?courseId=" + courseId;
		  else
			  window.location.href ="course/admin/list.do?d-16544-p=1";
	}
</script>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="courses" requestURI="${requestURI}" id="row">

	<!-- Display -->
	
	<spring:message code="course.display" var="display" />
	<display:column title="${display}" sortable="true">
			<spring:url value="course/display.do" var="displayURL">
				<spring:param name="courseId" value="${row.id}" />
			</spring:url>
			<a href="${displayURL}"><spring:message code="course.display" /></a>
	</display:column>

	<!-- ATRIBUTOS -->

	<spring:message code="course.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="course.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}"
		sortable="true" />

	<spring:message code="course.format.realisedMoment" var="pattern"></spring:message>
	<spring:message code="course.realisedMoment" var="realisedHeader" />
	<display:column property="realisedMoment" title="${realisedHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="course.minimumScore" var="minimumScoreHeader" />
	<display:column property="minimumScore" title="${minimumScoreHeader}"
		sortable="true" />





	<!--  EDIT -->
	<security:authorize access="hasRole('RECYCLER')">
		<spring:message code="course.assist" var="assist" />
		<display:column title="${assist}" sortable="true">
			<jstl:if test="${available==true }">

				<spring:url value="course/recycler/assist.do" var="assistURL">
					<spring:param name="courseId" value="${row.id}" />
				</spring:url>
				<a href="${assistURL}"><spring:message code="course.assist" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('RECYCLER')">
		<spring:message code="course.notAssist" var="notAssist" />
		<display:column title="${notAssist}" sortable="true">
			<jstl:if test="${available==false }">

				<spring:url value="course/recycler/notAssist.do" var="notAssistURL">
					<spring:param name="courseId" value="${row.id}" />
				</spring:url>
				<a href="${notAssistURL}"><spring:message
						code="course.notAssist" /></a>
			</jstl:if>
		</display:column>
	</security:authorize>


	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="course.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="course.delete" />"
				onclick="confirmDelete(${row.id});" />
		</display:column>
	</security:authorize>

	<!-- Listar las opiniones de ese item -->
	<spring:message code="course.opinions" var="opinions" />
	<display:column title="${opinions}" sortable="true">
		<spring:url value="opinion/list.do" var="displayURL">
			<spring:param name="opinableId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message code="course.opinions" /></a>
	</display:column>







</display:table>
