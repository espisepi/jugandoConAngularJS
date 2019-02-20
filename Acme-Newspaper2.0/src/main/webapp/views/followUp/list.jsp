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
	name="followUps" requestURI="${requestURI}" id="row">



	<!-- ATRIBUTOS -->



	<spring:message code="followUp.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="followUp.format.publicationMoment" var="pattern"></spring:message>
	<spring:message code="followUp.PublicationMoment" var="postedHeader" />
	<display:column property="publicationMoment" title="${postedHeader}" sortable="true" format="${pattern}" />

	<spring:message code="followUp.summary" var="summaryHeader" />
	<display:column property="summary" title="${summaryHeader}" sortable="true"/>
	
	<spring:message code="followUp.text" var="textHeader" />
	<display:column property="text" title="${textHeader}" sortable="true"/>
	
	<spring:message code="article.display" var="followDisplay" />
	<display:column title="${followDisplay}">
		<spring:url value="followUp/user/display.do" var="followUpPicturesURL">
			<spring:param name="followUpId" value="${row.id }" />
		</spring:url>
			<a href="${followUpPicturesURL}"><spring:message code="article.display" /></a>
	</display:column>

		
 </display:table>
 <%--
<security:authorize access="hasRole('USER')">
	<div>
		<a href="chirp/user/create.do"> 
			<spring:message	code="chirp.create" />
		</a>
	</div>
</security:authorize>
 --%>




