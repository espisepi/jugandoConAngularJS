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

<form:form action="subscription/customer/edit.do" modelAttribute="subscription">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="newspaper" />
	<%-- <form:hidden path="customer" /> --%>
	
<!-- ATRIBUTOS -->

	<fieldset>
	<B><acme:textbox code="subscription.creditCard.holderName" path="creditCard.holderName"/></B>
	<br>
	<B><acme:textbox code="subscription.creditCard.brandName" path="creditCard.brandName"/></B>
	<br>
	<B><acme:textbox code="subscription.creditCard.number" path="creditCard.number" placeHolder="Ej. XXXXXXXXXXXXXXX"/></B>
	<br>
	<B><acme:textbox code="subscription.creditCard.expirationMonth" path="creditCard.expirationMonth" placeHolder="XX"/></B>
	<br>
	<B><acme:textbox code="subscription.creditCard.expirationYear" path="creditCard.expirationYear" placeHolder="XX"/></B>
	<br>
	<B><acme:textbox code="subscription.creditCard.cvv" path="creditCard.cvv"/></B>
	</fieldset>
	
<!-- BOTONES -->

	<input type="submit" name="save" value="<spring:message code="subscription.save" />" />&nbsp; 

	<acme:cancel
		url="newspaper/customer/listAllPrivate.do?d-16544-p=1"
		code="subscription.cancel" />
</form:form>
