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
	name="newspapers" requestURI="${requestURI}" id="row">
	
	

	<!-- DISPLAY -->
	
	<security:authorize access="hasRole('AGENT')">
	<spring:message code="newspaper.display" var="Create" />
	<display:column title="${Create}" sortable="true">

		<spring:url value="newspaper/agent/display.do" var="createURL">
			<spring:param name="newspaperId" value="${row.id}" />
		</spring:url>
		<a href="${createURL}"><spring:message code="newspaper.display" /></a>

	</display:column>
</security:authorize>
	

	<!-- ATRIBUTOS -->

	<spring:message code="newspaper.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="newspaper.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true" />

	<spring:message code="newspaper.format.publicationDate" var="pattern"></spring:message>
	<spring:message code="newspaper.publicationDate" var="postedHeader" />
	<display:column property="publicationDate" title="${postedHeader}"
		sortable="true" format="${pattern}" />
		
		
	
	<!-- LA PARTE DE AGENTE -->

<security:authorize access="hasRole('AGENT')">
		<spring:message code="newspaper.open" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.open==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.open==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
		</security:authorize>
		
		

</display:table>

