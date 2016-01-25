<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1010"/>
<c:set var="labelTitreEcran" value="JSP_QUALIFICATION"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/registre/qualificationPart.js"></script>

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>
<script type="text/jadeTemplate" id="lineTemplate">
	<tr statut=''>
		<td>
			<ct:FWCodeSelectTag name="qualification" codeType="PTQUALIFIC" defaut="" notation="class='qualification'" />
		</td>
		<td>
			<ct:FWCodeSelectTag name="typeQualification" codeType="PTOUVREMPL" defaut="" notation="class='typeQualification'" />
		</td>
		<td>
			<ct:FWCodeSelectTag name="personnel" codeType="PTPERSONNE" defaut="" notation="class='personnel'" />
		</td>	
	</tr>
</script>
<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_NUMERO'/><td><input type="text" id="searchModel.forCodeAdministrationLike"></input> </td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_DESIGNATION'/><td><input type="text" id="searchModel.forDesignation1Like"></input></td></tr>				
			</table>
		</div>		
		<div align="right"  style="padding-right:80px;padding-top:10px;padding-bottom:10px;">
			<input type="button" name="" value="Rechercher">
		</div>
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th data-orderKey="id"><ct:FWLabel key='JSP_NUMERO'/></th>
						<th data-orderKey="designation"><ct:FWLabel key='JSP_DESIGNATION'/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>

		<div class="areaDetail">
			<div class="detail">
				<p class="line"><label><ct:FWLabel key='JSP_NUMERO'/></label>
					<c:out value="${currentEntity.simpleConvention.idConvention}" />
					<span id="currentEntity.admin.idTiersAdministration" > </span>
				</p>
				<p class="line"><span id="currentEntity.simpleConvention.designation" ></span></p>
			</div>
			<div id="tabLiaisons">
				<ul>
					<li><a href="#areaLiaisonParametres"><ct:FWLabel key='JSP_QUALIFICATIONS'/></a></li>
				</ul>
				<div id="areaLiaisonParametre">
					<table id="tblParametre">
						<thead>
							<tr>
								<th><ct:FWLabel key='JSP_QUALIFICATIONS'/></th>
								<th><ct:FWLabel key='JSP_QUALIFICATIONS_OUVRIER'/></th>
								<th><ct:FWLabel key='JSP_QUALIFICATIONS_PERSONNEL'/></th>
							</tr>
						</thead>
						<tbody id="tableParametres"></tbody>
					</table>
				</div>
						<div class="buttons">
			<div class="btnAjax">
				<ct:ifhasright element="${partialUserAction}" crud="u">
					<input class="btnAjaxUpdate" type="button" value="${btnUpdLabel}">
				</ct:ifhasright>
				<input class="btnAjaxValidate" type="button" value="${btnValLabel}">
				<input class="btnAjaxCancel" type="button" value="${btnCanLabel}">
			</div>
		</div>
			</div>
		</div> <!-- /areaDetail -->
	</div>
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />