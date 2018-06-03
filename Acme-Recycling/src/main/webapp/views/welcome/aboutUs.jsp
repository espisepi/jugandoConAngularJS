<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>


<font size="6" color = "green" face="Georgia"><B><jstl:out value="¿Quiénes somos?"></jstl:out></B></font>
<img src="${aboutUsPicture}" width="250" height="150" style="float:left;  " /><P ALIGN="center">

<P ALIGN="left">
<font size="5" color = "black" face="Georgia" ><jstl:out value="${whoAreWeSpanish}"></jstl:out></font>
</p>

<br>
<br>


<font size="6" color = "green" face="Georgia"><B><jstl:out value="¿Dónde nos encontramos?"></jstl:out></B></font>
<P ALIGN="left">
<font size="5" color = "black" face="Georgia" ><jstl:out value="${locationSpanish}"></jstl:out></font>

<br>
<br>
<br>

<%-- <font size="6" color = "green" face="Georgia"><B><jstl:out value="Organización"></jstl:out></B></font>
<P ALIGN="left">

<img src="images/Miguel_recycler.jpeg" width="170" height="270" style="float:center;  "><P ALIGN="center"> --%>