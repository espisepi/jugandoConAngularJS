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

<form:form action="${requestURI}" modelAttribute="newscast">
<form:hidden path="id" />
<form:hidden path="version" />
<form:hidden path="comments" />

	
<!-- ATRIBUTOS -->

<fieldset>
	
	
<B><acme:textarea code="new.title" path="title"/></B>


<B><acme:textarea code="new.content" path="content"/></B>
<B><acme:textarea code="new.pictures" path="pictures" placeHolder="http://imagen1, http://imagen2"/></B>

<B><acme:textbox code="new.creationDate" path="creationDate" readonly="true"/></B>
	</fieldset>

	<br>
<!-- BOTONES -->

	<input type="submit" name="save" value="<spring:message code="new.save" />" />&nbsp; 

	<jstl:if test="${newscast.id != 0}">
		<input type="submit" name="delete"
			value="<spring:message code="new.delete" />"
			onclick="javascript: return confirm('<spring:message code="new.confirm.delete" />')" />&nbsp;
	</jstl:if>

	<acme:cancel
		url="newscast/editor/list.do?d-16544-p=1"
		code="new.cancel" />
</form:form>
