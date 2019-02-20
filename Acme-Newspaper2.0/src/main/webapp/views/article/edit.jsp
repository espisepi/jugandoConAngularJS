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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="article/user/edit.do" modelAttribute="article">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<%-- <form:hidden path="publishedMoment" />
	<form:hidden path="writer" />
	<form:hidden path="followUps" /> --%>
	<form:hidden path="newspaper" />
<!-- ATRIBUTOS -->
	<acme:textbox code="article.title" path="title"/>
	<br />
	<acme:textbox code="article.summary" path="summary"/>
	<br />
	<acme:textbox code="article.body" path="body"/>
	<br />
	<acme:textbox code="article.pictures" path="pictures" placeHolder="http://imagen1, http://imagen2"/>
	<br />
	<acme:booleanselect code="article.draftMode" path="draftMode"/>
	<br />
	
<!-- BOTONES -->

	<input type="submit" name="save" value="<spring:message code="article.save" />" />&nbsp; 

	<spring:url value="article/user/listMyArticles.do" var="editURL">
			<spring:param name="newspaperId" value="${article.newspaper.id}" />
			<spring:param name="d-16544-p" value="1" />
	</spring:url>
	<acme:cancel
		url="${editURL }"
		code="chirp.cancel" />
		
		
</form:form>
