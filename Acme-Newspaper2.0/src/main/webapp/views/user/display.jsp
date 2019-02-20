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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<display:table name="user" class="displaytag"
  requestURI="${RequestUri}" id="row">
  
  <!-- Attributes -->
	
	<display:column>
	<B><spring:message code="user.name" />:</B>
	<jstl:out value="${row.name}"></jstl:out>
	

	<p>
		<B><spring:message code="user.surname" />:</B>
		<jstl:out value="${row.surname}"></jstl:out>
	</p>
		
	<p>
		<B><spring:message code="user.postalAddress" />:</B>
		<jstl:out value="${row.postalAddress}"></jstl:out>
	</p>

	<p>
		<B><spring:message code="user.phoneNumber" />:</B>
		<jstl:out value="${row.phone}"></jstl:out>
	</p>
	
	<p>
		<B><spring:message code="user.emailAddress" />:</B>
		<jstl:out value="${row.email}"></jstl:out>
	</p>	
	
	<security:authorize access="hasRole('ADMIN')">
	<p>
	<spring:message code="user.articles1" var="articlesHeader" />
	<B><jstl:out value ="${articlesHeader }:"></jstl:out></B>
			<spring:url value="article/admin/listArticlesOfUserNotDraftMode.do" var="articlesURL">
				<spring:param name="userId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articlesURL}"><spring:message code ="user.articles"/></a>
		
	</p>
	</security:authorize>
	<security:authorize access="hasRole('USER')">
	<spring:message code="user.articles1" var="articlesHeader" />
	<B><jstl:out value ="${articlesHeader }:"></jstl:out></B>
			<spring:url value="article/user/listArticles.do" var="articlesURL">
				<spring:param name="userId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articlesURL}"><spring:message code ="user.articles"/></a>
		
	
	</security:authorize>
	

	
		<security:authorize access="hasRole('CUSTOMER')">
	<p>
	
	<spring:message code="user.articles1" var="articlesHeader" />
	<B><jstl:out value ="${articlesHeader }:"></jstl:out></B>
			<spring:url value="article/customer/listb.do" var="articlesURL">
				<spring:param name="userId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articlesURL}"><spring:message code ="user.articles"/></a>
		
	</p></security:authorize>
	
		<security:authorize access="isAnonymous()">
	<p>
	
	<spring:message code="user.articles1" var="articlesHeader" />
	<B><jstl:out value ="${articlesHeader }:"></jstl:out></B>
			<spring:url value="article/listb.do" var="articlesURL">
				<spring:param name="userId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articlesURL}"><spring:message code ="user.articles"/></a>
		
	</p></security:authorize>
	
	<security:authorize access="hasRole('AGENT')">
	<p>
	
	<spring:message code="user.articles1" var="articlesHeader" />
	<B><jstl:out value ="${articlesHeader }:"></jstl:out></B>
			<spring:url value="article/agent/listb.do" var="articlesURL">
				<spring:param name="userId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articlesURL}"><spring:message code ="user.articles"/></a>
		
	</p></security:authorize>

		<p>
		<B><spring:message code="user.chirps1" />:</B>
			<spring:url value="${requestChirpsURL}" var="chirpURL">
			<spring:param name="userId" value="${row.id }" />
			<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${chirpURL}"><spring:message code="user.chirps" /></a>
	</p>
	
</display:column>
  
	
</display:table>