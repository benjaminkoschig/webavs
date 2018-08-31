<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT4001"/>
<c:set var="labelTitreEcran" value="JSP_ANNONCES_A_MYPRODIS"/>

<%-- visibiltés des boutons --%>
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

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">

globazGlobal.processLaunched = ${processLaunched};
globazGlobal.genreAnnonceTheoriquesAnnuel = '${viewBean.genreAnnonceTheoriquesAnnuel}';
globazGlobal.genreAnnonceTheoriquesMensuel = '${viewBean.genreAnnonceTheoriquesMensuel}';
globazGlobal.genreAnnonceCotisants = '${viewBean.genreAnnonceCotisants}';
globazGlobal.genreCP = '${viewBean.genreCP}';
globazGlobal.messageAnneeObligatoire = '${viewBean.messageAnneeObligatoire}';
globazGlobal.messageMoisObligatoire = '${viewBean.messageMoisObligatoire}';

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
	if(!globazGlobal.processLaunched) {
		$('input').removeProp('disabled');
		$('select').removeProp('disabled');
	}
}

$(function () {
	$('#annoncerSalairesTheoriquesAnnuel').click(function () {
		if($('#anneeAnnuel').val().length !== 4) {
			showErrorDialog(globazGlobal.messageAnneeObligatoire);
			return;
		}
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.decomptesalaire.annoncerMyProdis.executer";
		document.forms[0].elements('genreAnnonce').value=globazGlobal.genreAnnonceTheoriquesAnnuel;
		document.forms[0].submit();
	});
	$('#annoncerSalairesTheoriquesMensuel').click(function () {
		if($('#anneeMensuel').val().length !== 4) {
			showErrorDialog(globazGlobal.messageAnneeObligatoire);
			return;
		}
		if($('#moisMensuel').val().length == 0 || $('#moisMensuel').val().length > 2) {
			showErrorDialog(globazGlobal.messageMoisObligatoire);
			return;
		}
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.decomptesalaire.annoncerMyProdis.executer";
		document.forms[0].elements('genreAnnonce').value=globazGlobal.genreAnnonceTheoriquesMensuel;
		document.forms[0].submit();
	});
	$('#annoncerSalairesCotisants').click(function () {
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.decomptesalaire.annoncerMyProdis.executer";
		document.forms[0].elements('genreAnnonce').value=globazGlobal.genreAnnonceCotisants;
		document.forms[0].submit();
	});
	$('#annoncerCP').click(function() {
		$(this).prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.decomptesalaire.annoncerMyProdis.executer";
		document.forms[0].elements('genreAnnonce').value=globazGlobal.genreCP;
		document.forms[0].submit();
	});
})
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
			<!-- <tr>
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr> -->
			<c:if test="${not processLaunched}">
			<tr>
				<td id="annoncerSalairesTheoriquesAnnuel" colspan="2"><input style="width:100%"  type="button" value="<ct:FWLabel key="JSP_ANNONCER_SALAIRES_THEORIQUES_ANNUEL"/>" /></td>
				<td style="text-align: right;"><input style="width : 80px;" id="anneeAnnuel" type="text" name="anneeAnnuel" value="${viewBean.anneeAnnuel}" data-g-integer="" placeholder='<ct:FWLabel key="JSP_ANNEE" />'></td>
			</tr>
			<tr>
				<td id="annoncerSalairesTheoriquesMensuel" colspan="2"><input style="width:100%"  type="button" value="<ct:FWLabel key="JSP_ANNONCER_SALAIRES_THEORIQUES_MENSUEL"/>" /></td>
				<td style="text-align: right;"><input style="width : 80px;" id="anneeMensuel" type="text" name="anneeMensuel" value="${viewBean.anneeMensuel}" data-g-integer="" placeholder='<ct:FWLabel key="JSP_ANNEE" />'></td>
				<td style="text-align: right;"><input style="width : 60px;" id="moisMensuel" type="text" name="moisMensuel" value="${viewBean.moisMensuel}" data-g-integer="" placeholder='<ct:FWLabel key="JSP_MOIS" />'></td>
			</tr>
			<tr>
				<td id="annoncerSalairesCotisants" colspan="2"><input style="width:100%" type="button" value='<ct:FWLabel key="JSP_ANNONCER_SALAIRES_COTISANTS"/>' /></td>
			</tr>
			<tr>
				<td id="annoncerCP" colspan="2"><input style="width:100%" type="button" value='<ct:FWLabel key="JSP_ANNONCER_CP"/>' /></td>
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
<input name="genreAnnonce" type="hidden" />

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>