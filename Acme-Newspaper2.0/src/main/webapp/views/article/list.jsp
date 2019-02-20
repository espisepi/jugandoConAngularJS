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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script type="text/javascript">
	function confirmDelete(articleId) {
		confirm=confirm('<spring:message code="article.confirm.delete"/>');
		if (confirm)
		  window.location.href ="article/admin/delete.do?articleId=" + articleId;
		  else
			  window.location.href ="article/admin/list.do";
	}
</script>

<jstl:if test="${showSearch}">

	<input type="text" id="keyword" value="">
	<input type="button" id="search"
		value="<spring:message code="article.search"/>" />

	<security:authorize access="isAnonymous()">
		<input type="hidden" id="rol" value="/" />
	</security:authorize>
	<security:authorize access="hasRole('USER')">
		<input type="hidden" id="rol" value="/user/" />
	</security:authorize>
	<security:authorize access="hasRole('ADMIN')">
		<input type="hidden" id="rol" value="/admin/" />
	</security:authorize>
	<security:authorize access="hasRole('CUSTOMER')">
		<input type="hidden" id="rol" value="/customer/" />
	</security:authorize>

	<script type="text/javascript">
	$(document).ready(function() {
		$("#search").click(function() {
			if ($("#keyword").val()!="")
				window.location.replace('article'+$("#rol").val()+'search.do?d-16544-p=1&keyword='+ $("#keyword").val());
			else
				window.location.replace('article'+$("#rol").val()+'search.do?d-16544-p=1&keyword=');
		});
	});
	</script>
</jstl:if>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="articles" requestURI="${requestURI}" id="row">

	<!--  EDIT -->

	<security:authorize access="hasRole('USER')">
		<jstl:if test="${showButtonEdit}">
			<spring:message code="article.edit" var="Edit" />
			<display:column title="${Edit}" sortable="true">
				<jstl:if test="${row.draftMode==true}">
					<spring:url value="article/user/edit.do" var="editURL">
						<spring:param name="articleId" value="${row.id}" />
					</spring:url>
					<a href="${editURL}"><spring:message code="article.edit" /></a>
				</jstl:if>
			</display:column>
		</jstl:if>
	</security:authorize>

	<!-- DISPLAY -->

	<security:authorize access="isAnonymous()">
		<spring:message code="article.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			<spring:url value="article/display.do" var="editURL">
				<spring:param name="articleId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="article.display" /></a>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('USER')">
		<spring:message code="article.display" var="Display" />
		<display:column title="${Display}" sortable="true">
			<spring:url value="article/user/display.do" var="editURL">
				<spring:param name="articleId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="article.display" /></a>
		</display:column>
	</security:authorize>

	<!-- 	DISPLAY PARA ADMIN -->

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.display" var="display" />
		<display:column title="${display}" sortable="true">
			<spring:url value="article/admin/display.do" var="displayURL">
				<spring:param name="articleId" value="${row.id }" />
			</spring:url>
			<a href="${displayURL}"><spring:message code="newspaper.display" /></a>
		</display:column>
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="article.display" var="display" />
		<display:column title="${display}" sortable="true">
			<spring:url value="article/customer/display.do" var="displayURL">
				<spring:param name="articleId" value="${row.id }" />
			</spring:url>
			<a href="${displayURL}"><spring:message code="newspaper.display" /></a>
		</display:column>
	</security:authorize>


	<!-- ATRIBUTOS -->

	<spring:message code="article.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />


	<spring:message code="article.format.publishedMoment" var="pattern"></spring:message>
	<spring:message code="article.publishedMoment" var="startDateHeader" />
	<display:column property="publishedMoment" title="${startDateHeader}"
		sortable="true" format="${pattern}" />

	<!-- Boton de delete para el admin ya que puede borrar los articles que quiera pero no editarlas -->
	<security:authorize access="hasRole('ADMIN')">
		<jstl:if test="${showDelete}">
			<spring:message code="article.delete" var="deleteHeader" />
			<display:column title="${deleteHeader}" sortable="true">
				<input type="button" name="delete"
					value="<spring:message code="article.delete" />"
					onclick="confirmDelete(${row.id});" />
			</display:column>
		</jstl:if>
	</security:authorize>


	<security:authorize access="hasRole('USER')">
		<spring:message code="article.draftMode" var="draftMode" />
		<display:column title="${draftMode}">
			<jstl:if test="${row.draftMode==true}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/yes.png" width="30" height="30">
				</div>
			</jstl:if>
			<jstl:if test="${row.draftMode==false}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/no.png" width="30" height="30">
				</div>
			</jstl:if>
		</display:column>
	</security:authorize>


	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.draftMode" var="draftMode" />
		<display:column title="${draftMode}">
			<jstl:if test="${row.draftMode==true}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/yes.png" width="30" height="30">
				</div>
			</jstl:if>
			<jstl:if test="${row.draftMode==false}">
				<div style="width: 30px; height: 30px; margin-left: 20px;">

					<img src="images/no.png" width="30" height="30">
				</div>
			</jstl:if>
		</display:column>
	</security:authorize>


	<jstl:if test="${showCreateFollowUp}">
		<security:authorize access="hasRole('USER')">
			<spring:message code="article.createFollowUp" var="createFollowUp" />
			<display:column title="${createFollowUp}" sortable="true">
				<%--  <jstl:if test="${!row.draftMode==true}">  --%>
				<jstl:if
					test="${(row.newspaper.publicationDate!=null)and(row.draftMode==false)}">
					<spring:url value="followUp/user/create.do" var="editURL">
						<spring:param name="articleId" value="${row.id}" />
					</spring:url>
					<a href="${editURL}"><spring:message code="article.followUp" /></a>
				</jstl:if>
			</display:column>
		</security:authorize>
	</jstl:if>

	<jstl:if test="${showListFollowUps}">
		<spring:message code="followUp.list" var="followUpsHeader" />
		<display:column title="${followUpsHeader}" sortable="true">
			<spring:url value="followUp/user/list.do" var="followUpURL">
				<spring:param name="articleId" value="${row.id }" />
			</spring:url>
			<a href="${followUpURL}"><spring:message code="followUp.list" /></a>
		</display:column>
	</jstl:if>
</display:table>

<!-- Boton de create article -->

<jstl:if test="${(newspaper.publicationDate == null) and (showCreate)}">
	<security:authorize access="hasRole('USER')">
		<div>
			<spring:url value="article/user/create.do" var="editURL">
				<spring:param name="newspaperId" value="${newspaper.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="article.create" /></a>
		</div>
	</security:authorize>
</jstl:if>