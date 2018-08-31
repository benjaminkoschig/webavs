<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT8001"/>
<c:set var="labelTitreEcran" value="JSP_NOUVEAU_TRAVAILLEURS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${rootPath}/ebusiness/nouveauTravailleur_de.js"></script>
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


<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
				<table width="100%">
					<tr>
						<td width ="20%"><ct:FWLabel key='JSP_NUMERO'/></td><td><input type="text" value="${viewBean.searchModel.forIdTravailleur}" id="searchModel.forIdTravailleur"></input></td>
						<td width ="20%"><ct:FWLabel key='JSP_CONVENTION'/></td>
						<td>
							<select id="searchModel.forConvention">
								<option value=""></option>
									<c:forEach var="convention" items="${viewBean.conventions}">
										<option value="${convention.code}">${convention.designation}</option>
									</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td width ="20%"><ct:FWLabel key='JSP_NOM'/></td><td><input type="text" value="${viewBean.searchModel.likeNom}" id="searchModel.likeNom"></input></td>
						<td width ="20%">Status de l'annonce</td>
						<td><ct:FWCodeSelectTag name="searchModel.forStatus" codeType="EBUSTAN" defaut="511110" wantBlank="true"/></td>						
					</tr>
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
<!-- 						<th></th> -->
						<th data-orderKey="id"><ct:FWLabel key='JSP_NUMERO'/></th>
						<th style="text-align: left;" data-orderKey="nom"><ct:FWLabel key='JSP_NOM'/></th>
						<th style="text-align: left;" data-orderKey="prenom"><ct:FWLabel key='JSP_PRENOM'/></th>
						<th data-orderKey="nss"><ct:FWLabel key='JSP_NSS'/></th>
						<th class="notSortable"><ct:FWLabel key='JSP_DATE_NAISSANCE'/></th>
						<th><ct:FWLabel key='JSP_EMPLOYEUR_NO_AFFILIATION'/></th>
						<th><ct:FWLabel key='JSP_RAISON_SOCIALE'/></th>
						<th><ct:FWLabel key='JSP_EXISTE_DANS_WM'/></th>
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