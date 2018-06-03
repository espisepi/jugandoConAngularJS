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
	function confirmDelete(incidenceId) {
		confirm=confirm('<spring:message code="incidence.confirm.delete"/>');
		if (confirm)
		  window.location.href ="incidence/admin/delete.do?incidenceId=" + incidenceId;
		  else
			  window.location.href ="incidence/admin/list.do";
	}
</script>
<!-- Listing messageFodler -->
<display:table name="incidences" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">
	
	<spring:message code="incidence.display" var="Display"/>
		
		<display:column title="${Display}" sortable="true">

			<spring:url value="${RequestURIdisplay}" var="displayURL">
				<spring:param name="incidenceId" value="${row.id}" />
			</spring:url>
			<a href="${displayURL}"><spring:message code="incidence.display" /></a>
		</display:column>
	
	<security:authorize access="hasRole('RECYCLER')">
	<spring:message code="incidence.edit" var="Edit"/>
		
		<display:column title="${Edit}" sortable="true">
		
		<jstl:if test="${row.resolved==false}">
			<spring:url value="${RequestURIedit}" var="editURL">
				<spring:param name="incidenceId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="incidence.edit" /></a>
		
		</jstl:if>
		</display:column>
	</security:authorize>
	<!-- Attributes -->

	<acme:column code="incidence.title" property="title" sortable ="true"/>
	
	<spring:message code="message.format.date" var="pattern"></spring:message>
	<spring:message code="incidence.createdMoment" var="postedHeader" />
	<display:column property="createdMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="incidence.resolved" var="resolved" />
	<display:column title="${resolved}">
		<jstl:if test="${row.resolved==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes1.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.resolved==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no1.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
	
		
	
		
<!-- Enlace para cambiar a resuelta la incidencia --->
<security:authorize access="hasRole('MANAGER')">

	<jstl:if test="${row.resolved==false}">
	<spring:message code="incidence.resolve" var="resolve"/>
		
		<display:column title="${resolve}" sortable="true">
		
		
			<spring:url value="${RequestURIresolve}" var="resolveURL">
				<spring:param name="incidenceId" value="${row.id}" />
			</spring:url>
			<a href="${resolveURL}"><spring:message code="incidence.resolve" /></a>
		
		
		</display:column>
		</jstl:if>
	</security:authorize>
	

<!-- Attributes PARA EL ADMIN -->
<security:authorize access="hasRole('ADMIN')">

<acme:column code="incidence.reasonWhy" property="reasonWhy" sortable ="true"/>

<acme:column code="incidence.comment" property="comment" sortable ="true"/>




<!-- Boton de delete para el admin ya que puede borrar las noticias que quiera pero no editarlas -->
	<security:authorize access="hasRole('ADMIN')">
	
	<spring:message code="incidence.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="incidence.delete" />"
				onclick="confirmDelete(${row.id});" />
		</display:column>

	</security:authorize>
		
</security:authorize>
</display:table>



