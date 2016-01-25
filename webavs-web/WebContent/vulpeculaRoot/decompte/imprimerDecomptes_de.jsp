<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1114"/>
<c:set var="labelTitreEcran" value="JSP_IMPRESSION_DECOMPTES"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/decompte/imprimerDecomptes_de.js"></script>


<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	globazGlobal.decompteViewService = '${viewBean.decompteViewService}';
	globazGlobal.messagePasDeDocumentSelectionne = '${viewBean.messagePasDeDocumentSelectionne}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
			<table>
				<tr style="display: none;">
					<td><ct:FWLabel key="JSP_EMAIL"/></td>
					<td><input id="email" name="email" /></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_ID_DECOMPTE"/></td>
					<td><input id="idDecompte" name="idDecompte" /></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_NO_DECOMPTE"/></td>
					<td><input id="numeroDecompte" name="numeroDecompte" /></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_NO_AFFILIE"/></td>
					<td>
						<input id="noAffilie" name="noAffilie" />
					</td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_PASSAGE_FACTURATION"/></td>
					<td><input id="idPassage" name="idPassage" /></td>
				</tr>
				<tr>
					<td><ct:FWLabel key="JSP_ETAT"/></td>
					<td>
						<select id="etatDecompte" name="etatDecompte">
						<option value=""></option>
						<option value="${viewBean.etatsEnSuspens}" selected>En suspens</option>
						<c:forEach var="etat" items="${viewBean.etatDecomptes}">
							<option value="${etat.id}">${etat.libelle}</option>
						</c:forEach>
						</select>
					</td>
				</tr>
			</table>													
		</div>		
		<%@ include file="/theme/find_ajax_el/searchNewButtons.jspf" %> 
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th class="notSortable"><ct:FWLabel key="JSP_ID"/></th>
						<th class="notSortable"><ct:FWLabel key="JSP_NO_DECOMPTE"/></th>
						<th class="notSortable"><ct:FWLabel key="JSP_NO_AFFILIE"/></th>
						<th class="notSortable"><ct:FWLabel key="JSP_TYPE"/></th>
						<th class="notSortable"><ct:FWLabel key="JSP_PASSAGE_FACTURATION"/></th>
						<th class="notSortable"><ct:FWLabel key="JSP_ETAT"/></th>
						<th class="notSortable"></th>
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