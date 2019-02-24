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

<form:form action="validacion/edit.do" modelAttribute="validacion">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
<!-- ATRIBUTOS -->

	<fieldset>
	<B><acme:textbox code="validacion.textMaxLength" path="textMaxLength"/></B>
	<br>
	<B><acme:textbox code="validacion.email" path="email"/></B>
	<br>
	<B><acme:textbox code="validacion.numberMax" path="numberMax"/></B>
	<br>
	<B><acme:textbox code="validacion.numberMin" path="numberMin"/></B>
	<br>
	<B><acme:textbox code="validacion.textPattern" path="textPattern"/></B>
	<br>
	<B><acme:textbox code="validacion.url" path="url"/></B>
	<br>
	
	</fieldset>
	
<!-- BOTONES -->

	<input type="submit" name="save" value="<spring:message code="validacion.save" />" />&nbsp; 

	<acme:cancel
		url="validacion/list.do?d-16544-p=1"
		code="validacion.cancel" />
</form:form>