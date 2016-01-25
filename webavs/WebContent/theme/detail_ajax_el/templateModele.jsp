<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="???????" />
<c:set var="labelTitreEcran" value="" />

<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />

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
			</div>
			<div>
				<table class="areaTable" width="100%">
					<thead>
					</thead>
					<tbody>
						<%-- Rempli dynamiquement ! --%>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
				</div>
				<div class="buttons">
					<%@ include file="/theme/detail_ajax_el/capageButtons.jspf"%>
				</div>
			</div>
		</div>
	</td>
</tr>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>