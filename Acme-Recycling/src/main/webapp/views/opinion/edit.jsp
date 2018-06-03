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
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<form:form action="${requestURI}" modelAttribute="opinionForm">

	<form:hidden path="opinion.id" />
	<form:hidden path="opinion.version" />

	<B><acme:textbox code="opinion.title" path="opinion.title" /></B>
	<br />
	<B><acme:textbox code="opinion.comment" path="opinion.comment" /></B>
	<br />
	<B><acme:textbox code="opinion.photo" path="opinion.photo" placeHolder="http://"/></B>
	<br />
	<B><acme:textbox code="opinion.caption" path="opinion.caption" /></B>
	<br />

	<jstl:if test="${!hiddenSelects}">

		<jstl:if test="${showItem}">
			<acme:select items="${products}" itemLabel="title"
				code="opinion.product" path="opinableId" />
			<br />
			<form:hidden path="opinableItem" value="1" />
		</jstl:if>


		<jstl:if test="${!showItem}">
			<acme:select items="${courses}" itemLabel="title"
				code="opinion.course" path="opinableId" />
			<br />
			<form:hidden path="opinableItem" value="0" />
		</jstl:if>

	</jstl:if>

	<!-- BOTONES -->

	<input type="submit" name="save"
		value="<spring:message code="opinion.save"/>" />&nbsp;
		
	<security:authorize access="isAuthenticated()">
		<jstl:if test="${opinableItem}">
			<acme:cancel url="opinion/actor/myListOpinionItem.do?d-16544-p=1"
				code="opinion.cancel" />
		</jstl:if>

		<jstl:if test="${!opinableItem}">
			<acme:cancel url="opinion/actor/myListOpinionCourse.do?d-16544-p=1"
				code="opinion.cancel" />
		</jstl:if>
	</security:authorize>

</form:form>

