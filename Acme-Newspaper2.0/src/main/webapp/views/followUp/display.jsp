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


<display:table name="followUp" class="displaytag"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->
<display:column>

	<B><spring:message code="followUp.title" /></B>
	<jstl:out value="${row.title}"></jstl:out>
	

	<p>
		<B><spring:message code="followUp.summary" /></B>
		<jstl:out value="${row.summary}"></jstl:out>
	</p>
		
	<p>
		<B><spring:message code="followUp.text" /></B>
		<jstl:out value="${row.text}"></jstl:out>
	</p>
	
	<p>
			
 			<spring:message code="followUp.format.publicationMoment1" var="pattern"></spring:message>
			<fmt:formatDate value="${row.publicationMoment}" pattern="${pattern}" var="newdatevar" />
			<B><spring:message code="followUp.PublicationMoment"></spring:message></B>
			<c:out value="${newdatevar}" />
			
			
	</p>
	
	
	
	<spring:message code="followUp.pictures" var="titleHeader" />
	<table>
	<tr>
	<jstl:forEach items="${row.pictures}" var="picture">
	<td><img src="${picture}" width="250" height="150"></td>
	</jstl:forEach>


</tr>
</table>
	
</display:column>
	</display:table>
	
	