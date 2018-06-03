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
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<display:table name="item" class="displaytag"
	requestURI="${requestURI}" id="row">
	<display:column>
		<p>
		<B><spring:message code="item.title" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.title}"></jstl:out>
		</p>
		<p>
		<spring:message code="incidence.format.date" var="pattern"></spring:message>
		<B><spring:message code="incidence.createdMoment" /></B>
		<B><spring:message code="item.publicationMoment"  /></B>
		<jstl:out value=":" />
		<fmt:formatDate value="${row.publicationMoment}" pattern="${pattern}" var="pattern"></fmt:formatDate>
		<c:out value="${pattern}" />
		</p>
		<p>
		<B><spring:message code="item.description" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.description}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="item.quantity" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.quantity}"></jstl:out>
		</p>
		<p>
		
		<B><spring:message code="item.photo" /></B>
		<jstl:out value=":" />
		<jstl:if test="${row.photo!=''}">
			<img src="${row.photo}" width="200" height="200">
		</jstl:if>
		</p>
		
		<p>
		<B><spring:message code="item.value" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.value}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="item.labelProduct" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.labelProduct.name}"></jstl:out>
		</p>
		
		<security:authorize access="hasRole('RECYCLER')">
		<jstl:if test="${row.request != null and !hiddenRequest}">
		<p>
		<B><spring:message code="item.request" /></B>
		<jstl:out value=":" />
			<spring:url value="${RequestURIedit}" var="editURL">
				<spring:param name="requestId" value="${row.request.id}" />
			</spring:url>
			<a href="${editURL}"><spring:message code="item.request" /></a>
		</p>
		</jstl:if>
		</security:authorize>
		
		
	</display:column>

</display:table>