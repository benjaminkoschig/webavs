<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@page import="globaz.vulpecula.vb.ctrlemployeur.PTLettreControleViewBean"%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Param?trage global de la page ************************************************************** --%>
<%-- labels n? ecran et titre --%>
<c:set var="idEcran" value="PPT1116"/>
<c:set var="labelTitreEcran" value="JSP_LETTRE_CONTROLE_EMPLOYEUR"/>

<%-- visibilt?s des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page" />
<c:set var="bButtonUpdate" value="false" scope="page" />


<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre ? la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases ? red?finir
var messageEmployeurRequis = '${viewBean.messageEmployeurRequis}';
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
			document.forms[0].elements('userAction').value="vulpecula.ctrlemployeur.lettreControle.executer";
			document.forms[0].submit();
		} else {
			$("#launchListe").removeAttr('disabled');
		}
	});
	setTimeout(function() {
		$('#descriptionEmployeur').focus();
	});
});

globazGlobal.checkFields = function () {
	var mainForm = $('form[name="mainForm"]').serializeFormJSON();
	
	function isEmployeurSaisie(mainForm) {
		if(mainForm.idEmployeur.length>0){
			return true;
		}else{
			showErrorDialog(messageEmployeurRequis);
			return false;
		}
	}
	
	if(isEmployeurSaisie(mainForm)){
		return true;	
	}else{
		return false;
	} 
}
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
				<td><label for="idEmployeur"><ct:FWLabel key='JSP_EMPLOYEUR'/></label></td>
				<td>
				<input id="idEmployeur" name="idEmployeur" type="hidden" />
						<input id="descriptionEmployeur" class="jadeAutocompleteAjax" name="employeurNumero" type="text" 
							data-g-autocomplete="
								service:?${viewBean.employeurService}?,
								method:?search?,
								criterias:?{'likeNumeroAffilie': 'No affili?', 'likeDesignationTiersUpper': 'designation'}?,
								lineFormatter:?#{affiliationTiersComplexModel.affiliation.affilieNumero} #{affiliationTiersComplexModel.affiliation.raisonSociale}?,
								modelReturnVariables:?affiliationTiersComplexModel.affiliation.affiliationId,affiliationTiersComplexModel.affiliation.affilieNumero,affiliationTiersComplexModel.affiliation.raisonSociale?,
								functionReturn:?
									function(element) {
										var idAffilie = $(element).attr('affiliationTiersComplexModel.affiliation.affiliationId');
										var raisonSociale = $(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
										var affilieNumero = $(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero');
										
										$('#idEmployeur').val(idAffilie).change();
										this.value= affilieNumero+','+raisonSociale;
								}
							?,
							nbOfCharBeforeLaunch:?3?,
							mandatory:true"
						/>
				</td>
			</tr>
			<tr>
				<td><label for="dateReference"><ct:FWLabel key="JSP_DATE_DE_REFERENCE"/></label></td>
				<td>
					<input name="dateReference" value="${viewBean.dateReference}" data-g-calendar="" />
				</td>	
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_ANNEE"/></td>
				<td>
					<input name="anneeDebut" value="${viewBean.anneeDebut}" data-g-integer="mandatory:true, sizeMax:5, autoFit:true" />
					<ct:FWLabel key="JSP_AU"/>&nbsp;
					<input name="anneeFin" value="${viewBean.anneeFin}" data-g-integer="mandatory:true, sizeMax:5, autoFit:true" />
				</td>
			</tr>	
			<tr>
				<td><ct:FWLabel key="JSP_LISTES_REVISION_REVISEUR"/></td>
				<td>
					<input name="reviseur" value="${viewBean.reviseur}"  />
				</td>
			</tr>
			<tr>
				<td><label for="date"><ct:FWLabel key="JSP_DATE_CONTROLE"/></label></td>
				<td>
					<input name="date" value="${viewBean.date}" data-g-calendar="" />
				</td>	
				<td><label for="heure"><ct:FWLabel key="JSP_HEURE_CONTROLE"/></label></td>
				<td>
					<input name="heure" value="${viewBean.heure}"  />
				</td>			
			</tr>
			<tr>
				<td style="text-align: center;" colspan="4">
					<br />
					<input id="launchListe" type="button" name="launchListe" value='<ct:FWLabel key="JSP_LANCER"/>'/>
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

<%-- <ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" /> --%>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>