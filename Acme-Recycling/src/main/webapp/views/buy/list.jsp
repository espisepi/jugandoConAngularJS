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
	name="buys" requestURI="${requestURI}" id="row">
	


	<!-- ATRIBUTOS -->
	
	<spring:message code="buy.material.title"  var="title"/>
	<display:column title = "${title }" sortable="true">
		
		<jstl:out value="${row.material.title}"></jstl:out>
	</display:column>
	
	<spring:message code="buy.comment" var="titleHeader" />
	<display:column property="comment" title="${titleHeader}" sortable="true" />

	<spring:message code="buy.quantity" var="titleHeader" />
	<display:column property="quantity" title="${titleHeader}" sortable="true" />
	
	<spring:message code="buy.creditCard.holderName"  var="title"/>
	<display:column title = "${title }" sortable="true">
		
		<jstl:out value="${row.creditCard.holderName}"></jstl:out>
	</display:column>
	

	
</display:table>
