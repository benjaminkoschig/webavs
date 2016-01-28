<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1101"/>
<c:set var="labelTitreEcran" value="JSP_TRAVAILLEURS"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${rootPath}/postetravail/travailleur_de.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>


<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	<c:set var="userActionNew" value="vulpecula.postetravaildetail.travailleurdetail.afficher" scope="page" />
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>

<c:if test="${viewBean.protege}">
	<tr>
		<td><span style="color:red">${viewBean.protegeLibelle}</span></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</c:if>

<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
				<table width="100%">
					<tr><td width ="20%"><ct:FWLabel key='JSP_NUMERO'/></td><td><input type="text" value="${viewBean.searchModel.forIdTravailleur}" id="searchModel.forIdTravailleur"></input></td></tr>
					<tr><td width ="20%"><ct:FWLabel key='JSP_NOM'/></td><td><input type="text" value="${viewBean.searchModel.likeNom}" id="searchModel.likeNom"></input></td></tr>
					<tr><td width ="20%"><ct:FWLabel key='JSP_PRENOM'/></td><td><input type="text" value="${viewBean.searchModel.likePrenom}" id="searchModel.likePrenom"></input></td></tr>
					<tr><td width ="20%"><ct:FWLabel key='JSP_NSS'/></td><td><input type="text" value="${viewBean.searchModel.forNumAvs}" id="searchModel.forNumAvs"></input></td></tr>
					<tr><td width ="20%"><ct:FWLabel key='JSP_DATE_NAISSANCE'/></td><td><input type="text" value="${viewBean.searchModel.forDateNaissance}" id="searchModel.forDateNaissance" ></input></td></tr>
				</table>													
		</div>
	
		<%@ include file="/theme/find_ajax_el/searchNewButtons.jspf" %> 
		
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th></th>
						<th data-orderKey="id"><ct:FWLabel key='JSP_NUMERO'/></th>
						<th style="text-align: left;" data-orderKey="nom"><ct:FWLabel key='JSP_NOM'/></th>
						<th style="text-align: left;" data-orderKey="prenom"><ct:FWLabel key='JSP_PRENOM'/></th>
						<th data-orderKey="nss"><ct:FWLabel key='JSP_NSS'/></th>
						<th data-orderKey="dateNaissance"><ct:FWLabel key='JSP_DATE_NAISSANCE'/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>