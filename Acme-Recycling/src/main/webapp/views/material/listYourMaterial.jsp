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
<display:table name="materials" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<!-- Finder para el Buyer -->
	<%-- <security:authorize access="hasRole('BUYER')">
	<form:form action="finder/buyer/search.do" modelAttribute="finder" >

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="materials" />

		<acme:textbox code="finder.keyWord" path="keyWord" />
		<br />
		<acme:textbox code="finder.lowPrice" path="lowPrice" />
		<br />
		<acme:textbox code="finder.highPrice" path="highPrice" />
		<br />
	
		<input type="submit" name="search" value="<spring:message code="finder.search" />" /> &nbsp; 	 
	</form:form> 
	</security:authorize> --%>

	





	<!-- Attributes -->

	<acme:column code="material.title" property="title" sortable="true" />

	<acme:column code="material.description" property="description"
		sortable="true" />

	<acme:column code="material.unitPrice" property="unitPrice"
		sortable="true" />
		
		<acme:column code="material.quantity" property="unitPrice"
		sortable="true" />
		
		<acme:column code="material.totalPrice" property="unitPrice"
		sortable="true" />


</display:table>



