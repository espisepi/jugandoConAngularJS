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


<!-- Listing messageFodler -->
<display:table name="actors" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<spring:message code="actor.display" var="display"/>
		
		<display:column title="${display}" sortable="true">
		
		
		<spring:url value="${RequestUriDisplay}" var="displayURL">
			<spring:param name="recyclerId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message code="actor.display" /></a>
		
		
	</display:column>
	
	<!-- Attributes -->

	<acme:column code="actor.name" property="name" sortable ="true"/>
	
	<acme:column code="actor.surname" property="surname" sortable ="true"/>
	
	<acme:column code="actor.email" property="email" sortable ="true"/>
	
	<acme:column code="actor.phone" property="phone" sortable ="true"/>
	
	<acme:column code="actor.address" property="address" sortable ="true"/>
	
	<acme:column code="actor.province" property="province" sortable ="true"/>
	
	<spring:message code="actor.items" var="items"/>
		<display:column title="${items}" sortable="true">
			<spring:url value="${RequestURIitems}" var="itemsURL">
				<spring:param name="actorId" value="${row.id}" />
			</spring:url>
			<a href="${itemsURL}"><spring:message code="actor.items" /></a>
		</display:column>
	
	
	<security:authorize access="hasRole('ADMIN')">

	<jstl:if test="${showbun}">
	<spring:message code="actor.ban" var="ban"/>
		
		<display:column title="${ban}" sortable="true">
		
			<jstl:if test="${row.userAccount.activated==true}">
			<spring:url value="${RequestURIban}" var="banURL">
				<spring:param name="actorId" value="${row.id}" />
			</spring:url>
			<a href="${banURL}"><spring:message code="actor.ban" /></a>
			</jstl:if>
		
		</display:column>
	</jstl:if>
		
	</security:authorize>
	
	
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${showunbun}">
	<spring:message code="actor.unban" var="unban"/>
		
		<display:column title="${unban}" sortable="true">
		
			<jstl:if test="${row.userAccount.activated==false}">
			<spring:url value="${RequestURIunban}" var="unbanURL">
				<spring:param name="actorId" value="${row.id}" />
			</spring:url>
			<a href="${unbanURL}"><spring:message code="actor.unban" /></a>
			</jstl:if>
		
		</display:column>
	</jstl:if>
	</security:authorize>
	

</display:table>



