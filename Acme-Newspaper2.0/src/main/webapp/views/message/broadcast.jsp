<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<div class="col-md-6 col-centered">
	<div class="well bs-component">
		<form:form action="${requestURI}" modelAttribute="m">
			

			<form:label path="priority">
				<spring:message code="message.priority" />:
			</form:label>
			
			<form:select path="priority">
				<form:options items="${priorities}" />
			</form:select>

			<br />
			<br />	
					
			<acme:textarea code="message.subject" path="subject" />
			<br />	
			
			<acme:textarea code="message.body" path="body" />
			<br />  
			

			<acme:submit name="sendBroadcast" code="message.send.broadcast.link"/>
			<acme:cancel url="messageFolder/admin/list.do" code="message.cancel.link"/>

<br />  
		</form:form>
	</div>
</div>