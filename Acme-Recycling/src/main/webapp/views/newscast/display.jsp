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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="newscast" class="displaytag"
	requestURI="${requestURI}" id="row">

	
 <display:column>

	<B><spring:message code="new.title" /></B>
	<jstl:out value="${row.title}"></jstl:out>
	<p>
	
	<B><spring:message code="new.content" /></B>
	<jstl:out value="${row.content}"></jstl:out>
	<p>
	
	
 	<spring:message code="article.format.publishedMoment1" var="pattern"></spring:message>
	<fmt:formatDate value="${row.creationDate}" pattern="${pattern}" var="newdatevar" />
	<B><spring:message code="new.creationDate"></spring:message></B>
	<c:out value="${newdatevar}" />
			

<p>
<B><spring:message code="new.pictures"></spring:message></B>
	<spring:message code="new.pictures" var="titleHeader" />
	<jstl:if test="${row.pictures.size()!=0}">
	<table>
	<tr>
	<jstl:forEach items="${row.pictures}" var="picture">
	<td><img src="${picture}" width="250" height="150"></td>
	</jstl:forEach>
</tr>
</table>
</jstl:if>
	
<jstl:if test="${row.pictures.size()==0}">
			
			<B><spring:message code ="nothing.found.images"></spring:message></B>
			</jstl:if>
	
</display:column> 

</display:table>

	<h2><spring:message code="new.comments" /></h2>	
	<display:table name="comments" id="row" class="displaytag">
	<spring:message code="comment.body" var="titleHeader" />
	<display:column property="body" title="${titleHeader}" sortable="true" />
	<security:authorize access="hasRole('RECYCLER')">
	<spring:message code="new.comment.reply" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			
				<spring:url value="comment/recycler/createReply.do" var="editURL">
					<spring:param name="commentId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.comment.reply" /></a>
		</display:column>
	</security:authorize>
	<!--  EDIT -->
	
	<security:authorize access="hasRole('EDITOR')">
		<spring:message code="new.display.comment" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			
				<spring:url value="comment/editor/display.do" var="editURL">
					<spring:param name="commentId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="new.display.comment" /></a>
		</display:column>
	</security:authorize>
	

</display:table>
	