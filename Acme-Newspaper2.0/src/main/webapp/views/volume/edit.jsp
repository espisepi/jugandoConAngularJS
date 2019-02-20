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
<form:form action="volume/user/edit.do" modelAttribute="volume">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<acme:textbox code="volume.title" path="title"/>
	<br />
 	<acme:textbox code="volume.description" path="description"/>
	<br /> 
	<%-- <acme:textbox code="volume.year" path="year" placeHolder="XXXX"/> --%>
	<br />
	<jstl:out value="Year"></jstl:out>
	<form:input path="year" type="number" value = "${volume.year}" max="3000" min ="1000"/>
	<acme:selectmultiple items="${newspapers}" itemLabel="title" code="volume.newspaper" path="newspapers"/>
	<br />
	
<!-- BOTONES -->

	<input type="submit" name="save" value="<spring:message code="newspaper.save" />" />&nbsp; 

	<acme:cancel
		url="volume/user/mylist.do?d-16544-p=1"
		code="chirp.cancel" />
</form:form>
