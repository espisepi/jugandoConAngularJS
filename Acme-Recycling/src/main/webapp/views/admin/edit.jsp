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

<form:form action="${requestURI}" modelAttribute="adminForm">
	
<form:hidden path="admin.id" />
	
	
<jstl:if test="${adminForm.admin.id == 0}">
			<B><acme:textbox code="admin.username"
				path="admin.userAccount.username" /><br /></B>
			<B><acme:password code="admin.password"
				path="admin.userAccount.password" /><br /></B>
			<B><acme:password code="admin.password" path="passwordCheck" /></B>
			<br />
		</jstl:if>	
	
	<B><acme:textbox code="admin.name" path="admin.name"/></B>
	<br />
	<B><acme:textbox code="admin.surname" path="admin.surname"/></B>
	<br />
	<B><acme:textbox code="admin.emailAddress" path="admin.email"/></B>
	<br />
	<B><acme:textbox code="admin.phoneNumber" path="admin.phone" /></B>
	<br />
	<B><acme:textbox code="admin.postalAddress" path="admin.address"/></B>
	<br />
	<B><acme:provinceselect code="admin.province" path="admin.province"/></B>
	<br />
	
	
	
	<acme:submit name="save" code="admin.save"/>
	<acme:cancel url="welcome/index.do" code="admin.cancel"/>
	<br />
	<br/>
	<jstl:if test="${adminForm.admin.id == 0}">
   		<form:label path="conditions">
		<spring:message code="admin.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="admin.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>