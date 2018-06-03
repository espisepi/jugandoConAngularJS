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

<form:form action="lesson/buyer/edit.do" modelAttribute="lesson">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="course" />
	<form:hidden path="number" />

	<!-- ATRIBUTOS -->
	<B><acme:textbox code="lesson.title" path="title" /></B>
	<br />	
	<acme:textarea code="lesson.summary" path="summary" />
	<br />

	<!-- BOTONES -->
	<input type="submit" name="save" value="<spring:message code="lesson.save"/>" />&nbsp;
	
	<jstl:if test="${lesson.id !=0 }">
			<acme:submit_with_on_click name="delete" code="lesson.delete" code2="lesson.confirm.delete"/>
			
			
			
	</jstl:if>
		
	<acme:cancel url="course/buyer/list.do?d-16544-p=1" code="lesson.cancel" />
	
</form:form>

