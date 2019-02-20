<%--
 * footer.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:useBean id="date" class="java.util.Date" />

<hr />

<div id="overbox3"> 
	<h2 align="center"><spring:message code="master.page.legal" /></h2>
	<h2 align="center"></h2>

    <div id="infobox3"  align="center">

        <a href="welcome/legal.do"><spring:message code="master.page.terminos"/></a>
     
    </div>
    <br/>
</div>
<div align="center">
<b>Copyright &copy; <fmt:formatDate value="${date}" pattern="yyyy" /> Acme-Newspaper Co., Inc.</b></div>