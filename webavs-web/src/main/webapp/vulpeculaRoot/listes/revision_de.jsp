<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2011"/>
<c:set var="labelTitreEcran" value="JSP_LISTES_REVISION"/>

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
var messageEmployeurManquant = '${viewBean.messageEmployeurManquant}';
var messageAnneeObligatoire = '${viewBean.messageAnneeObligatoire}';
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
	$('#widgetEmployeur').val("");
	$('#idEmployeur').val("");
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {	
	$("#launchListeRevision").click(function () {
		$("#launchListeRevision").prop('disabled', true);
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.listes.revision.executer";
			document.forms[0].submit();
		} else {
			$("#launchListeRevision").removeAttr('disabled');
		}

	});
});

globazGlobal.checkFields = function () {
	if($("#idEmployeur").val().length <= 0){
		showErrorDialog(messageEmployeurManquant);
		return false;
	}
	
	var mainForm = $('form[name="mainForm"]').serializeFormJSON();

	if(mainForm.anneeDebut==0 || mainForm.anneeDebut>mainForm.anneeFin){
		showErrorDialog(messageAnneeObligatoire);
		return false;
	}

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
			<tr>
				<td><label for="employeur"><ct:FWLabel key="JSP_EMPLOYEUR"/></label></td>
				<td>
					<input id="idEmployeur" name="idEmployeur" type="hidden" />
					<ct:widget id='widgetEmployeur'
						style="width:400px;"
		   				name = 'designationEmployeur'
		   				styleClass="widgetAdmin"
		   				notation="data-g-string='mandatory:false' value='${viewBean.designationEmployeur}'">
						<ct:widgetService defaultLaunchSize="1" methodName="search" className="${viewBean.employeurViewService}">
						<ct:widgetCriteria criteria="likeNumeroAffilie" label="JSP_NO_AFFILIE"/>
						<ct:widgetCriteria criteria="likeDesignationTiersUpper" label="JSP_RAISON_SOCIALE"/>
						<ct:widgetLineFormatter format="#{affiliationTiersComplexModel.affiliation.affilieNumero} - #{affiliationTiersComplexModel.affiliation.raisonSociale}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									this.value=$(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero')+', '+$(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
									$('#idEmployeur').val($(element).attr('affiliationTiersComplexModel.affiliation.affiliationId'));
								}
							</script>
					</ct:widgetJSReturnFunction>
					</ct:widgetService>
					</ct:widget>
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
			<td><ct:FWLabel key="JSP_LISTES_REVISION_AVEC_LETTRE"/></td>
			<td>
				<input name="printLettre" type="checkbox" checked="check">
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
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launchListeRevision" type="button" name="launchListeRevision" value='<ct:FWLabel key="JSP_LANCER"/>'/>
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