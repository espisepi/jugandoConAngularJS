<%--
 * header.jsp
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

<div>
	<img src="${bannerURL}" alt="Acme-Recycling Co., Inc." />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message
						code="master.page.administrator" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/admin/edit.do"><spring:message
								code="master.page.buyer.edit" /></a></li>
					<li><a href="profile/admin/display.do"><spring:message
								code="master.page.buyer.profile" /></a></li>
					<li><a href="admin/dashboard.do"><spring:message
								code="master.page.statistics" /></a>
				</ul></li>
				
				<li><a class="fNiv"><spring:message
						code="master.page.responsabilidades" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="course/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.course.list" /></a></li>
					<li><a href="cleanPoint/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.administrator.cleanPoints" /></a></li>
					<li><a href="material/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.admin.material.list" /></a></li>
					<li><a href="actor/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.ban" /></a></li>
				</ul></li>

			

			<li><a class="fNiv"><spring:message
						code="master.page.administrator.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="carrier/admin/create.do"><spring:message
								code="master.page.admin.carrier.edit" /></a></li>
					<li><a href="manager_/admin/create.do"><spring:message
								code="master.page.admin.manager.edit" /></a></li>
					<li><a href="editor/admin/create.do"><spring:message
								code="master.page.admin.editor.edit" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/admin/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/admin/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.administrator.configurationSystem" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="configurationSystem/admin/tabooWord/list.do?d-16544-p=1"><spring:message
								code="master.page.administrator.tabooWords" /></a></li>
					<li><a
						href="configurationSystem/admin/legislation/list.do?d-16544-p=1"><spring:message
								code="master.page.administrator.legislation" /></a></li>
					<li><a href="configurationSystem/admin/edit.do"><spring:message
								code="master.page.administrator.configurationSystem.edit" /></a></li>
				</ul></li>


			<li><a class="fNiv"><spring:message
						code="master.page.tabooWord" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="newscast/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.news.tabooWord" /></a></li>
					<li><a href="incidence/admin/list.do?d-16544-p=1"><spring:message
								code="master.page.incidence" /></a></li>
				</ul></li>

				
			<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
				</ul>
			</li>


			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>


		</security:authorize>



		<security:authorize access="hasRole('BUYER')">


			<li><a class="fNiv"><spring:message
						code="master.page.buyer.buyer1" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/buyer/edit.do"><spring:message
								code="master.page.buyer.edit" /></a></li>
					<li><a href="profile/buyer/display.do"><spring:message
								code="master.page.buyer.profile" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/buyer/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/buyer/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/buyer/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.course" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="course/buyer/list.do?d-16544-p=1"><spring:message
								code="master.page.buyer.course" /></a></li>
				</ul></li>
				
				<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
				</ul>
			</li>

				<li><a href="newscast/buyer/list.do"><spring:message
						code="master.page.New.list" /></a></li>

		
			
				
				
			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message
						code="master.page.material.buy" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="material/buyer/list.do?d-16544-p=1"><spring:message
								code="master.page.material" /></a></li>
					<li><a href="buy/buyer/list.do?d-16544-p=1"><spring:message
								code="master.page.miscompras" /></a></li>
					<li><a href="finder/buyer/list.do?d-16544-p=1"><spring:message
								code="master.page.finder" /></a></li>
				</ul></li>
				
				<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>
				
		</security:authorize>

		<security:authorize access="hasRole('RECYCLER')">
			<li><a class="fNiv"><spring:message
						code="master.page.recycler.recyler1" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/recycler/edit.do"><spring:message
								code="master.page.recycler.edit" /></a></li>
					<li><a href="profile/recycler/display.do"><spring:message
								code="master.page.recycler.profile" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/recycler/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/recycler/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/recycler/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.comment" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="comment/recycler/list.do?d-16544-p=1"><spring:message
								code="master.page.comment.myList" /></a></li>
					<li><a href="newscast/recycler/list.do?d-16544-p=1"><spring:message
								code="master.page.new.create.comment" /></a></li>

				</ul></li>

			<li><a class="fNiv"><spring:message code="master.page.item" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="item/recycler/list.do?d-16544-p=1"><spring:message
								code="master.page.item.myList" /></a></li>
					<li><a href="item/recycler/create.do"><spring:message
								code="master.page.item.create" /></a></li>


				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.incidence" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="incidence/recycler/list.do?d-16544-p=1"><spring:message
								code="master.page.incidence.myList" /></a></li>
					<li><a href="incidence/recycler/create.do"><spring:message
								code="master.page.incidence.create" /></a></li>


				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.course" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="course/recycler/listCoursesAvailables.do?d-16544-p=1"><spring:message
								code="master.page.course.listCoursesAvailables" /></a></li>
					<li><a
						href="course/recycler/listOfCoursesThatIAttend.do?d-16544-p=1"><spring:message
								code="master.page.course.listOfCoursesThatIAttend" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a
						href="opinion/actor/myListOpinionCourse.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionCourse" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableCourse.do"><spring:message
								code="master.page.opinion.createOpinableCourse" /></a></li>
				</ul>
			</li>

			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>
				
				<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>
				
		</security:authorize>

		<security:authorize access="hasRole('CARRIER')">
			<li><a class="fNiv"><spring:message
						code="master.page.buyer.carrier1" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/carrier/edit.do"><spring:message
								code="master.page.buyer.edit" /></a></li>
					<li><a href="profile/carrier/display.do"><spring:message
								code="master.page.buyer.profile" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/carrier/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/carrier/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/carrier/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.request" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="request/carrier/listMyRequest.do?d-16544-p=1"><spring:message
								code="master.page.request.listMyRequest" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.assessment" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="assessment/carrier/listMyAssessment.do?d-16544-p=1"><spring:message
								code="master.page.assessment.listMyAssessment" /></a></li>
				</ul></li>
				
				<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
				</ul>
			</li>

			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>

				<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message
						code="master.page.buyer.manager1" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/manager/edit.do"><spring:message
								code="master.page.buyer.edit" /></a></li>
					<li><a href="profile/manager/display.do"><spring:message
								code="master.page.buyer.profile" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/manager/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/manager/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/manager/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message
						code="master.page.request" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="request/manager/listItemsNonRequest.do?d-16544-p=1"><spring:message
								code="master.page.request.listItemsNonRequest" /></a></li>
					<li><a href="request/manager/listMyRequest.do?d-16544-p=1"><spring:message
								code="master.page.request.listMyRequest" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message code="master.page.label" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="labelMaterial/manager/list.do?d-16544-p=1"><spring:message
								code="master.page.labelMaterial.list" /></a></li>
					<li><a href="labelProduct/manager/list.do?d-16544-p=1"><spring:message
								code="master.page.labelProduct.list" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message
						code="master.page.incidence" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="incidence/manager/list.do?d-16544-p=1"><spring:message
								code="master.page.incidences.listNoResolved" /></a></li>
					<li><a href="incidence/manager/listResolved.do?d-16544-p=1"><spring:message
								code="master.page.incidences.listResolved" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
				</ul>
			</li>

			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>

		</security:authorize>

		<security:authorize access="hasRole('EDITOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.buyer.editor1" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="profile/editor/edit.do"><spring:message
								code="master.page.buyer.edit" /></a></li>
					<li><a href="profile/editor/display.do"><spring:message
								code="master.page.buyer.profile" /></a></li>
				</ul></li>

			<li><a class="fNiv"><spring:message
						code="master.page.messageFolder" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="messageFolder/editor/list.do?d-16544-p=1"><spring:message
								code="master.page.messageFolder.myList" /></a></li>
					<li><a href="messageFolder/editor/create.do"><spring:message
								code="master.page.messageFolder.create" /></a></li>
				</ul></li>

			<li><a href="message/editor/send.do"><spring:message
						code="master.page.message.send" /></a></li>

			<li><a class="fNiv"><spring:message code="master.page.new" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="newscast/editor/list.do?d-16544-p=1"><spring:message
								code="master.page.New.list" /></a></li>
					<li><a href="newscast/editor/create.do"><spring:message
								code="master.page.New.create" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.opinion" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="opinion/actor/myListOpinionItem.do?d-16544-p=1"><spring:message
								code="master.page.opinion.myListOpinionProduct" /></a></li>
					<li><a href="opinion/actor/createOpinableItem.do"><spring:message
								code="master.page.opinion.createOpinableProduct" /></a></li>
				</ul>
			</li>

			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>
				
				<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<!-- REGISTRO DE USUARIO -->
			<li><a class="fNiv"><spring:message
						code="master.page.register" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="recycler/create.do"><spring:message
								code="master.page.recycler.register" /></a></li>
					<li><a href="buyer/create.do"><spring:message
								code="master.page.buyer.register" /></a></li>

				</ul></li>
			<li><a class="fNiv"><spring:message code="master.page.new" /></a>
				<ul>
					<li class="arrow"></li>

					<li><a href="newscast/list.do?d-16544-p=1"><spring:message
								code="master.page.New.list" /></a></li>
				</ul></li>
			<li><a class="fNiv"><spring:message
						code="master.page.admin.material" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="material/list.do?d-16544-p=1"><spring:message
								code="master.page.admin.material.list" /></a></li>
				</ul></li>
				
			<li><a class="fNiv"><spring:message	code="master.page.recycler" /></a>
				<ul>
					<li class="arrow"></li>
					
					<li><a href="recycler/list.do?d-16544-p=1"><spring:message code="master.page.recycler.list" /></a></li>
				</ul>
			</li>
			
			<li><a class="fNiv"><spring:message
						code="master.page.course" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a
						href="course/list.do?d-16544-p=1"><spring:message
								code="master.page.course.listAll" /></a></li>
				</ul></li>
				
			
			<li><a class="fNiv"><spring:message
						code="master.page.aboutUs" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="welcome/aboutUs.do"><spring:message
								code="master.page.aboutUs" /></a></li>
				</ul></li>
			<li><a class="fNiv"><spring:message
						code="master.page.administrator.legislation" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="legislation/list.do"><spring:message
								code="master.page.administrator.legislation" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>

					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>
		</security:authorize>
	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>
<br>
<font size="6" color="green" face="Georgia"><B><jstl:out
			value="${Name}"></jstl:out></B></font>

<spring:message code="master.page.new" var="newLanguage" />
<jstl:choose>
	<jstl:when test="${newLanguage == 'Newscasts'}">
		<br>
		<br>
		<i><jstl:out value="${EnglishWelcomeMessage}"></jstl:out></i>
	</jstl:when>

	<jstl:when test="${newLanguage == 'Noticias'}">
		<br>
		<br>
		<i><jstl:out value="${SpanishWelcomeMessage}"></jstl:out></i>
	</jstl:when>
</jstl:choose>