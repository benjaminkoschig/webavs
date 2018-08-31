<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Param�trage global de la page ************************************************************** --%>
<%-- labels n� ecran et titre --%>
<c:set var="idEcran" value="PPT2004"/>
<c:set var="labelTitreEcran" value="JSP_LISTES_COMMUNICATIONSALAIRES_RESOR"/>

<%-- visibilt�s des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionListe" value="vulpecula.listes" />
<ct:checkRight var="hasCreateRightOnListes" element="${userActionListe}" crud="c" />


<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre � la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases � red�finir
var messageConventionRequise = '${viewBean.messageConventionRequise}';
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

function init(){
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {
$("#launchListe").click(function () {
		$("#launchListe").prop('disabled', true);
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.listes.communicationsalairesresor.executer";
			document.forms[0].submit();
		} else {
			$("#launchListe").removeAttr('disabled');
		}
	});
});

globazGlobal.checkFields = function () {
	return true;
};
</script>
<style type="text/css">
	#mainWrapper {
		height: auto !important;
	}
</style>


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
			<tr>
				<td>
					<label><ct:FWLabel key='JSP_ANNEE'/></label>
				</td>
				<td>		
					<input id="annee" name="annee" value="${viewBean.annee}" />
				</td>
			</tr>		
			<tr>
				<td><label for="typeListe"><ct:FWLabel key="JSP_LISTE"/></label></td>
				<td>
					<ct:FWCodeSelectTag name="typeListe" codeType="TYCOMMSAL" defaut="${viewBean.typeListe}" />
				</td>
			</tr>
				<c:if test="${not processLaunched}">
				<tr>
					<td style="text-align: center;" colspan="2">
						<br />
						<input id="launchListe" type="button" name="launchListe" value='<ct:FWLabel key="JSP_LANCER"/>'/>
					</td>
				</tr>
				</c:if>
		
			
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