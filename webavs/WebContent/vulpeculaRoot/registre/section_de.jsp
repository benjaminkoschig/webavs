<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1005"/>
<c:set var="labelTitreEcran" value="JSP_SECTIONS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/registre/sectionPart.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>


<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_CODE_ADMINISTRATION'/></td><td><input type="text" id="searchModel.forCodeAdministrationLike"></input> </td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_DESIGNATION'/></td><td><input type="text" id="searchModel.forDesignation1Like"></input></td></tr>				
			</table>		
		</div>
		<div align="right"  style="padding-right:80px;padding-top:10px;padding-bottom:10px;">
			<input type="button" name="" value="Rechercher">
		</div> 	
		
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th data-orderKey="codeAdministrationLike"><ct:FWLabel key='JSP_CODE_ADMINISTRATION'/></th>
						<th data-orderKey="designation1"><ct:FWLabel key='JSP_DESIGNATION'/></th>
					</tr>				
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="areaDetail">
			<div class="detail">
				<p class="line"><a href="#" id="administrationLink" class="external_link"><span id="currentEntity.admin.codeAdministration"></span> - <span id="currentEntity.tiers.designation1"></span></a></p>
				<p class="line">
					<pre>
<span id="adresse"></span>
					</pre>
				</p>
			</div>
		</div>		
	</div>
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />