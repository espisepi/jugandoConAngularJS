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

<form:form action="agent/edit.do" modelAttribute="agentForm">
	
<form:hidden path="agent.id" />
	
	
<jstl:if test="${agentForm.agent.id == 0}">
			<acme:textbox code="agent.username"
				path="agent.userAccount.username" /><br />
			<acme:password code="agent.password"
				path="agent.userAccount.password" /><br />
			<acme:password code="agent.password" path="passwordCheck" />
			<br />
		</jstl:if>	
	
	<acme:textbox code="agent.name" path="agent.name"/>
	<br />
	<acme:textbox code="agent.surname" path="agent.surname"/>
	<br />
	<acme:textbox code="agent.postalAddress" path="agent.postalAddress"/>
	<br />
	<acme:textbox code="agent.phoneNumber" path="agent.phone" />
	<br />
	<acme:textbox code="agent.emailAddress" path="agent.email"/>
	<br />
	
	<acme:submit name="save" code="agent.save"/>
	<acme:cancel url="welcome/index.do" code="agent.cancel"/>
	<br />
	<br/>
	<jstl:if test="${agentForm.agent.id == 0}">
   		<form:label path="conditions">
		<spring:message code="actor.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="actor.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>