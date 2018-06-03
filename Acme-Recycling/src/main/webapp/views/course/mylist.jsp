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
<%-- <jsp:useBean id="util" class="utilities.Methodutilities" scope="page" /> --%>

<script type="text/javascript">
	function confirmDelete(rendezvousId) {
		confirm=confirm('<spring:message code="course.confirm.delete"/>');
		if (confirm)
		  window.location.href ="course/buyer/delete.do?courseId=" + courseId;
		  else
			  window.location.href ="course/buyer/list.do";
	}
</script>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="courses" requestURI="${requestURI }" id="row">

<%-- <%!String estilo;%>
	<jstl:choose>
			<jstl:when test="${util.organisedMoment(row.organisedMoment)==false}">
				<%=estilo = "p2"%>

			</jstl:when>
			
			 <jstl:when test="${util.organisedMoment(row.organisedMoment)==true}">
				<%=estilo = "yellow"%>

			</jstl:when>
 
		</jstl:choose> --%>
		
		
	<!-- ENLACE EDITAR Y DISPLAY-->
<%-- 	<security:authorize access="hasRole('BUYER')">
		<spring:message code="buyer.edit" var="Edit" />

		<display:column title="${Edit}" sortable="true">
			<jstl:if test="${row.draftMode==true}">
				<spring:url value="rendezvous/user/edit.do" var="editURL">
					<spring:param name="rendezvouseId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="rendezvous.edit" /></a>
			</jstl:if>
		</display:column>
	

	<spring:message code="rendezvous.display" var="Display" />
	<display:column title="${Display}" sortable="true" >
		<spring:url value="rendezvous/user/display.do" var="editURL">
			<spring:param name="rendezvousId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="rendezvous.display" /></a>

	</display:column>
	</security:authorize> --%>
	
<%-- 		
	<security:authorize access="isAnonymous()">
	<spring:message code="rendezvous.display" var="Display" />
	<display:column title="${Display}" sortable="true" >
		<spring:url value="rendezvous/display.do" var="editURL">
			<spring:param name="rendezvousId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="rendezvous.display" /></a>

	</display:column>
	</security:authorize> --%>



	<spring:message code="course.display" var="Display" />
		<display:column title="${Display}" sortable="false">
			<spring:url value="course/buyer/display.do" var="displayURL">
				<spring:param name="courseId" value="${row.id}" />
			</spring:url>
			<a href="${displayURL}"><spring:message code="course.display" /></a>
		</display:column>
	
	
	<spring:message code="course.edit" var="Edit" />
		<display:column title="${Edit}" sortable="false">
		<jstl:if test="${row.draftMode}">
			<spring:url value="course/buyer/edit.do" var="editURL">
				<spring:param name="courseId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="course.edit" /></a>
		</jstl:if>
	</display:column>
	
		
	<!-- ATRIBUTOS -->
	
	<spring:message code="course.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="course.description" var="titleHeader" />
	<display:column property="description" title="${titleHeader}" sortable="true" />
		
	<spring:message code="course.format.realisedMoment" var="pattern"></spring:message>
	<spring:message code="course.realisedMoment" var="postedHeader" />
	<display:column property="realisedMoment" title="${postedHeader}" sortable="true" format="${pattern}" />

	<spring:message code="course.minimumScore" var="titleHeader" />
	<display:column property="minimumScore" title="${titleHeader}" sortable="true" />
	
	<security:authorize access="hasRole('BUYER')">
		<spring:message code="course.draftMode" var="draftMode" />
		<display:column title="${draftMode}">
			<jstl:if test="${row.draftMode==true}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/yes1.png" width="30" height="30">
				</div>
			</jstl:if>
			<jstl:if test="${row.draftMode==false}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/no1.png" width="30" height="30">
				</div>
			</jstl:if>
		</display:column>
		
			
			<spring:message code="course.createLesson" var="createLesson" />
			<display:column title="${createLesson}" sortable="false">
				<%-- <jstl:if test="${(row.newspaper.publicationDate!=null)and(row.draftMode==false)}"> --%>
					<jstl:if test="${row.draftMode}">
					<spring:url value="lesson/buyer/create.do" var="createURL">
						<spring:param name="courseId" value="${row.id}" />
					</spring:url>
					<a href="${createURL}"><spring:message code="course.lesson.create" /></a>
				</jstl:if>
			</display:column>
			
			<spring:message code="course.lesson.list" var="lessonHeader" />
			<display:column title="${lessonHeader}" sortable="false">
				<spring:url value="lesson/buyer/list.do" var="lessonURL">
					<spring:param name="courseId" value="${row.id }" />
				</spring:url>
				<a href="${lessonURL}"><spring:message code="course.lesson.list2" /></a>
			</display:column>
			
	</security:authorize>
	
	<!-- ENLACES -->
	

</display:table>


<security:authorize access="hasRole('BUYER')">
	<div>
		<a href="course/buyer/create.do"> <spring:message
				code="course.create" />
		</a>
	</div>
</security:authorize>
