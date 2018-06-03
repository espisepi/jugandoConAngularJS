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


<script type="text/javascript">
	function confirmDelete(newscastId) {
		confirm=confirm('<spring:message code="new.confirm.delete"/>');
		if (confirm)
		  window.location.href ="newscast/admin/delete.do?newscastId=" + newscastId;
		  else
			  window.location.href ="newscast/admin/list.do";
	}
</script>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="newscast" requestURI="${requestURI}" id="row">
	
	<!--  EDIT -->
	
	<security:authorize access="hasRole('EDITOR')">
		<spring:message code="new.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
				<spring:url value="newscast/editor/edit.do" var="editURL">
					<spring:param name="newscastId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.edit" /></a>
		</display:column>
	</security:authorize>
	
	<!--  DISPLAY -->
	
	<security:authorize access="hasRole('EDITOR')">
		<spring:message code="new.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			
				<spring:url value="newscast/editor/display.do" var="editURL">
					<spring:param name="newscastId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.display" /></a>
		</display:column>
	</security:authorize>
	
	<!--  DISPLAY -->
	
	<security:authorize access="hasRole('RECYCLER')">
		<spring:message code="new.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			
				<spring:url value="newscast/recycler/display.do" var="editURL">
					<spring:param name="newscastId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.display" /></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('BUYER')">
		<spring:message code="new.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			
				<spring:url value="newscast/buyer/display.do" var="editURL">
					<spring:param name="newscastId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.display" /></a>
		</display:column>
	</security:authorize>

	<security:authorize access="isAnonymous()">
	<spring:message code="new.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			
				<spring:url value="newscast/display.do" var="editURL">
					<spring:param name="newscastId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.display" /></a>
		</display:column>
	</security:authorize>
	

	<!-- ATRIBUTOS -->

	<spring:message code="new.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />
	
	<spring:message code="new.content" var="titleHeader" />
	<display:column property="content" title="${titleHeader}" sortable="true" />
	
	<spring:message code="newspaper.format.publicationDate" var="pattern"></spring:message>
	<spring:message code="new.creationDate" var="postedHeader" />
	<display:column property="creationDate" title="${postedHeader}"
		sortable="true" format="${pattern}" />



<security:authorize access="hasRole('ADMIN')">
		<spring:message code="new.pictures" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.pictures.size()!=0}">
			
	<table>
	<tr>
	<jstl:forEach items="${row.pictures}" var="picture">
	<td><img src="${picture}" width="100" height="100"></td>
	</jstl:forEach>
</tr>
</table> 



		</jstl:if>
		
		<jstl:if test="${row.pictures.size()==0}">
			<B><spring:message code="new.pictures"></spring:message></B>
			<B><spring:message code ="nothing.found.images"></spring:message></B>
		</jstl:if>
		
</display:column>
		</security:authorize>


			
<!-- CREAR UN COMENTARIO -->
	<security:authorize access="hasRole('RECYCLER')">
	<spring:message code="new.comment" var="Create" />
	<display:column title="${Create}" sortable="true">
	

		<spring:url value="comment/recycler/create.do" var="createURL">
			<spring:param name="newscastId" value="${row.id}" />
		</spring:url>
		<a href="${createURL}"><spring:message code="new.comment.create" /></a>
	
	</display:column>
</security:authorize>
		
	
	<!-- Boton de delete para el admin ya que puede borrar las noticias que quiera pero no editarlas -->
	<security:authorize access="hasRole('ADMIN')">
	
	<spring:message code="new.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="new.delete" />"
				onclick="confirmDelete(${row.id});" />
		</display:column>

	</security:authorize> 
	
</display:table>
<security:authorize access="isAnonymous()">
<B><spring:message code="aviso1" /></B>
<a href="buyer/create.do"><spring:message code="aviso2"  /></a>
<B><spring:message code="aviso4" /></B>
<a href="recycler/create.do"><spring:message code="aviso3"  /></a>

</security:authorize>