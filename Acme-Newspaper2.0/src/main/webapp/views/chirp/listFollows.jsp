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
	name="chirps" requestURI="${requestURI}" id="row">


	<!-- ATRIBUTOS -->

	<spring:message code="chirp.format.postedMoment" var="pattern"></spring:message>
	<spring:message code="chirp.postedMoment" var="postedHeader" />
	<display:column property="postedMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="chirp.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="chirp.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true"/>
		

		<spring:message code="chirp.user" var="userHeader" />
		<display:column title="${userHeader}" sortable="true">
			<spring:url value="user/display.do" var="userURL">
				<spring:param name="userId" value="${row.user.id }" />
			</spring:url>
			<a href="${userURL}"><jstl:out value="${row.user.name }" /></a>
		</display:column>

</display:table>




