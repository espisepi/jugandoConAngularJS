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
	function confirmDelete(chirpId) {
		confirm=confirm('<spring:message code="chirp.confirm.delete"/>');
		if (confirm)
		  window.location.href ="chirp/admin/delete.do?chirpId=" + chirpId;
		  else
			  window.location.href ="chirp/admin/list.do";
	}
</script>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="chirps" requestURI="${requestURI}" id="row">



	<!-- ATRIBUTOS -->

	<spring:message code="chirp.format.postedMoment" var="pattern"></spring:message>
	<spring:message code="chirp.postedMoment" var="postedHeader" />
	<display:column property="postedMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />

	<spring:message code="chirp.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="chirp.description" var="descriptionHeader" />
	<display:column property="description" title="${descriptionHeader}" sortable="true"/>
	
	<!-- Boton de delete para el admin ya que puede borrar los chirps que quiera pero no editarlas -->
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${showDelete}">
	<spring:message code="chirp.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="chirp.delete" />"
				onclick="confirmDelete(${row.id});" />
		</display:column>
	</jstl:if>
	</security:authorize>
		

		
</display:table>





