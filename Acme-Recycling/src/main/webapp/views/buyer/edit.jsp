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

<form:form action="buyer/edit.do" modelAttribute="buyerForm">
	
<form:hidden path="buyer.id" />
	
	
<jstl:if test="${buyerForm.buyer.id == 0}">
			<B><acme:textbox code="buyer.username"
				path="buyer.userAccount.username" /><br /></B>
			<B><acme:password code="buyer.password"
				path="buyer.userAccount.password" /><br /></B>
			<B><acme:password code="buyer.password" path="passwordCheck" /></B>
			<br />
		</jstl:if>	
	
	<B><acme:textbox code="buyer.name" path="buyer.name"/></B>
	<br />
	<B><acme:textbox code="buyer.surname" path="buyer.surname"/></B>
	<br />
	<B><acme:textbox code="buyer.emailAddress" path="buyer.email"/></B>
	<br />
	<B><acme:textbox code="buyer.phoneNumber" path="buyer.phone" /></B>
	<br />
	<B><acme:textbox code="buyer.postalAddress" path="buyer.address"/></B>
	<br />
	<B><acme:provinceselect code="buyer.province" path="buyer.province"/></B>
	<br />
	
	
	
	<acme:submit name="save" code="buyer.save"/>
	<acme:cancel url="welcome/index.do" code="buyer.cancel"/>
	<br />
	<br/>
	<jstl:if test="${buyerForm.buyer.id == 0}">
   		<form:label path="conditions">
		<spring:message code="buyer.legal.accept"/> - <a href="welcome/legal.do"><spring:message code="buyer.legal.moreinfo"/></a>
		</form:label>
		<form:checkbox id="conditions" path="conditions"/>
		<form:errors cssClass="error" path="conditions"/>
   </jstl:if>
 <br/>
	</form:form>