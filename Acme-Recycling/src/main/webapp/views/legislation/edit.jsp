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

<form:form action="legislation/admin/edit.do" modelAttribute="legislation">

	<form:hidden path="id" />
	<form:hidden path="version" />
	
	<B><acme:textbox code="legislation.title" path="title"/></B>
	<br />
	
	<B><acme:textbox code="legislation.body" path="body" /></B>
	<br />
	
	<B><acme:textbox code="legislation.link" path="link" placeHolder="http://imagen1"/></B>
	<br />
		
	

			
	

<!-- 	//BOTONES -->
	
	<input type="submit" name="save"
		value="<spring:message code="legislation.save" />" />&nbsp; 
	
	<jstl:if test="${legislation.id != 0 }">
	 	<acme:submit_with_on_click name="delete" code="legislation.delete" code2="legislation.confirm.delete"/>
	</jstl:if>
	
	<input type="button" name="cancel"
		value="<spring:message code="legislation.cancel" />"
		onclick="javascript:  window.location.replace('configurationSystem/admin/legislation/list.do?d-16544-p=1');" />
	<br />
</form:form>