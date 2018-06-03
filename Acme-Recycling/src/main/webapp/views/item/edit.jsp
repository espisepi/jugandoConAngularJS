
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


<form:form action="${requestURI}" modelAttribute="item">


	<form:hidden path="id" />

	
	<B><acme:textbox code="item.title" path="title"/></B>
	<br />
	
	<B><acme:textbox code="item.publicationMoment" path="publicationMoment" readonly="true"/></B>
	<br />
	
	<B><acme:textarea code="item.description" path="description"/></B>
	<br />
	
	<B><acme:textbox code="item.quantity" path="quantity"/></B>
	<br />
	
	<B><acme:textbox code="item.photo" path="photo" placeHolder="http://"/></B>
	<br />
	
	<B><acme:select items="${labelsProduct}" itemLabel="name" code="item.labelProduct" path="labelProduct"/></B>
	<br />
	
	
	<!-- 	//BOTONES -->
	
	<acme:submit name="save" code="item.save"/>
	
	<jstl:if test="${item.id != 0 }">
		<acme:submit_with_on_click code2="item.confirm.delete" name="delete" code="item.delete"/>
	</jstl:if>
	
	<acme:cancel url="${RequestURIcancel}" code="item.cancel"/>
	<br />

	
</form:form>