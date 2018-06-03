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
<display:table name="labelProducts" id="row" requestURI="${requestURI}"
	pagesize="5" class="displaytag">

	<!--  EDIT -->
	<security:authorize access="hasRole('MANAGER')">

		<spring:message code="labelProduct.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			<jstl:if test="${row.items.size()==0 }">
				<jstl:if test="${row.byDefault==false }">
					<spring:url value="labelProduct/manager/edit.do" var="editURL">
						<spring:param name="labelProductId" value="${row.id}" />
					</spring:url>
					<a href="${editURL}"><spring:message code="labelProduct.edit" /></a>
				</jstl:if>
			</jstl:if>
		</display:column>

	</security:authorize>



	<!-- Attributes -->

	<acme:column code="labelProduct.name" property="name" sortable="true" />

	<spring:message code="labelProduct.byDefault" var="byDefault" />
	<display:column title="${byDefault}">
		<jstl:if test="${row.byDefault==true}">
			<div style="width: 30px; height: 30px; margin-left: 20px;">

				<img src="images/yes1.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.byDefault==false}">
			<div style="width: 30px; height: 30px; margin-left: 20px;">

				<img src="images/no1.png" width="30" height="30">
			</div>
		</jstl:if>
	</display:column>


	<!--  EDIT -->
	<security:authorize access="hasRole('MANAGER')">

		<spring:message code="labelProduct.delete" var="delete" />
		<display:column title="${delete}" sortable="true">
			<jstl:if test="${row.items.size()==0 }">
				<jstl:if test="${row.byDefault==false }">

					<spring:url value="labelProduct/manager/delete.do" var="deleteURL">
						<spring:param name="labelProductId" value="${row.id}" />
					</spring:url>
					<a href="${deleteURL}"><spring:message
							code="labelProduct.delete" /></a>
				</jstl:if>
			</jstl:if>
		</display:column>




	</security:authorize>


</display:table>

<security:authorize access="hasRole('MANAGER')">

	<spring:url value="labelProduct/manager/create.do" var="createURL" />
	<a href="${createURL}"><spring:message code="labelProduct.create" /></a>

</security:authorize>

