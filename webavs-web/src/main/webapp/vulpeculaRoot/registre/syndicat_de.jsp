<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1004"/>
<c:set var="labelTitreEcran" value="JSP_SYNDICATS"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/registre/syndicatPart.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>


<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	globazGlobal.idSyndicat = '${viewBean.idSyndicat}';
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
					<td width ="20%"><ct:FWLabel key='JSP_CODE_ADMINISTRATION'/></td>
					<td><input type="text" id="searchModel.forCodeAdministrationLike"></input></td>
				</tr>
				<tr>
					<td width ="20%" class="notSortable"><ct:FWLabel key='JSP_DESIGNATION'/></td>
					<td><input type="text" id="searchModel.forDesignation1Like"></input></td>
				</tr>
			</table>
		</div>		
		<div align="right"  style="padding-right:80px;padding-top:10px;padding-bottom:10px;">
			<input type="button" name="" value="Rechercher">
		</div> 	
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th class="notSortable"><ct:FWLabel key='JSP_CODE_ADMINISTRATION'/></th>
						<th class="notSortable"><ct:FWLabel key='JSP_DESIGNATION'/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="areaDetail">
			<div class="detail">
				<div style="float: right;">
				<c:if test="${bButtonNew}">
					<input class="btnCtrl" type="button" id="btnNouveau" value="${btnNewLabel}"  />
				</c:if>					
				</div>
				<p class="line"><a href="#" id="administrationLink" class="external_link"><span id="currentEntity.admin.codeAdministration"></span> - <span id="currentEntity.tiers.designation1"></span></a></p>
				<p class="line">
					<pre>
<span id="adresse"></span>
					</pre>
				</p>
			</div>
			<input id="currentEntity.admin.idTiersAdministration" type="hidden" value="" />
			
			<div class="areaParametres" style="margin-bottom: 24px;">
				<div class="areaSearchParametres">
				
				</div>
				<table class="areaTableParametres">
					<thead>
						<tr>
							<th class="notSortable"><ct:FWLabel key="JSP_ID"/></th>
							<th style="width: 20%" class="notSortable"><ct:FWLabel key="JSP_CAISSE_METIER"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_POURCENTAGE"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_MONTANT_PAR_TRAVAILLEUR"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_DATE_DEBUT"/></th>
							<th class="notSortable"><ct:FWLabel key="JSP_DATE_FIN"/></th>
						</tr>
						<tr>
							<th class="notSortable"></th>
							<th class="notSortable">
								<select id="caisseMetier">
									<option selected="selected" value=""></option>
									<c:forEach var="caisseMetier" items="${viewBean.caissesMetiers}">
										<option value="${caisseMetier.id}">${caisseMetier.designation1}</option>
									</c:forEach>
								</select>
							</th>
							<th class="notSortable"></th>
							<th class="notSortable"></th>
							<th class="notSortable"></th>
							<th class="notSortable"></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />