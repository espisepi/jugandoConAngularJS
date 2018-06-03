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
	name="comments" requestURI="${requestURI}" id="row">
	
<!--  EDIT -->
	<security:authorize access="hasRole('RECYCLER')">
		<spring:message code="comment.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			
				<spring:url value="comment/recycler/edit.do" var="editURL">
					<spring:param name="commentId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="comment.edit" /></a>
		</display:column>
	</security:authorize>


	<!-- ATRIBUTOS -->

	<spring:message code="comment.body" var="titleHeader" />
	<display:column property="body" title="${titleHeader}" sortable="true" />

	<spring:message code="comment.format.publicationDate" var="pattern"></spring:message>
	<spring:message code="comment.createdMoment" var="postedHeader" />
	<display:column property="createdMoment" title="${postedHeader}"
		sortable="true" format="${pattern}" />


<!-- Boton para responder a un comentario -->
	<security:authorize access="hasRole('RECYCLER')">
		<spring:message code="reply" var="CreateComments" />
		<display:column title="${CreateComments}" sortable="true">
			<spring:url value="comment/recycler/createReply.do"
				var="createReplyCommentURL">
				<spring:param name="commentId" value="${row.id}" />
			</spring:url>
			<a href="${createReplyCommentURL}"><spring:message
					code="reply" /></a>
		</display:column>
	</security:authorize>
</display:table>
