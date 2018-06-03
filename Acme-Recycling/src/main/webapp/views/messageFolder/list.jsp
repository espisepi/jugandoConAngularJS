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

<!-- Listing messageFodler -->
<display:table name="messageFolders" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

		
		<spring:message code="messageFolder.edit" var="Edit"/>
		
		<display:column title="${Edit}" sortable="true">
		
		<jstl:if test="${row.modifiable==true}">
			<spring:url value="${RequestURIedit}" var="editURL">
				<spring:param name="messageFolderId" value="${row.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="messageFolder.edit" /></a>
		
		</jstl:if>
		</display:column>
	

	
	
	<!-- Attributes -->

	<acme:column code="messageFolder.name" property="name" sortable ="true"/>
	
	<spring:message code="messageFolder.messages" var="Messages" />
		
		<display:column title="${Messages}" sortable="true">
			<spring:url value="${RequestURImessages}" var="messagesURL">
					<spring:param name="messageFolderId" value="${row.id}" />
				</spring:url>
			<a href="${messagesURL}"><spring:message code="messageFolder.messages" /></a>
		</display:column>


</display:table>



