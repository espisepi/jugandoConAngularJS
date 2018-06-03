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


<display:table name="incidence" class="displaytag"
	requestURI="${requestURI}" id="row">
	<display:column>
		<p>
		<B><spring:message code="incidence.title" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.title}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="incidence.reasonWhy" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.reasonWhy}"></jstl:out>
		</p>
		<p>
		<spring:message code="incidence.format.date" var="pattern"></spring:message>
		<B><spring:message code="incidence.createdMoment" /></B>
		<jstl:out value=":" />
		<fmt:formatDate value="${row.createdMoment}" pattern="${pattern}" var="pattern" ></fmt:formatDate>
		<c:out value="${pattern}" />
		</p>
		<p>
		<B><spring:message code="incidence.comment" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.comment}"></jstl:out>
		</p>
		<p>
		<p>
		<B><spring:message code="incidence.recycler" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.recycler.name}"></jstl:out>
		</p>
		
		
	</display:column>

</display:table>