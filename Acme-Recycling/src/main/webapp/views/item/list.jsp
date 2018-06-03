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


<script type="text/javascript">
	function confirmDelete(itemId) {
		confirm=confirm('<spring:message code="item.confirm.delete"/>');
		if (confirm)
		  window.location.href ="item/recycler/delete.do?itemId=" + itemId;
		  else
			  window.location.href ="item/recycler/list.do";
	}
</script>

<security:authorize access="hasRole('RECYCLER')">
<%-- <jstl:if test="${showScore}">
<h2><spring:message code="item.score" /></h2>
</jstl:if> --%>
</security:authorize>
<!-- Listing messageFodler -->
<display:table name="items" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<spring:message code="item.display" var="display"/>
		
		<display:column title="${display}" sortable="true">
		
		
		<spring:url value="${RequestUriDisplay}" var="displayURL">
			<spring:param name="itemId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message code="item.display" /></a>
		
		
	</display:column>
	
	<!-- Attributes -->

	<acme:column code="item.title" property="title" sortable ="true"/>
	
	<spring:message code="message.format.date" var="pattern"></spring:message>
	<spring:message code="item.publicationMoment" var="postedHeader" />
	<display:column property="publicationMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />
	
	<acme:column code="item.quantity" property="quantity" sortable ="true"/>
	
	<acme:column code="item.value" property="value" sortable ="true"/>
	
	<acme:column code="item.labelProduct" property="labelProduct.name" sortable ="true"/>
	
	<spring:message code="item.photo" var="photoVar" />
		<display:column title="${photoVar}" sortable="true">
			<jstl:if test="${row.photo!=''}">
				<div
					style="position: relative; width: 100px; height: 100px; margin-left: auto; margin-right: auto;">
				<jstl:if test="${!row.photo}">	
					<img src="${row.photo}" width="100" height="100">
				</jstl:if>
				</div>
			</jstl:if>
		</display:column>
	
	
	
	<jstl:if test="${showDelete}">
 	<spring:message code="item.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="item.delete" />"
				onclick="confirmDelete(${row.id});" />
	</display:column>
	</jstl:if>
	
	
	<!-- Listar las opiniones de ese item -->
	<spring:message code="item.opinions" var="opinions"/>		
	<display:column title="${opinions}" sortable="true">
		<spring:url value="opinion/list.do" var="displayURL">
			<spring:param name="opinableId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message code="item.opinions" /></a>
	</display:column>


</display:table>



