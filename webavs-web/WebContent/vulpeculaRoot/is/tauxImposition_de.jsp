<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1013" />
<c:set var="labelTitreEcran" value="JSP_TAUX_IMPOSITION" />

<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="${rootPath}/is/tauxImposition_de.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/detail_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/detail_ajax_el/bodyStart2.jspf"%>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
			<table>
				<tr>
					<td><ct:FWLabel key="JSP_CANTON"/></td>
					<td>
						<ct:FWCodeSelectTag name="searchModel.forCanton" codeType="ALCANTON" defaut="" wantBlank="true"/>
					</td>
					<td><ct:FWLabel key="JSP_TYPE_IMPOSITION"/></td>
					<td><ct:FWCodeSelectTag name="searchModel.forTypeImposition" codeType="PTTYPIMP" defaut="" wantBlank="true"/></td>
				</tr>
			</table>
			</div>
			<div>
				<table style="width:100%" class="areaTable">
					<thead>
						<tr>
							<td><ct:FWLabel key="JSP_CANTON"/></td>
							<td><ct:FWLabel key="JSP_TYPE_IMPOSITION"/></td>
							<td><ct:FWLabel key="JSP_TAUX_IMPOSITION"/></td>
							<td><ct:FWLabel key="JSP_DATE_DEBUT"/></td>
							<td><ct:FWLabel key="JSP_DATE_FIN"/></td>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
					<table>
						<tr>
							<td><ct:FWLabel key="JSP_PERIODE_DE"/></td>
							<td><input id="currentEntity.periodeDebut" data-g-calendar="" /></td>
							<td><ct:FWLabel key="JSP_PERIODE_A"/></td>
							<td><input id="currentEntity.periodeFin" data-g-calendar="" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_CANTON_IMPOSITION"/></td>
							<td><ct:FWCodeSelectTag name="currentEntity.canton" codeType="PYCANTON" defaut="" wantBlank="true" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_TYPE_IMPOSITION"/></td>
							<td><ct:FWCodeSelectTag name="currentEntity.typeImposition" codeType="PTTYPIMP" defaut="" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_TAUX"/></td>
							<td><input id="currentEntity.taux" name="currentEntity.taux" data-g-rate="mandatory:false" >%</td>
						</tr>
					</table>
				</div>
				<div class="buttons">
					<%@ include file="/theme/detail_ajax_el/capageButtons.jspf"%>
				</div>
			</div>
		</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>