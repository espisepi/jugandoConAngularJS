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

<form:form action="user/edit.do" modelAttribute="userForm">

	<form:hidden path="user.id" />
	
	<jstl:choose>
			<jstl:when test="${userForm.user.id != 0}">
				<acme:textbox path="user.userAccount.username"
					code="user.username" readonly="true" /><br/>
			</jstl:when>
			<jstl:otherwise>
				<acme:textbox path="user.userAccount.username"
					code="user.username" /><br/>
			</jstl:otherwise>
		</jstl:choose>
		<jstl:choose>
			<jstl:when test="${userForm.user.id==0}">
				<acme:password code="user.password"
					path="user.userAccount.password" /><br/>
				<acme:password code="user.password" path="passwordCheck" /><br/>
			</jstl:when>
			<jstl:otherwise></jstl:otherwise>
		</jstl:choose>	
	
	<acme:textbox code="user.name" path="user.name"/>
	<br />
	<acme:textbox code="user.surname" path="user.surname"/>
	<br />
	<acme:textbox code="user.postalAddress" path="user.postalAddress"/>
	<br />
	<acme:textbox code="user.phoneNumber" path="user.phone" />
	
	<br />
	<acme:textbox code="user.emailAddress" path="user.email"/>
	<br />
	
	<acme:submit name="save" code="user.save"/>
	<acme:cancel url="welcome/index.do" code="user.cancel"/>
	<br />
	<br/>
	<jstl:if test="${userForm.user.id == 0}">
   		<form:label path="conditions">
		<spring:message code="actor.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="actor.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>
	
	
		
	

