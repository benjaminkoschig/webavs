<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT4003"/>
<c:set var="labelTitreEcran" value="JSP_ATTESTATION_AF"/>

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
globazGlobal.CS_LISTE_ALLOCATAIRE = '${viewBean.CSListeAllocataire}';
globazGlobal.CS_LISTE_FISC = '${viewBean.CSListeFisc}';

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
		var $launch = $('#launch');
		var $listeAttestationAF = $('#listeAttestationAF'); 
		var $configFisc = $('.configFisc');
		var $configAllocataire = $('.configAllocataire');
		var $periodeDebut = $('#per')
		
		$launch.click(function () {
			if(validation()) {
				$(this).prop('disabled', true);
				document.forms[0].elements('userAction').value="vulpecula.is.attestationsAF.executer";
				document.forms[0].submit();
			}
		});
		
		$listeAttestationAF.change(function() {
			var value = $(this).val();
			if(value==globazGlobal.CS_LISTE_ALLOCATAIRE) {
				$configFisc.hide();
				$configAllocataire.show();
			} else {
				$configFisc.show();
				$configAllocataire.hide();
			}
		});
})

function validation() {
	if(!notationManager.validateAndDisplayError()) return false;
	return true;
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
				<td><ct:FWLabel key="JSP_EMAIL"/></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>	
			<tr>
				<td><ct:FWLabel key="JSP_DESTINATAIRE"/></td>
				<td>
					<ct:FWCodeSelectTag name="listeAttestationAF" codeType="PTATTAF" defaut=""/>
				</td>
			</tr>			
			<tr class="configAllocataire">
				<td><ct:FWLabel key="JSP_ALLOCATAIRE"/></td>
				<td>
				<input id="designationAllocataire" name="descriptionAllocataire" data-g-autocomplete="
							service:¦${viewBean.allocataireComplexModelService}¦,
							method:¦search¦,
							criterias:¦{'forIdAllocataire': 'Id allocataire','likeNomAllocataireUpper' : 'Nom','likePrenomAllocataire':'Prénom'}¦,
							lineFormatter:¦#{allocataireModel.id},#{personneEtendueComplexModel.tiers.designation1},#{personneEtendueComplexModel.tiers.designation2}¦,
							modelReturnVariables:¦allocataireModel.idAllocataire,personneEtendueComplexModel.tiers.designation1,personneEtendueComplexModel.tiers.designation2¦,
							functionReturn:¦
								function(element) {
									var $element = $(element);
									var idAllocataire = $element.attr('allocataireModel.idAllocataire');
									var nomAllocataire = $element.attr('personneEtendueComplexModel.tiers.designation1');
									var prenomAllocataire = $element.attr('personneEtendueComplexModel.tiers.designation2');
									
									$('#idAllocataire').val(idAllocataire);
									$('#designationAllocataire').val(idAllocataire + ',' + nomAllocataire + ' ' + prenomAllocataire);
							}
						¦,
						nbOfCharBeforeLaunch:¦0¦,
						mandatory:true" value="${viewBean.descriptionAllocataire}"/>
					<input id="idAllocataire" name="idAllocataire" value="" type="hidden" />
				</td>
			</tr>		
			<tr class="configAllocataire">				
				<td><ct:FWLabel key="JSP_PERIODE_AF"/></td>
				<td>
					<input id="dateDebut" name="dateDebut" data-g-calendar="mandatory:true,type:month" value="${viewBean.dateDebut}" />
					<input id="dateFin" name="dateFin" data-g-calendar="mandatory:true,type:month" value="${viewBean.dateFin}" />
				</td>
			</tr>
			<tr class="configFisc" style="display: none;">
				<td><ct:FWLabel key="JSP_ANNEE"/></td>
				<td>
					<input id="annee" name="annee" data-g-integer="mandatory:true" value="${viewBean.currentYear}" />
				</td>
			</tr>
			<c:if test="${not processLaunched}">	
			<tr>
				<td style="text-align: center;" colspan="4">
					<br />
					<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER"/>'/>
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