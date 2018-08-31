<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2014"/>
<c:set var="labelTitreEcran" value="JSP_LISTE_NON_CONTROLE_4_ANS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="tableHeight" value="auto" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir
var messageProcessusLance = '${viewBean.messageProcessusLance}';

function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init() {
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {	
	$("#launchListeNonControle").click(function () {
		$("#launchListeNonControle").prop('disabled', true);
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.listes.nonControle.executer";
			document.forms[0].submit();
		} else {
			$("#launchListeNonControle").removeAttr('disabled');
		}

	});
});

globazGlobal.checkFields = function () {
	return true;
};
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="position: absolute; right:0px; width: 30%">
</div>
<div style="width: 100%;text-align: center;">
	<div>
		<table>
			<tr>
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>	
				<td><ct:FWLabel key="JSP_PERIODE"/></td>
				<td>
					<input name="dateDebut" value="${viewBean.dateDefaut}" data-g-calendar="mandatory:true" />
				</td>	
			<tr>
			<tr>
				<td><ct:FWLabel key="JSP_NON_CONTROLE_DEPUIS"/></td>
				<td><input name="nombreAnnee" value="${viewBean.nombreAnneeDefaut}" data-g-integer="mandatory:true" /></td>
				<td><ct:FWLabel key="JSP_NON_CONTROLE_0"/></td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_UNIQUEMENT_AVS"/></td>
				<td><input name="uniquementAVS"  id="uniquementAVS" type="checkbox"/></td>
			</tr>	
			</tr>
							
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launchListeNonControle" type="button" name="launchListeNonControle" value='<ct:FWLabel key="JSP_LANCER"/>'/>
				</td>
			</tr>

		</table>
	</div>
</div>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED"/>
	</div>
</c:if>


<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>