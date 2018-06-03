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

<form:form action="course/buyer/edit.do" modelAttribute="course">

<form:hidden path="id" />
<form:hidden path="version" />


	<B><acme:textbox code="course.title" path="title"/></B>
	<br />
	
	<B><acme:textbox code="course.realisedMoment" path="realisedMoment" placeHolder="yyyy/MM/dd"/></B>
	<br />
	
	<B><acme:textbox code="course.minimumScore" path="minimumScore"/></B>
	<br />
	
	<B><acme:booleanselect code="course.draftMode" path="draftMode"/></B>
	<br />
	
	<B><acme:selectmultiple items="${materials}" itemLabel="title" code="course.materials" path="materials"/></B>
	<br />
	
	<acme:textarea code="course.description" path="description" />
	
	
	<B><jstl:out value="GPS"></jstl:out></B>
	<fieldset>
	<B><acme:textbox code="course.location.name" path="location.name"/></B>
	<br>
	<B><acme:textbox code="course.location.latitude" path="location.latitude"/></B>
	<br>
	<B><acme:textbox code="course.location.longitude" path="location.longitude"/></B>
	<br>
	</fieldset>
	<br>


<input type="submit" name="save" value="<spring:message code="course.save" />" />&nbsp; 
	
	<jstl:if test="${course.id !=0 }">
			<acme:submit_with_on_click name="delete" code="course.delete" code2="course.confirm.delete"/>
			
			
			
	</jstl:if>
	
<acme:cancel url="course/buyer/list.do?d-16544-p=1"	code="course.cancel" />

</form:form>