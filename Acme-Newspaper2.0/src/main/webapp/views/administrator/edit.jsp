<%--
 * edit.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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




<form:form action="profile/admin/edit.do"
	modelAttribute="adminForm">

	<form:hidden path="admin.id" />
	<form:hidden path="admin.version" />



	<security:authorize access="hasRole('ADMIN')">

		<jstl:if test="${adminForm.admin.id == 0}">
			<acme:textbox code="admin.username"
				path="admin.userAccount.username" /><br />
			<acme:password code="admin.password"
				path="admin.userAccount.password" /><br />
			<acme:password code="admin.password" path="passwordCheck" />
			<br />
		</jstl:if>

		<acme:textbox code="admin.name" path="admin.name" /><br />
		<acme:textbox code="admin.surname"
			path="admin.surname" /><br />
		<acme:textbox code="admin.emailAddress"
			path="admin.email" /><br />
		<acme:textbox code="admin.postalAddress"
			path="admin.postalAddress" /><br />
		<acme:textbox code="admin.phoneNumber"
			path="admin.phone"/><br />

		<jstl:if test="${adminForm.admin.id == 0}">
			<form:label path="conditions">
				<spring:message code="actor.legal.accept" /> - <a
					href="welcome/legal.do"><spring:message
						code="actor.legal.moreinfo" /></a>
			</form:label>
			<form:checkbox id="conditions" path="conditions" />
			<form:errors cssClass="error" path="conditions" /><br />
		</jstl:if>
<br />

		<input type="submit" name="save"
			value="<spring:message code="admin.save" />" />&nbsp; 
	
	<input type="button" name="cancel"
			value="<spring:message code="admin.cancel" />"
			onclick="javascript: window.location.replace('welcome/index.do');" />
		<br />

	</security:authorize>

</form:form>