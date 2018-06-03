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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="material" class="displaytag"
	requestURI="${requestURI}" id="row">

	<spring:message code="material.material" var="titleMaterial"></spring:message>
	<display:column title="${titleMaterial }">

		<B><spring:message code="material.title" /><jstl:out value=":"/></B>
		<jstl:out value="${row.title }"></jstl:out>

		<p>
			<B><spring:message code="material.description" /><jstl:out value=":"/></B>
			<jstl:out value="${row.description }"></jstl:out>

		</p>
		<p>



			<B><spring:message code="material.unitPrice" /><jstl:out value=":"/></B>
			<jstl:out value="${row.unitPrice }"></jstl:out>
		</p>
		<p>
			<B><spring:message code="material.quantity" /><jstl:out value=":"/></B>
			<jstl:out value="${row.quantity }"></jstl:out>
		</p>
		<p>

			<B><spring:message code="material.totalPrice" /><jstl:out value=":"/></B>
			<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${row.totalPrice}" />
		</p>

		<p>

			<B><spring:message code="material.labelMaterial" /><jstl:out value=":"/></B>
			<jstl:out value="${row.labelMaterial.name }"></jstl:out>
		</p>



	</display:column>


</display:table>

<H1>
	<spring:message code="material.buy" var="titleBuy" />
	<strong> <jstl:out value="${titleBuy }"></jstl:out>
	</strong>
</H1>
<display:table name="buys" class="displaytag" requestURI="${requestURI}"
	id="row">



	<acme:column code="material.buy.quantity" property="quantity"
		sortable="true" />

	<acme:column code="material.buy.comment" property="comment"
		sortable="true" />

</display:table>