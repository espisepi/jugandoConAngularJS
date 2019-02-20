<%--
 * list.jsp
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

<security:authorize access="hasRole('USER')">
<script type="text/javascript">
	function confirmDelete(messageId, messageFolderId) {
		confirm=confirm('<spring:message code="message.confirm.delete"/>');
		if (confirm)
		  window.location.href ="message/user/delete.do?messageId=" + messageId;
		  else
			  window.location.href ="message/user/list.do?messageFolderId=" + messageFolderId;
	}
</script>
</security:authorize>

<security:authorize access="hasRole('CUSTOMER')">
<script type="text/javascript">
	function confirmDelete(messageId, messageFolderId) {
		confirm=confirm('<spring:message code="message.confirm.delete"/>');
		if (confirm)
		  window.location.href ="message/customer/delete.do?messageId=" + messageId;
		  else
			  window.location.href ="message/customer/list.do?messageFolderId=" + messageFolderId;
	}
</script>
</security:authorize>

<security:authorize access="hasRole('AGENT')">
<script type="text/javascript">
	function confirmDelete(messageId, messageFolderId) {
		confirm=confirm('<spring:message code="message.confirm.delete"/>');
		if (confirm)
		  window.location.href ="message/agent/delete.do?messageId=" + messageId;
		  else
			  window.location.href ="message/agent/list.do?messageFolderId=" + messageFolderId;
	}
</script>
</security:authorize>

<security:authorize access="hasRole('ADMIN')">
<script type="text/javascript">
	function confirmDelete(messageId, messageFolderId) {
		confirm=confirm('<spring:message code="message.confirm.delete"/>');
		if (confirm)
		  window.location.href ="message/admin/delete.do?messageId=" + messageId;
		  else
			  window.location.href ="message/admin/list.do?messageFolderId=" + messageFolderId;
	}
</script>
</security:authorize>

<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="messages" requestURI="${requestURI }" id="row">
	
	<spring:message code="message.display" var="display" />	
	<display:column title="${display}" sortable="true">
		<spring:url value="${RequestURIDisplay}" var="displayURL">
			<spring:param name="messageId" value="${row.id}" />
		</spring:url>
		<a href="${displayURL}"><spring:message code="message.display" /></a>
		
		</display:column>


	<!-- Attributes -->
	
	<spring:message code="message.format.date" var="pattern"></spring:message>
	<spring:message code="message.moment" var="postedHeader" />
	<display:column property="moment" title="${postedHeader}"
		sortable="true" format="${pattern}" />
	
	<acme:column code="message.subject" property="subject"/>
	
	<acme:column code="message.sender" property="sender.name"/>
	
	<spring:message code="message.delete.link" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="message.delete.link" />"
				onclick="confirmDelete(${row.id}, ${row.messageFolder.id});" />
		</display:column>
	

	<spring:message code="message.changefolder.link" var="Move" />	
	<display:column title="${Move}" sortable="true">
		<spring:url value="${RequestURIChange}" var="changeURL">
			<spring:param name="messageId" value="${row.id}" />
		</spring:url>
		<a href="${changeURL}"><spring:message code="message.changefolder.link" /></a>
		
		</display:column>


</display:table>

