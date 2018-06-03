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

<form:form action="${requestURI}" modelAttribute="editorForm">
	
<form:hidden path="editor.id" />
	
	
<jstl:if test="${editorForm.editor.id == 0}">
			<B><acme:textbox code="editor.username"
				path="editor.userAccount.username" /><br /></B>
			<B><acme:password code="editor.password"
				path="editor.userAccount.password" /><br /></B>
			<B><acme:password code="editor.password" path="passwordCheck" /></B>
			<br />
		</jstl:if>	
	
	<B><acme:textbox code="editor.name" path="editor.name"/></B>
	<br />
	<B><acme:textbox code="editor.surname" path="editor.surname"/></B>
	<br />
	<B><acme:textbox code="editor.emailAddress" path="editor.email"/></B>
	<br />
	<B><acme:textbox code="editor.phoneNumber" path="editor.phone" /></B>
	<br />
	<B><acme:textbox code="editor.postalAddress" path="editor.address"/></B>
	<br />
	<B><acme:provinceselect code="editor.province" path="editor.province"/></B>
	<br />
	
	
	
	<acme:submit name="save" code="editor.save"/>
	<acme:cancel url="welcome/index.do" code="editor.cancel"/>
	<br />
	<br/>
	<jstl:if test="${editorForm.editor.id == 0}">
   		<form:label path="conditions">
		<spring:message code="editor.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="editor.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>