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


<display:table name="request" class="displaytag"
	requestURI="${requestURI}" id="row">
	<display:column>
		<p>
		<B><spring:message code="request.code" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.code}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="request.title" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.title}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="request.observation" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.observation}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="request.status" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.status}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="request.carrier" /></B>
		<jstl:out value=":" />
		<jstl:out value="${row.carrier.name}"></jstl:out>
		</p>
		<p>
		<B><spring:message code="request.cleanPoint" /></B>
		<jstl:out value=":" />
		<jstl:forEach items="${row.cleanPoints}" var="cleanPoint">
			<jstl:out value="${cleanPoint.address}"></jstl:out>
		</jstl:forEach>
		</p>
	</display:column>

</display:table>