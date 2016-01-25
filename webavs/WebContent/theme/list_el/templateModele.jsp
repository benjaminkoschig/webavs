<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/list_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<c:set var="detailLink" value="xx?userAction=xx.yyyyy.afficher&selectedId=" />
<c:set var="menuName" value="xxxxx" />
<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/list_el/javascripts.jspf"%>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir

function onCustomLoad() {
	
}

//chargement du dom jquery
$(function () {
	
});
</script>

<%--  *************************************************************** Corps de la page *********************************************************************** --%>
<%@ include file="/theme/list_el/tableHeader.jspf"%>

<tr>
	<th><ct:FWLabel key="HEADER001" /></th>
	<th><ct:FWLabel key="HEADER002" /></th>
	<th><ct:FWLabel key="HEADER003" /></th>
</tr>
<c:forEach var="entity" items="${viewBean.entities}" varStatus="status">
	<%--Définition d'une variable "rowStyle" --%>
	<%@ include file="/theme/list_el/lineStyle.jspf"%>
	<tr class="${rowStyle}" onMouseOver="jscss('swap', this, '${rowStyle}', 'rowHighligthed')" onMouseOut="jscss('swap', this, 'rowHighligthed', '${rowStyle}')">
		<c:set var="actionDetail" value="${targetLocation}='${detailLink}${entity.id}'" />
		<td class="mtd">
			<ct:menuPopup menu="${menuName}" target="top.fr_main">
			</ct:menuPopup>
		</td>
		<td class="center" onClick="${actionDetail}">${entity.id}</td>	
	</tr>
</c:forEach>

<%@ include file="/theme/list_el/tableFooter.jspf"%>