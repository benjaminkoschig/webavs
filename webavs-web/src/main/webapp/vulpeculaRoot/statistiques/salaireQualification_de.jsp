<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT2010"/>
<c:set var="labelTitreEcran" value="JSP_SALAIRE_QUALIFICATION"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">

var messageQualificationRequis = '${viewBean.messageQualificationRequis}';

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

/**
 * Chargement des codesQualifications en fonction de la convention
 */
function loadCodesQualifications(idConvention){
	var options = {
			serviceClassName:'ch.globaz.vulpecula.web.views.postetravail.PosteTravailViewService',
			serviceMethodName:'getQualificationsParConvention',
			parametres:idConvention,
			callBack:function (data) {
				$('#trQualif').show();
				var selector = $('#codesQualifications');
				selector.empty();
				for(var i = 0; i < data.length; i++){
					var e = data[i];
					selector.append('<option value="' + e.id + '">' + e.libelle + '</option>');
				}
			}
	};
	vulpeculaUtils.lancementService(options);
}

//chargement du dom jquery
$(function () {
	if($('#idConvention').val() != ""){
		loadCodesQualifications($('#idConvention').val());
	}
	
	$('#idConvention').change(function(){
		if($('#idConvention').val() != ""){
			loadCodesQualifications($(this).val());
		} else {
			$('#trQualif').hide();
			var selector = $('#codesQualifications');
			selector.empty();
		}
	});
	
	$('#launch').click(function() {
		if(!$('#idConvention').val() && $('#codesQualifications').val()){
			showErrorDialog(messageQualificationRequis);
		}else{
			$(this).prop('disabled',true);
			document.forms[0].elements('userAction').value="vulpecula.statistiques.salaireQualification.executer";
			document.forms[0].submit();	
		}
	});
});
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<style type="text/css">
	#mainWrapper {
		height: auto !important;
	}
</style>
<div style="width:100%;text-align:center;">
	<div>
		<table>
			<tr>
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" data-g-email="mandatory:true" name="email" value="${viewBean.email}" /></td>
			</tr>
			<tr>
				<td><label for="convention"><ct:FWLabel key="JSP_CONVENTION"/></label></td>
				<td>
					<select  name="idConvention" id="idConvention">
						<option value="" selected="selected"></option>
						<c:forEach var="convention" items="${viewBean.conventions}">
							<c:choose>
								<c:when test="${viewBean.idConvention==convention.id}">
									<option selected="selected" value="${convention.id}">${convention.designation}</option>
								</c:when>
								<c:otherwise>
									<option value="${convention.id}">${convention.designation}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr id="trQualif" style="display:none;">
				<td><label><ct:FWLabel key="JSP_QUALIFICATION"/></label></td>
				<td>
					<select id="codesQualifications" name="codesQualifications" multiple>
						
					</select>
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PERIODE"/></td>
				<td>
					<input id="periodeDebut" data-g-calendar="type:month,mandatory:true" name="periodeDebut" style="width:50px;" value="${viewBean.periodeDebut}" type="text"/>
					<ct:FWLabel key="JSP_A_FIN"/>
					<input id="periodeFin" data-g-calendar="type:month,mandatory:true" name="periodeFin" style="width:50px;" value="${viewBean.periodeFin}" type="text"/>
				</td>
			</tr>
			<c:if test="${not processLaunched}">
			<tr>
				<td style="text-align:center;" colspan="2">
					<br />
					<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER" />'/>
				</td>
			</tr>
			</c:if>
		</table>
	</div>
</div>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED" />
	</div>
</c:if>
 
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>