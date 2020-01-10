<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="ch.globaz.al.properties.ALProperties" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT4001"/>
<c:set var="labelTitreEcran" value="JSP_IMPOT_SOURCE_PAIEMENTS_AF"/>

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
	$('#launch').click(function () {
		$("#launch").prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.is.iSTraitement.executer";
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
			<tr>
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>
			<%if(!ALProperties.IMPOT_A_LA_SOURCE.getBooleanValue()) {%>
			<tr>
				<td><label for="processusPaiementsDirects"><ct:FWLabel key="JSP_PROCESSUS_AF_PAIEMENTS_DIRECTS"/></label></td>
				<td><input id="processusPaiementsDirects" data-g-autocomplete="
							service:¦${viewBean.processusAFServiceCRUD}¦,
							method:¦search¦,
							criterias:¦{'forId': 'Id','forDatePeriode': 'Période AF'}¦,
							lineFormatter:¦#{processusPeriodiqueModel.id},#{periodeAFModel.datePeriode}¦,
							modelReturnVariables:¦processusPeriodiqueModel.id¦,
							constCriterias:¦forEtat=${viewBean.csEtatProcessusTermine},forIsPartiel=true,forBusinessProcessus=${viewBean.configPaiementsDirects}¦
							functionReturn:¦
								function(element) {
									var idProcessus = $(element).attr('processusPeriodiqueModel.id');
									$('#processusPaiementsDirects').val(idProcessus);
									$('#idProcessusPaiementDirect').val(idProcessus);
							}
						¦,
						nbOfCharBeforeLaunch:¦0¦,
						mandatory:true"/>
					<input id="idProcessusPaiementDirect" name="idProcessusPaiementDirect" type="hidden" />
				</td>
			</tr>
			<% } %>
			<tr>
				<td><label for="processusPaiementsIndirects"><ct:FWLabel key="JSP_PROCESSUS_AF_PAIEMENTS_INDIRECTS"/></label></td>
				<td><input id="processusPaiementsIndirects" data-g-autocomplete="
							service:¦${viewBean.processusAFServiceCRUD}¦,
							method:¦search¦,
							criterias:¦{'forId': 'Id','forDatePeriode': 'Période AF'}¦,
							lineFormatter:¦#{processusPeriodiqueModel.id},#{periodeAFModel.datePeriode}¦,
							modelReturnVariables:¦processusPeriodiqueModel.id¦,
							constCriterias:¦forEtat=${viewBean.csEtatProcessusTermine},forIsPartiel=true,forBusinessProcessus=${viewBean.configPaiementsIndirects}¦
							functionReturn:¦
								function(element) {
									var idProcessus = $(element).attr('processusPeriodiqueModel.id');
									$('#processusPaiementsIndirects').val(idProcessus);
									$('#idProcessusPaiementIndirect').val(idProcessus);									
							}
						¦,
						nbOfCharBeforeLaunch:¦0¦,
						mandatory:true"/>
					<input id="idProcessusPaiementIndirect" name="idProcessusPaiementIndirect" type="hidden" />
				</td>
			</tr>
			<c:if test="${not processLaunched}">	
			<tr>
				<td style="text-align: center;" colspan="2">
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