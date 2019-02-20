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

<form:form action="customer/edit.do" modelAttribute="customerForm">

	<form:hidden path="customer.id" />
	
	
<jstl:if test="${customerForm.customer.id == 0}">
			<acme:textbox code="customer.username"
				path="customer.userAccount.username" /><br />
			<acme:password code="customer.password"
				path="customer.userAccount.password" /><br />
			<acme:password code="customer.password" path="passwordCheck" />
			<br />
		</jstl:if>
	
	<acme:textbox code="customer.name" path="customer.name"/>
	<br />
	<acme:textbox code="customer.surname" path="customer.surname"/>
	<br />
	<acme:textbox code="customer.postalAddress" path="customer.postalAddress"/>
	<br />
	<acme:textbox code="customer.phoneNumber" path="customer.phone" />
	
	<br />
	<acme:textbox code="customer.emailAddress" path="customer.email"/>
	<br />
	
	<acme:submit name="save" code="customer.save"/>
	<acme:cancel url="welcome/index.do" code="customer.cancel"/>
	<br />
	<br/>
	<jstl:if test="${customerForm.customer.id == 0}">
   		<form:label path="conditions">
		<spring:message code="actor.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="actor.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>
	
	
		
	

