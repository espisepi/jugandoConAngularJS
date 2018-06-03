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
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<display:table name="cleanpoint" class="displaytag"
  requestURI="${requestURI}" id="row">
  <display:column>
  <br>
	<B><spring:message code="cleanPoint.address" /></B>
	 <jstl:out value="${row.address }"></jstl:out>
	 <br>
	 <B><spring:message code="cleanPoint.province" /></B>
	 <jstl:out value="${row.province }"></jstl:out><br>
	 
	 <jstl:if test="${!(row.phone=='') }">
  	<B><spring:message code="cleanPoint.phone" /></B>
  <jstl:out value="${row.phone }"></jstl:out><br></jstl:if>
  <B><spring:message code="cleanPoint.GPS.name" /></B>
  <jstl:out value="${row.location.name }"></jstl:out><br><br>
  
	<img src="https://maps.googleapis.com/maps/api/staticmap?zoom=13&size=600x300&maptype=roadmap&markers=color:red%7Clabel:C%7C${row.location.latitude },${row.location.longitude }&key=AIzaSyD6k6uBLY3StaXPY0ae8RJLTG0Ycs-QyxI">
	
	
	</display:column>
  
  
</display:table>