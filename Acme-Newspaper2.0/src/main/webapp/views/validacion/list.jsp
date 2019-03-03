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

<script type="text/javascript" src="scripts/validacion/validacionAngular.js"></script>
<script type="text/javascript" src="scripts/validacion/list.js"></script>
<div ng-controller="ListController">

<table ng-table ="tableParams" >
<tr ng-repeat="validacion in validacions">
<td data-title="'textMaxLength'" filter="{ textMaxLength: 'text'}">{{validacion.textMaxLength}}</td>
</tr>
</table>

</div>

<%-- <display:table pagesize="5" class="displaytag" keepStatus="true"
	name="validacions" requestURI="${requestURI}" id="row">
	
	<!-- Attributes -->

	<spring:message code="validacion.textMaxLength" var="titleHeader" />
	<display:column property="textMaxLength" title="${titleHeader}" sortable="true" />

	<spring:message code="validacion.email" var="titleHeader" />
	<display:column property="email" title="${titleHeader}" sortable="true" />
	
	<spring:message code="validacion.numberMax" var="titleHeader" />
	<display:column property="numberMax" title="${titleHeader}" sortable="true" />
	
	<spring:message code="validacion.numberMin" var="titleHeader" />
	<display:column property="numberMin" title="${titleHeader}" sortable="true" />
	
	<spring:message code="validacion.textPattern" var="titleHeader" />
	<display:column property="textPattern" title="${titleHeader}" sortable="true" />
	
	<spring:message code="validacion.url" var="titleHeader" />
	<display:column property="url" title="${titleHeader}" sortable="true" />
	
</display:table>
 --%>
<spring:url value="validacion/create.do" var="displayURL" />
			<a href="${displayURL}"><spring:message
					code="validacion.create" /></a>