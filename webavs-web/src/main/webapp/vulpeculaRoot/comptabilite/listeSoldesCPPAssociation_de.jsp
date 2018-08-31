<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@page import="globaz.vulpecula.vb.comptabilite.PTListeSoldesCPPAssociationViewBean"%>
<%@page import="globaz.osiris.db.comptes.CARoleViewBean"%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT6005"/>
<c:set var="labelTitreEcran" value="JSP_PT_LISTES_SOLDES_CPP_ASSOCIATION"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionListe" value="vulpecula.comptabilite" />
<ct:checkRight var="hasCreateRightOnListes" element="${userActionListe}" crud="c" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
<%
%>

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
		document.forms[0].elements('userAction').value="vulpecula.comptabilite.listeSoldesCPPAssociation.executer";
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
			<tr>
				<td><label for="dateUntil"><ct:FWLabel key="JSP_LISTE_SOLDES_OUVERT_CPP_ASSOCIATION_DATE_JUSQUAU"/></label></td>
				<td><input id="dateUntil" type="text" name="dateUntil" value="${viewBean.dateUntil}" /></td>
			</tr>
			<tr>
				<td><label for="orderBy"><ct:FWLabel key="JSP_LISTE_SOLDES_OUVERT_CPP_ASSOCIATION_TRI_PAR"/></label></td>
				<td>
					<select id="orderBy" name="orderBy">
						<option id="IDEXTERNEROLE" value="IDEXTERNEROLE" selected><ct:FWLabel key="JSP_LISTE_SOLDES_OUVERT_CPP_ASSOCIATION_TRI_COMPTE_ANNEXE"/></option>
						<option id="NOM_CPP_ASSOCIATION" value="NOM_CPP_ASSOCIATION"><ct:FWLabel key="JSP_LISTE_SOLDES_OUVERT_CPP_ASSOCIATION_TRI_NOM_CPP_ASSOCIATION"/></option>
					</select>
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

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>