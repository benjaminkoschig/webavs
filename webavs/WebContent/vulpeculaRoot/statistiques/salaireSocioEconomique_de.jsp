<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Param�trage global de la page ************************************************************** --%>
<%-- labels n� ecran et titre --%>
<c:set var="idEcran" value="PPT2009"/>
<c:set var="labelTitreEcran" value="JSP_SALAIRE_SOCIO_ECONOMIQUE"/>

<%-- visibilt�s des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>

<%--  *************************************************************** Script propre � la page **************************************************************** --%>
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
//chargement du dom jquery
$(function () {
	$('#launch').click(function() {
		$(this).prop('disabled',true);
		document.forms[0].elements('userAction').value="vulpecula.statistiques.salaireSocioEconomique.executer";
		document.forms[0].submit();
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