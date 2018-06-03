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



<h2><spring:message code="recycler.score" />
<jstl:out value="${puntuation}"></jstl:out></h2>

<display:table name="recycler" class="displaytag"
  requestURI="${requestURI}" id="row">
  
  <!-- Attributes -->
	
	<display:column>
	<B><spring:message code="recycler.name" /></B>
	<jstl:out value="${row.name}"></jstl:out>
	

	<p>
		<B><spring:message code="recycler.surname" /></B>
		<jstl:out value="${row.surname}"></jstl:out>
	</p>
	<p>
		<B><spring:message code="recycler.phoneNumber" /></B>
		<jstl:out value="${row.phone}"></jstl:out>
	</p>
	<p>
		<B><spring:message code="recycler.emailAddress" /></B>
		<jstl:out value="${row.email}"></jstl:out>
	</p>
		<p>
		<B><spring:message code="recycler.postalAddress" /></B>
		<jstl:out value="${row.address}"></jstl:out>
	</p>
	<p>
		<B><spring:message code="recycler.province" /></B>
		<jstl:out value="${row.province}"></jstl:out>
	</p>
	<p>
		<B><spring:message code="recycler.items" /></B>
			<spring:url value="${requestItemsURL}" var="itemsURL">
			<spring:param name="recyclerId" value="${row.id }" />
			<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${itemsURL}"><spring:message code="recycler.items1" /></a>
	</p>
	
	
</display:column>
  
</display:table>