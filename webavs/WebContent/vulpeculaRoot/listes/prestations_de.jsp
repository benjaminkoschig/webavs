<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2001"/>
<c:set var="labelTitreEcran" value="JSP_LISTES_PRESTATIONS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir
var messagePeriodeOuLotRequis = '${viewBean.messagePeriodeOuLotRequis}';
var messagePeriodeOuLot = '${viewBean.messagePeriodeOuLot}';
var messagePeriodeFinPlusGrandDebut = '${viewBean.messagePeriodeFinPlusGrandDebut}';
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

	function $getIdPassageFacturation() {
		return $('#idPassageFacturation');	
	}
	
	function $getPeriodeFin() {
		return $('#periodeFin');
	}
	
	function $getPeriodeDebut() {
		return $('#periodeDebut');
	}
	
	function $getWidgetPassageFacturation() {
		return $('#widgetPassageFacturation');
	}

	$getWidgetPassageFacturation().change(function () {
		var idPassageFacturation = $getIdPassageFacturation().val();
		if(idPassageFacturation!=0) {
			$getPeriodeDebut().attr('disabled','disabled');
			$getPeriodeFin().attr('disabled','disabled');
		} else {
			$getPeriodeDebut().removeAttr('disabled');
			$getPeriodeFin().removeAttr('disabled');		
		}
		
		//Si le contenu est vide, on force alors l'id du passage à 0
		var value = $(this).val();
		if(value.length == 0) {
			$getIdPassageFacturation().val(0);
		}
	});
	
	$getPeriodeDebut().focusout(function () {
		var value = $(this).val();
		if(value.length > 0) {
			$getWidgetPassageFacturation().attr('disabled','disabled');
		} else {
			$getWidgetPassageFacturation().removeAttr('disabled');
		}
	});
	
	$("#launchListePrestations").click(function () {
		$("#launchListePrestations").prop('disabled', true);
		
		if(globazGlobal.checkFields()) {
			document.forms[0].elements('userAction').value="vulpecula.listes.prestations.executer";
			document.forms[0].submit();
		} else {
			$("#launchListePrestations").removeAttr('disabled');
		}
	});
});

globazGlobal.checkFields = function () {
	function isPassageSaisie(mainForm) {
		return mainForm.idPassageFacturation.length > 0 && mainForm.idPassageFacturation != "0";
	}
	
	function checkPeriode(mainForm) {
		var dateDebut = getDateFromFormat(mainForm.periodeDebut, 'dd.MM.yyyy');
		var dateFin = getDateFromFormat(mainForm.periodeFin, 'dd.MM.yyyy');
		
		if(isPeriodeSaisie(mainForm)) {
			return dateFin==0 || dateDebut<=dateFin;
		}
		return false;
	}
	
	function isPeriodeSaisie(mainForm) {
		return mainForm.periodeDebut
	}
	
	var mainForm = $('form[name="mainForm"]').serializeFormJSON();
	if(isPeriodeSaisie(mainForm) && isPassageSaisie(mainForm)) {
		showErrorDialog(messagePeriodeOuLot);
		return false;
	} else if(isPeriodeSaisie(mainForm)) {
		if(!checkPeriode(mainForm)) {
			showErrorDialog(messagePeriodeFinPlusGrandDebut);
			return false;
		}
	} else if(!isPeriodeSaisie(mainForm) && !isPassageSaisie(mainForm)) {
		showErrorDialog(messagePeriodeOuLotRequis);
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
				<td><ct:FWLabel key="JSP_DESTINATAIRE"/></td>
				<td><ct:FWCodeSelectTag name="destinataire" codeType="PTLISDEST" defaut="${viewBean.destinataire}"/></td>
			</tr>	
			<tr>
				<td><label for="prestations"><ct:FWLabel key="JSP_PRESTATIONS"/></label></td>
				<td>
					<ct:FWListSelectTag name="typePrestation" data="${viewBean.typesModules}" defaut="${viewBean.typePrestation}"/>
				</td>				
			</tr>
			<tr>
				<td><label for="lot"><ct:FWLabel key="JSP_LOT"/></label></td>
				<td>
					<input id="idPassageFacturation" name="idPassageFacturation" type="hidden" />
					<ct:widget id='widgetPassageFacturation'
		   				name = 'designationPassageFacturation'
		   				styleClass="widgetAdmin"
		   				notation="data-g-string='mandatory:false' value='${viewBean.designationPassageFacturation}'">
						<ct:widgetService defaultLaunchSize="1" methodName="searchPassageFacturationPrestations" className="${viewBean.passageViewService}">
						<ct:widgetCriteria criteria="forIdPassage" label="JSP_ID_PASSAGE"/>
						<ct:widgetCriteria criteria="likeLibellePassage" label="JSP_LIBELLE_PASSAGE"/>
						<ct:widgetLineFormatter format="#{passageModel.idPassage} - #{passageModel.libellePassage}"/>
						<ct:widgetDynamicCriteria>
						<script type="text/javascript">
							function(){
								return 'forTypeFacturation=' + $('#typePrestation').val();
							}
						</script>
						</ct:widgetDynamicCriteria>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									this.value=$(element).attr('passageModel.idPassage')+'-'+$(element).attr('passageModel.libellePassage');
									$('#idPassageFacturation').val($(element).attr('passageModel.idPassage'));
								}
							</script>
					</ct:widgetJSReturnFunction>
					</ct:widgetService>
					</ct:widget>
				</td>				
			</tr>
			<tr>
				<td><label for="employeur"><ct:FWLabel key="JSP_EMPLOYEUR"/></label></td>
				<td>
					<input id="idEmployeur" name="idEmployeur" type="hidden" />
					<ct:widget id='widgetEmployeur'
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
									this.value=$(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero')+'-'+$(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
									$('#idEmployeur').val($(element).attr('affiliationTiersComplexModel.affiliation.affiliationId'));
								}
							</script>
					</ct:widgetJSReturnFunction>
					</ct:widgetService>
					</ct:widget>
				</td>				
			</tr>
			<tr>
				<td><label for="travailleur"><ct:FWLabel key="JSP_TRAVAILLEUR"/></label></td>
				<td>
					<input id="idTravailleur" name="idTravailleur" type="hidden" />
					<ct:widget id='widgetTravailleur'
		   				name = 'designationTravailleur'
		   				styleClass="widgetAdmin"
		   				notation="data-g-string='mandatory:false'  value='${viewBean.designationTravailleur}'">
						<ct:widgetService defaultLaunchSize="1" methodName="search" className="${viewBean.travailleurViewService}">
						travailleurSimpleModel.id,personneEtendueComplexModel.tiers.designation1,personneEtendueComplexModel.tiers.designation2
						<ct:widgetCriteria criteria="forIdTravailleur" label="JSP_ID_TRAVAILLEUR"/>
						<ct:widgetCriteria criteria="likeNom" label="JSP_NOM"/>
						<ct:widgetCriteria criteria="likePrenom" label="JSP_PRENOM"/>
						<ct:widgetLineFormatter format="#{travailleurSimpleModel.id} - #{personneEtendueComplexModel.tiers.designation1} #{personneEtendueComplexModel.tiers.designation2}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									this.value=$(element).attr('travailleurSimpleModel.id')+'-'+$(element).attr('personneEtendueComplexModel.tiers.designation1')+' '+$(element).attr('personneEtendueComplexModel.tiers.designation2');
									$('#idTravailleur').val($(element).attr('travailleurSimpleModel.id'));
								}
							</script>
					</ct:widgetJSReturnFunction>
					</ct:widgetService>
					</ct:widget>
				</td>				
			</tr>
			<tr>
				<td><label for="convention"><ct:FWLabel key="JSP_CONVENTION"/></label></td>
				<td>
					<select name="idConvention">
						<c:choose>
							<c:when test="${empty viewBean.idConvention}">
								<option value="" selected="selected"></option>
								<c:forEach var="convention" items="${viewBean.conventions}">
									<option value="${convention.id}">${convention.designation}</option>
								</c:forEach>
						</c:when>
						<c:otherwise>
							<c:forEach var="convention" items="${viewBean.conventions}">
								<c:choose>
								<c:when test="${convention.id==viewBean.idConvention}">
									<option selected="selected" value="${convention.id}">${convention.designation}</option>
								</c:when>
								<c:otherwise>
									<option value="${convention.id}">${convention.designation}</option>
								</c:otherwise>
								</c:choose>
							</c:forEach>							
						</c:otherwise>
						</c:choose>
					</select>
				</td>				
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PERIODE"/></td>
				<td>
					<input id="periodeDebut" name="periodeDebut" value="${viewBean.periodeDebut}" type="text" data-g-calendar="" />
					<ct:FWLabel key="JSP_AU"/>
					<input id="periodeFin" name="periodeFin" value="${viewBean.periodeFin}" type="text" data-g-calendar="" />
				</td>
			</tr>	
			<c:if test="${not processLaunched}">		
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launchListePrestations" type="button" name="launchListePrestations" value='<ct:FWLabel key="JSP_LANCER"/>'/>
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