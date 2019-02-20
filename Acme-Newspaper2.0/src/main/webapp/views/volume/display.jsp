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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

	<display:table name="volume" class="displaytag"
		requestURI="${requestURI}" id="row">

		<display:column>

			<B><spring:message code="volume.title" /></B>
			<jstl:out value="${row.title},  "></jstl:out>

				<B><spring:message code="volume.description" /></B>
				<jstl:out value="${row.description},  "></jstl:out>
			<B><spring:message code="volume.year" /></B>
				<jstl:out value="${row.year}"></jstl:out>

		</display:column>		
	</display:table>
	
<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="newspapers" requestURI="${requestURI}" id="row">
<spring:message code="newspaper.display" var="display" />
		<display:column title="${display}" sortable="true" >
			<jstl:if test="${row.publicationDate!=null}">
				<spring:url value="newspaper/customer/display.do" var="displayURL">
					<spring:param name="newspaperId" value="${row.id }" />
				</spring:url>
				<a href="${displayURL}"><spring:message
						code="newspaper.display" /></a>
						</jstl:if>
		</display:column>
		
		
	<spring:message code="newspaper.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
<%-- 	<jstl:if test="${publicationDate}==null">
 --%>	<spring:message code="newspaper.format.publicationDate" var="pattern"></spring:message>
	<spring:message code="newspaper.publicationDate" var="postedHeader" />
	<display:column property="publicationDate" title="${postedHeader}"
		sortable="true" format="${pattern}" />
		
	<!-- Añadido imagen de public o private -->	
		
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
		
	</display:table>
	