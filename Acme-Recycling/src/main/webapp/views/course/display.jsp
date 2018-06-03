<%--
 * edit.jsp
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="course" class="displaytag"
	requestURI="${requestURI}" id="row">


<jstl:if test="${row.picture==''}">
	<spring:message code="nothing.found.to.display" />
	</jstl:if> 
	
	<jstl:if test="${!(row.picture=='') }">
	<spring:message code="course.picture" var="titleHeader" />
	<display:column title="${titleHeader}">

		<div
			style="position: relative; width: 500px; height: 300px; margin-left: auto; margin-right: auto;">

			<img src="${row.picture}" width="500" height="300">
		</div>
	</display:column>
	</jstl:if>
	
	<spring:message code="course.information" var="titleHeader" />
	<display:column title="${titleHeader}">

	<B><spring:message code="course.title" /></B>
	<jstl:out value="${row.title}"></jstl:out>
	<p>
	<B><spring:message code="course.description" /></B>
	<jstl:out value="${row.description}"></jstl:out>
	</p>
	

	
	
	<p>
	<spring:message code="course.format.realisedMoment1" var="pattern"></spring:message>
	<fmt:formatDate value="${row.realisedMoment}" pattern="${pattern}" var="newdatevar" />
	<B><spring:message code="course.realisedMoment"></spring:message></B>
	<c:out value="${newdatevar}" />
	</p>
		<p>
	<B><spring:message code="course.minimumScore" /></B>
	<jstl:out value="${row.minimumScore}"></jstl:out>
	</p>
	
	<security:authorize access="hasRole('BUYER')">
	
	<p>
	
	
			<jstl:if test="${row.draftMode==true}">
				<div style="vertical-align: middle; height=100;">
				<B><spring:message code="course.draftMode" /></B>	
					
				</div>
				<img src="images/yes1.png" width="60" height="60">
			</jstl:if>
			<jstl:if test="${row.draftMode==false}">
				<div style="vertical-align: middle; height=100;">
				<B><spring:message code="course.draftMode" /></B>	
					
				</div>
				<img src="images/no1.png" width="100" height="100">
			</jstl:if>
			</security:authorize>
		</display:column>
		

	<spring:message code="course.location" var="titleHeader" />
	<display:column title="${titleHeader}">
	
	<p>
	<B><spring:message code="course.location.name" /></B>
	<jstl:out value="${row.location.name}"></jstl:out>
	</p>
	
	<p>
	<B><spring:message code="course.location.longitude" /></B>
	<jstl:out value="${row.location.longitude}"></jstl:out>
	</p>
	
	<p>
	<B><spring:message code="course.location.latitude" /></B>
	<jstl:out value="${row.location.latitude}"></jstl:out>
	</p>
	
	</display:column>
	
	</display:table>
	
	<h2><spring:message code="course.lessons" /></h2>	
	<display:table name="lessons" id="row" class="displaytag">
	<spring:message code="course.lesson.number" var="titleHeader" />
	<display:column property="number" title="${titleHeader}" sortable="true" />
	
	<spring:message code="course.lesson.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="false" />
	
	<spring:message code="course.lesson.summary" var="titleHeader" />
	<display:column property="summary" title="${titleHeader}" sortable="false" />

</display:table>