<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PT1111"/>
<c:set var="labelTitreEcran" value="JSP_RECEPTION_DECOMPTE"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${rootPath}/decomptereception/decomptereceptionPart.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>

<script type="text/javascript">
globazGlobal.decompteService = '${viewBean.decompteService}';
globazGlobal.decompteMiseAJourLabel = '${viewBean.decompteMiseAJourMessage}';
globazGlobal.decompteNonExistant = '<c:out value="${viewBean.decompteNonExistant}" />';
globazGlobal.decompteDejaReceptionne = '<c:out value="${viewBean.decompteDejaReceptionne}" />'
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="float:right; width: 30%">
</div>

<div>
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_ID_DECOMPTE" /></td>
			<td colspan="2"><input id="idDecompte" name="idDecompte" type="text" />
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_EMPLOYEUR"/></td>
			<td colspan="2"><textarea id="employeur" class="readonly" cols="40" rows="3" readonly="readonly" disabled="disabled"></textarea></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_NO_DECOMPTE"/></td>
			<td><input id="noDecompte" class="readonly" type="text" readonly="readonly" disabled="disabled" /></td>
			<td><input id="description" style="width: 270px;" class="readonly" type="text" readonly="readonly" disabled="disabled" /></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_TYPE_DECOMPTE"/></td>
			<td colspan="2"><input id="typeDecompte" class="readonly" type="text" readonly="readonly" disabled="disabled"/></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_RECU_LE"/></td>
			<td><input id="recuLe" type="text" data-g-calendar="currentDate:true" /></td>
		</tr>
	</table>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>