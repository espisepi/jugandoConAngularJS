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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>



<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="users" requestURI="${requestURI}" id="row">
	
	<security:authorize access="hasRole('USER')">
		<spring:message code="user.profile" var="userHeader" />
		<display:column title="${userHeader}" sortable="false">
			<spring:url value="user/display.do" var="userURL">
				<spring:param name="userId" value="${row.id }" />
			</spring:url>
			<a href="${userURL}"><spring:message code ="user.profile"/></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="user.profile" var="userHeader" />
		<display:column title="${userHeader}" sortable="true">
			<spring:url value="user/admin/display.do" var="userURL">
				<spring:param name="userId" value="${row.id }" />
			</spring:url>
			<a href="${userURL}"><spring:message code ="user.profile"/></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="user.profile" var="userHeader" />
		<display:column title="${userHeader}" sortable="true">
			<spring:url value="user/customer/display.do" var="userURL">
				<spring:param name="userId" value="${row.id }" />
			</spring:url>
			<a href="${userURL}"><spring:message code ="user.profile"/></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="isAnonymous()">
		<spring:message code="user.profile" var="userHeader" />
		<display:column title="${userHeader}" sortable="true">
			<spring:url value="user/display.do" var="userURL">
				<spring:param name="userId" value="${row.id }" />
			</spring:url>
			<a href="${userURL}"><spring:message code ="user.profile"/></a>
		</display:column>
	</security:authorize>
		
	
	<!-- Attributes -->

	<acme:column code="user.name" property="name"/>
	<acme:column code="user.surname" property="surname"/>
	
	<!-- Action links -->

		

	
	<jstl:if test="${seguidos}">
	<spring:message code="user.action" var="actionHeader" />
	<display:column title="${actionHeader}" sortable="false">
		
		<spring:url value="user/unfollow.do" var="unfollowURL">
		<spring:param name="userId" value="${row.id}"/>
		</spring:url>
		<a href="${unfollowURL}"><spring:message code="user.unfollow"/></a>
		
	</display:column>
	</jstl:if>

	
	<jstl:if test="${seguidos==false}">
	<spring:message code="user.action" var="actionHeader" />
	<display:column title="${actionHeader}" sortable="false">
		
		<spring:url value="user/follow.do" var="followURL">
		<spring:param name="userId" value="${row.id}"/>
		</spring:url>
		<a href="${followURL}"><spring:message code="user.follow"/></a>
		
	</display:column>
	</jstl:if>
	
	
	
	
</display:table>