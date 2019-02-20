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


<display:table name="article" class="displaytag"
	requestURI="${requestURI}" id="row">

	<!-- Attributes -->

<display:column>

<B><spring:message code="article.newspaper" /></B>
	<jstl:out value="${row.newspaper.title}"></jstl:out>
	

	<p>
	
	<B><spring:message code="article.title" /></B>
	<jstl:out value="${row.title}"></jstl:out>
	

	<p>
		<B><spring:message code="article.summary" /></B>
		<jstl:out value="${row.summary}"></jstl:out>
	</p>
		
	<p>
		<B><spring:message code="article.body" /></B>
		<jstl:out value="${row.body}"></jstl:out>
	</p>
	
	<p>
		<B><spring:message code="article.draftMode" /></B>
		<jstl:out value="${row.draftMode}"></jstl:out>
	</p>
	
	<p>
			<jstl:if test="${row.publishedMoment!=null }">
 			<spring:message code="article.format.publishedMoment1" var="pattern"></spring:message>
			<fmt:formatDate value="${row.publishedMoment}" pattern="${pattern}" var="newdatevar" />
			<B><spring:message code="article.publishedMoment"></spring:message></B>
			<c:out value="${newdatevar}" />
			</jstl:if>
			
			<jstl:if test="${row.publishedMoment==null}">
			<B><spring:message code="article.publishedMoment"></spring:message></B>
			<B><spring:message code ="nothing.found"></spring:message></B>
			</jstl:if>
	
	</p>
	
	
	
	<spring:message code="article.pictures" var="titleHeader" />
	<jstl:if test="${row.pictures.size()!=0}">
	<table>
	<tr>
	<jstl:forEach items="${row.pictures}" var="picture">
	<td><img src="${picture}" width="250" height="150"></td>
	</jstl:forEach>
</tr>
</table>
</jstl:if>

<jstl:if test="${row.pictures.size()==0}">
			<B><spring:message code="article.pictures"></spring:message></B>
			<B><spring:message code ="nothing.found.images"></spring:message></B>
			</jstl:if>

<h2><spring:message code="article.followsUp" /></h2>
<display:table name="followsUp" id="row1" class="displaytag">

	<display:column>
	<B><spring:message code="article.followsUp.title" /></B>
	<jstl:out value="${row1.title}"></jstl:out>
	

	<p>
		<B><spring:message code="article.followsUp.summary" /></B>
		<jstl:out value="${row1.summary}"></jstl:out>
	</p>
		
	<p>
		<B><spring:message code="article.followsUp.text" /></B>
		<jstl:out value="${row1.text}"></jstl:out>
	</p>
	
	
	<p>
 			<spring:message code="article.format.publishedMoment1" var="pattern"></spring:message>
			<fmt:formatDate value="${row1.publicationMoment}" pattern="${pattern}" var="newdatevar" />
			<B><spring:message code="article.followsUp.publicationMoment"></spring:message></B>
			<c:out value="${newdatevar}" />
	
	</p>
	
	
	<spring:message code="article.pictures" var="titleHeader" />
	<table>
	<tr>
	<jstl:forEach items="${row1.pictures}" var="picture">
	<td><img src="${picture}" width="250" height="150"></td>
	</jstl:forEach>
</tr>
</table>
</display:column>
</display:table>


<h2><spring:message code="advertisement.random" /></h2>
<display:table name="advertisementrandom" id="row" class="displaytag">
<jstl:if test="${row!=adnull}">
		<display:column>

	<a href="${row.targetPage}"><spring:message code = "advertisement.targetPage"></spring:message></a>
	
	<p>		
	<B><jstl:out value="Banner:"></jstl:out></B>
		<a href="${row.banner}"><img src="${row.banner}" width="250" height="150" ></a>
	</p>
	
	<p>
		<B><spring:message code="advertisement.title" /></B>
		<jstl:out value="${row.title}"></jstl:out>
	</p>
	
	<p>
		<B><spring:message code="advertisement.agent" /></B>
		<jstl:out value="${row.agent.name}"></jstl:out>
	</p>
	</display:column>
	</jstl:if>
</display:table>
	
</display:column>

	</display:table>
	
	