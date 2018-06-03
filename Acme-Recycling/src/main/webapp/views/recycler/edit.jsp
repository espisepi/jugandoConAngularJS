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

<form:form action="recycler/edit.do" modelAttribute="recyclerForm">
	
<form:hidden path="recycler.id" />
	
	
<jstl:if test="${recyclerForm.recycler.id == 0}">
			<B><acme:textbox code="recycler.username"
				path="recycler.userAccount.username" /><br /></B>
			<B><acme:password code="recycler.password"
				path="recycler.userAccount.password" /><br /></B>
			<B><acme:password code="recycler.password" path="passwordCheck" /></B>
			<br />
		</jstl:if>	
	
	<B><acme:textbox code="recycler.name" path="recycler.name"/></B>
	<br />
	<B><acme:textbox code="recycler.surname" path="recycler.surname"/></B>
	<br />
	<B><acme:textbox code="recycler.emailAddress" path="recycler.email"/></B>
	<br />
	<B><acme:textbox code="recycler.phoneNumber" path="recycler.phone" /></B>
	<br />
	<B><acme:textbox code="recycler.postalAddress" path="recycler.address"/></B>
	<br />
	<B><acme:provinceselect code="recycler.province" path="recycler.province"/></B>
	<br />
	
	
	
	<acme:submit name="save" code="recycler.save"/>
	<acme:cancel url="welcome/index.do" code="recycler.cancel"/>
	<br />
	<br/>
	<jstl:if test="${recyclerForm.recycler.id == 0}">
   		<form:label path="conditions">
		<spring:message code="recycler.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="recycler.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>