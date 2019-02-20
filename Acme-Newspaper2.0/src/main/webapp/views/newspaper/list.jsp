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

<script type="text/javascript">
	function confirmDelete(newspaperId) {
		confirm=confirm('<spring:message code="newspaper.confirm.delete"/>');
		if (confirm)
		  window.location.href ="newspaper/admin/delete.do?newspaperId=" + newspaperId;
		  else
			  window.location.href ="newspaper/admin/list.do";
	}
</script>

<!-- 	SEARCH -->
<jstl:if test="${showSearch}">
<input type="text" id="keyword" value="">
	<input type="button" id="search"
		value="<spring:message code="newspaper.search"/>" />

	<security:authorize access="isAnonymous()">
		<input type="hidden" id="rol" value="/" />
	</security:authorize>
	<security:authorize access="hasRole('USER')">
		<input type="hidden" id="rol" value="/user/" />
	</security:authorize>
	<security:authorize access="hasRole('ADMIN')">
		<input type="hidden" id="rol" value="/admin/" />
	</security:authorize>
	<security:authorize access="hasRole('CUSTOMER')">
		<input type="hidden" id="rol" value="/customer/" />
	</security:authorize>

	<script type="text/javascript">
	$(document).ready(function() {
		$("#search").click(function() {
			if ($("#keyword").val()!="")
				window.location.replace('newspaper'+$("#rol").val()+'search.do?d-16544-p=1&keyword='+ $("#keyword").val());
			else
				window.location.replace('newspaper'+$("#rol").val()+'search.do?d-16544-p=1&keyword=');
		});
	});
	</script>
</jstl:if>



<br />


<display:table pagesize="5" class="displaytag" keepStatus="true"
	name="newspapers" requestURI="${requestURI}" id="row">
	
	

	<!-- 	DISPLAY PARA USER-->
	
	<security:authorize access="hasRole('USER')">
		<spring:message code="newspaper.display" var="display" />
		<display:column title="${display}" sortable="true" >
			<spring:url value="newspaper/user/display.do" var="displayURL">
				<spring:param name="newspaperId" value="${row.id }" />
			</spring:url>
			<a href="${displayURL}"><spring:message
					code="newspaper.display" /></a>
		</display:column>
	</security:authorize>
	
	<!-- DISPLAY PARA AGENT-->
	
	<security:authorize access="hasRole('AGENT')">
	<spring:message code="newspaper.display" var="Create" />
	<display:column title="${Create}" sortable="true">

		<spring:url value="newspaper/agent/display.do" var="createURL">
			<spring:param name="newspaperId" value="${row.id}" />
		</spring:url>
		<a href="${createURL}"><spring:message code="newspaper.display" /></a>

	</display:column>
</security:authorize>
	
	<!-- 	DISPLAY PARA CUSTOMER -->

	<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="newspaper.display" var="display" />
		<display:column title="${display}" sortable="true" >
				<spring:url value="newspaper/customer/display.do" var="displayURL">
					<spring:param name="newspaperId" value="${row.id }" />
				</spring:url>
				<a href="${displayURL}"><spring:message
						code="newspaper.display" /></a>
		</display:column>
	</security:authorize>
	
	<!-- 	DISPLAY PARA ADMIN -->

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="newspaper.display" var="display" />
		<display:column title="${display}" sortable="true" >
				<spring:url value="newspaper/admin/display.do" var="displayURL">
					<spring:param name="newspaperId" value="${row.id }" />
				</spring:url>
				<a href="${displayURL}"><spring:message
						code="newspaper.display" /></a>
		</display:column>
	</security:authorize>
	
	<!--  EDIT -->
	
	<security:authorize access="hasRole('USER')">
	<jstl:if test="${showButtonEdit}">
		<spring:message code="newspaper.edit" var="Edit" />
		<display:column title="${Edit}" sortable="true">
			<jstl:if test="${row.publicationDate==null}">
				<spring:url value="newspaper/user/edit.do" var="editURL">
					<spring:param name="newspaperId" value="${row.id}" />
				</spring:url>
				<a href="${editURL}"><spring:message code="newspaper.edit" /></a>
			</jstl:if>
		</display:column>
	</jstl:if>
	</security:authorize>

	<!-- DISPLAY -->	

	<security:authorize access="isAnonymous()">
	<spring:message code="newspaper.display" var="Display" />
	<display:column title="${Display}" sortable="true" >
		<spring:url value="newspaper/display.do" var="editURL">
			<spring:param name="newspaperId" value="${row.id}" />
		</spring:url>
		<a href="${editURL}"><spring:message code="newspaper.display" /></a>
	</display:column>
	</security:authorize>
	
	

	<!-- ATRIBUTOS -->

	<spring:message code="newspaper.title" var="titleHeader" />
	<display:column property="title" title="${titleHeader}" sortable="true" />

	<spring:message code="newspaper.format.publicationDate" var="pattern"></spring:message>
	<spring:message code="newspaper.publicationDate" var="postedHeader" />
	<display:column property="publicationDate" title="${postedHeader}"
		sortable="true" format="${pattern}" />
		
		
	<!-- Articles -->
	
	<security:authorize access="isAnonymous()">
		<spring:message code="newspaper.articles" var="articles" />
		<display:column title="${articles}" sortable="true" >
			<spring:url value="article/list.do" var="articleURL">
				<spring:param name="newspaperId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articleURL}"><spring:message
					code="newspaper.articles" /></a>
		</display:column>
	</security:authorize>
	
	<!-- ESTE LO USA ESPI -->
	<security:authorize access="hasRole('USER')">
	<jstl:if test="${showMyArticles}">
		<spring:message code="newspaper.articles" var="articles" />
		<display:column title="${articles}" sortable="true" >
			<spring:url value="article/user/listMyArticles.do" var="articleURL">
				<spring:param name="newspaperId" value="${row.id }" />
				<spring:param name="d-16544-p" value="1" />
			</spring:url>
			<a href="${articleURL}"><spring:message
					code="newspaper.articles" /></a>
		</display:column>
	</jstl:if> 
	
	<jstl:if test="${showButtonPublish}">
	<spring:message code="newspaper.publish" var="publish" />
		<display:column title="${publish}" sortable="true" >
			<jstl:if test="${row.publicationDate == null}">
				<spring:url value="newspaper/user/publish.do" var="publishURL">
					<spring:param name="newspaperId" value="${row.id }" />
				</spring:url>
				<a href="${publishURL}"><spring:message
						code="newspaper.publish" /></a>
			</jstl:if>		
		</display:column>
	</jstl:if>	
	</security:authorize>
	
	<security:authorize access="hasRole('CUSTOMER')">
		<jstl:if test="${showButtonSubscription}">
		<spring:message code="newspaper.subscription" var="subscription" />
		<display:column title="${subscription}" sortable="true" >
			<jstl:if test="${!newspapersSubscribed.contains(row)}">
				<spring:url value="subscription/customer/create.do" var="subscriptionURL">
					<spring:param name="newspaperId" value="${row.id }" />
				</spring:url>
				<a href="${subscriptionURL}"><spring:message
						code="newspaper.subscription" /></a>
			</jstl:if>		
		</display:column>
		</jstl:if>
	</security:authorize>
	
	<!-- Boton de delete para el admin ya que puede borrar los periódicos que quiera pero no editarlas -->
	<security:authorize access="hasRole('ADMIN')">
	<jstl:if test="${showDelete}">
	<spring:message code="newspaper.delete" var="deleteHeader" />
		<display:column title="${deleteHeader}" sortable="true">
			<input type="button" name="delete"
				value="<spring:message code="newspaper.delete" />"
				onclick="confirmDelete(${row.id});" />
		</display:column>
	</jstl:if>
	</security:authorize>
	
	
	<security:authorize access="hasRole('USER')">
		<spring:message code="newspaper.open" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.open==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.open==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
		</security:authorize>
		
		
		
		<security:authorize access="hasRole('ADMIN')">
		<spring:message code="newspaper.open" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.open==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.open==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
		</security:authorize>
		
		
		<security:authorize access="hasRole('CUSTOMER')">
		<spring:message code="newspaper.open" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.open==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.open==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
		</security:authorize>
	
	<!-- LA PARTE DE AGENTE -->

<security:authorize access="hasRole('AGENT')">
		<spring:message code="newspaper.open" var="draftMode" />
	<display:column title="${draftMode}">
		<jstl:if test="${row.open==true}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/yes.png" width="30" height="30">
			</div>
		</jstl:if>
		<jstl:if test="${row.open==false}">
			<div
				style="position: relative; width: 30px; height: 30px; margin-left: auto; margin-right: auto;">

				<img src="images/no.png" width="30" height="30">
			</div>
		</jstl:if>
		
</display:column>
		</security:authorize>
		
		<security:authorize access="hasRole('AGENT')">
	<spring:message code="newspaper.advertisement" var="Create" />
	<display:column title="${Create}" sortable="true">
	

		<spring:url value="advertisement/agent/create.do" var="createURL">
			<spring:param name="newspaperId" value="${row.id}" />
		</spring:url>
		<a href="${createURL}"><spring:message code="newspaper.advertisement.create" /></a>
	
	</display:column>
</security:authorize>
	

</display:table>
<jstl:if test="${showCreate}">
<security:authorize access="hasRole('USER')">
	<div>
		<a href="newspaper/user/create.do"> <spring:message
				code="newspaper.create" />
		</a>
	</div>
</security:authorize>
</jstl:if>
