<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@page import="globaz.vulpecula.vb.listes.PTSalairesAVSViewBean"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCodeManager"%>
<%@page import="globaz.globall.parameters.FWParametersSystemCode"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="globaz.jade.client.util.JadeCodesSystemsUtil"%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2003"/>
<c:set var="labelTitreEcran" value="JSP_LISTES_DECOMPTE_AVS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />


<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir
var messageTypeDecompteRequis = '${viewBean.messageTypeDecompteRequis}';
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
	$("#selectTypeDecompte").multiselect("enable");
	$('#descriptionEmployeur').focus();
}

$(function () {
$("#launchListe").click(function () {
		$("#launchListe").prop('disabled', true);
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.listes.salairesAVS.executer";
			document.forms[0].submit();
		} else {
			$("#launchListe").removeAttr('disabled');
		}
	});
});

globazGlobal.checkFields = function () {
	var mainForm = $('form[name="mainForm"]').serializeFormJSON();
	
	function isDecompteSaisie(mainForm) {
		if(mainForm.filterDecompte.length>0){
			return true;
		}else{
			showErrorDialog(messageTypeDecompteRequis);
			return false;
		}
	}
	function isEmployeurSaisie(mainForm) {
		if(mainForm.idEmployeur.length>0){
			return true;
		}else{
			showErrorDialog(messageEmployeurRequis);
			return false;
		}
	}
	
	if(isDecompteSaisie(mainForm)&&isEmployeurSaisie(mainForm)){
		return true;	
	}else{
		return false;
	} 
}

$(document).ready(function() {
	
	$("#selectTypeDecompte").multiselect({
		height: 120,
		minWidth : 220,
		noneSelectedText: "Aucun",
		checkAllText: "Tous",
		uncheckAllText: "Aucun",
		selectedList:1,
		selectedText: function(numChecked, numTotal, checkedItems){
	
			var csValueChecked = "";
			var csLabelCourtChecked ="";
			var csLabelLongChecked="";
			var nb = 0;
			$(checkedItems).each(function() {
				
				nb++;			
				var csValue = $(this).val();
				var labelCourtCs = $(this).next().html().substring(0,$(this).next().html().indexOf(':'));
				var labelLongCs = $(this).next().html();
			
				csValueChecked+=csValue;
				csLabelCourtChecked+=labelCourtCs;
				csLabelLongChecked+=labelLongCs;
				if (nb<numChecked) {
					csLabelCourtChecked +=", ";
					csLabelLongChecked+=", ";
					csValueChecked+=",";
				}
				
			});
			
			$('#filterDecompteId').val(csValueChecked);
			if(numChecked==numTotal){
				return "Tous";
			}
			if(nb==1){
				return csLabelLongChecked;
			}
			else{
				return csLabelCourtChecked;
			}
			
		},
		uncheckAll: function(){
			$('#filterDecompteId').val('');
		}, 
		click: function(event, ui){
			var csValueChecked = "";
			var nb = 0;
			$.each( $("#selectTypedecompte").multiselect("getChecked"), function(numChecked){
				nb++;
				var csValue = $(this).val().substring(0,$(this).val().indexOf('-'));
			
				csValueChecked+=csValue;
				if (nb<numChecked) {
					csValueChecked+=",";
				}
			});
			
			$('#filterDecompteId').val(csValueChecked);
		   }
		
	});
}); 
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
								service:¦${viewBean.employeurService}¦,
								method:¦search¦,
								criterias:¦{'likeNumeroAffilie': 'No affilié', 'likeDesignationTiersUpper': 'designation'}¦,
								lineFormatter:¦#{affiliationTiersComplexModel.affiliation.affilieNumero} #{affiliationTiersComplexModel.affiliation.raisonSociale}¦,
								modelReturnVariables:¦affiliationTiersComplexModel.affiliation.affiliationId,affiliationTiersComplexModel.affiliation.affilieNumero,affiliationTiersComplexModel.affiliation.raisonSociale¦,
								functionReturn:¦
									function(element) {
										var idAffilie = $(element).attr('affiliationTiersComplexModel.affiliation.affiliationId');
										var raisonSociale = $(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
										var affilieNumero = $(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero');
										
										$('#idEmployeur').val(idAffilie).change();
										this.value= affilieNumero+','+raisonSociale;
								}
							¦,
							nbOfCharBeforeLaunch:¦3¦,
							mandatory:true"
						/>
				</td>
			</tr>	
			<tr>
				<td><label for="annee"><ct:FWLabel key="JSP_ANNEE_COMPTABLE"/></label></td>
				<td>
					<c:choose>
						<c:when test="${empty viewBean.annee }">
							<input name="annee" value="${viewBean.currentYear}" data-g-integer="mandatory:true" />
						</c:when>
						<c:otherwise>
							<input name="annee" value="${viewBean.annee}" data-g-integer="mandatory:true" />
						</c:otherwise>
					</c:choose>
				</td>				
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_TYPE_DECOMPTE"/></td>
				<td>
					<select multiple id="selectTypeDecompte" >
					<c:forEach var="type" items="${viewBean.types}">
						<option selected="selected" value="${type}"><ct:FWCodeLibelle csCode="${type}"/></option>
					</c:forEach>
					</select>
					<input id="filterDecompteId" name="filterDecompte" type="hidden"/>
				</td>				
			</tr>
			<tr>
				<td style="text-align: center;" colspan="2">
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

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>