<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.perseus.vb.attestationsfiscalesRP.PFAttestationsFiscalesRPViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_ATTESTATIONSFICALES"
	idEcran="PPF1911";
	PFAttestationsFiscalesRPViewBean viewBean = (PFAttestationsFiscalesRPViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	userActionValue = "perseus.attestationsfiscalesRP.attestationsFiscalesRP.executer";
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		showProcessButton = !processLaunched;
	}else{
		showProcessButton = false;
	}
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

var $email;
var $dateDocument;
var $anneeAttestation;
var $caisse;
var $detail;

var userAction;
function add() {}
function del() {}
function init() {}
function upd() {}
function postInit() {}
function cancel() {}

function validate() {
	ok = true;
	if ($("#eMailAdresse").val() === "") {
		ok = false;
	}
	if ($("#dateDocument").val() === "") {
		ok = false;
	}
	if ($("#anneeAttestation").val() === "") {
		ok = false;
	}
	if ($("#caisse").val() === "") {
		ok = false;
	}
	if ($("#nomCaisse").val() === "") {
		ok = false;
	}
	if ($("#rueCaisse").val() === "") {
		ok = false;
	}
	if ($("#codePostaleCaisse").val() === "") {
		ok = false;
	}
	if ($("#localiteCaisse").val() === "") {
		ok = false;
	}
	if ($("#prenomPersonneContact").val() === "") {
		ok = false;
	}
	if ($("#nomPersonneContact").val() === "") {
		ok = false;
	}
	if ($("#telephoneContactCaisse").val() === "") {
		ok = false;
	}
	
	if (!ok) {
		alert("Les champs marqués d'une * sont obligatoires !");
	}
	return ok;
}

function afficherDetailCaisse(){
	if ($caisse.val() === '<%=CSCaisse.CCVD.getCodeSystem()%>' || $caisse.val() === '<%=CSCaisse.AGENCE_LAUSANNE.getCodeSystem()%>') {
		$detail.show();
	} else {
		$detail.hide();
	}
}

$(document).ready(function () {
	$email = $('#eMailAdresse');
	$dateDocument = $('#dateDocument');
	$anneeAttestation = $('#anneeAttestation');
	$caisse = $('#caisse');
	$idDossier = $('#idDossier');
	
	$caisse.change(function () {
			document.location.href = "perseus?userAction=perseus.attestationsfiscalesRP.attestationsFiscalesRP.afficher"
								+ "&email=" + $email.val()
								+ "&caisse=" + $caisse.val()
								+ "&dateDocument=" + $dateDocument.val() 
								+ "&anneeAttestation=" + $anneeAttestation.val()
								+ "&idDossier=" + $idDossier.val();
	});

	$detail = $('.detailAttestationsFiscales');
	
	afficherDetailCaisse();

	$detail.find(':input:not(:button)').prop('disabled', true);
	
	$boutonModifier = $('.modifierCaisse');
	$boutonModifier.click(function(){
		$detail.find(':input:not(:button)').removeProp('disabled');	
	});
});
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ATTESTATIONSFISCALES_RP_E_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<% if (!JadeStringUtil.isNull(viewBean.getIdDossier())) { %>
<tr>
	<td>
		<table>
			<tr><td width="400"></td></tr>
			<tr>
				<td valign="top"><label style="font-weight: bold;"><ct:FWLabel key="JSP_ATTESTATIONSFISCALES_E_REQUERANT" />&nbsp;</label></td>
				<td valign="top"><%=viewBean.getDetailAssure()%></td>
			</tr>
		</table>
	</td>
</tr>
<% } %>
<tr>
	<td>
		<table>	
			<tr height="20"><td width="400"></td></tr>
			<tr>
				<td><label style="font-weight: bold;" for=eMailAdresse><ct:FWLabel key="JSP_ATTESTATIONSFISCALES_E_ADMAIL"/></label></td>
				<td><input type="text" name="eMailAdresse" id="eMailAdresse" value="<%=viewBean.geteMailAdresse(objSession.getUserEMail())%>" class="libelleLong" data-g-string="mandatory:true"></td>
				 	<input type="hidden" name="idDossier" id="idDossier" value="<%=viewBean.getIdDossier()%>" />
			</tr>
			<tr>
				<td><label style="font-weight: bold;" for="dateDocument"><ct:FWLabel key="JSP_ATTESTATIONSFISCALES_E_DATEDOCUMENT"/></label></td>
				<td><input type="text" class="clearable" name="dateDocument" id="dateDocument" data-g-calendar="mandatory:true" value="<%=viewBean.getDateDocument()%>" /></td>
			</tr>
			<tr>
				
				<td><label style="font-weight: bold;"for=anneeAttestation><ct:FWLabel key="JSP_ATTESTATIONSFISCALES_E_ANNEEATTESTATION"/></label></td>
				<td><input type="text" size="6" name="anneeAttestation" id="anneeAttestation" value="<%=viewBean.getAnneeAttestation() %>" data-g-integer="mandatory:true"/></td>
				
			</tr>
			<tr>
				
				<td><label style="font-weight: bold;" for=agence><ct:FWLabel key="JSP_ATTESTATIONFISCALES_E_CAISSE" /></label>&nbsp;</td>
				<td>
					<ct:select name="caisse" id="caisse" notation="data-g-select='mandatory:true'" defaultValue="" wantBlank="true">
						<ct:optionsCodesSystems csFamille="<%= IPFConstantes.CSGROUP_CAISSE %>"/>
					</ct:select>
				</td>
			</tr>
		</table>
	</td>
</tr>
<tr id="detailCaisse" class="detailAttestationsFiscales">
	<td>
		<table>
			<tr height="20"><td width="400"></td></tr>
			<tr>
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_ATTESTATION_NOM_CAISSE"/></label></td>
				<td><input type="text" size="45" name="nomCaisse" id="nomCaisse" data-g-string="mandatory:true" value="<%=viewBean.getNomCaisse()%>"></td>
			</tr>
			<tr>
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_ATTESTATION_RUE_CAISSE"/></label></td>
				<td><input type="text" size="27" name="rueCaisse" id="rueCaisse" data-g-string="mandatory:true" value="<%=viewBean.getRueCaisse()%>"></td>
			</tr>
		</table>
	</td>
</tr>
<tr id="detailLocalite" class="detailAttestationsFiscales">
	<td>
		<table>
			<tr>
				<td width="400"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_ATTESTATION_LOCALITE_CAISSE"/></label></td>
				<td><input type="text" size="3" name="codePostaleCaisse" id="codePostaleCaisse" notation="data-g-integer='mandatory:true'" value="<%=viewBean.getCodePostaleCaisse()%>"></td>
				<td><input type="text" name="localiteCaisse" id="localiteCaisse" data-g-string="mandatory:true" value="<%=viewBean.getLocaliteCaisse()%>"></td>
			</tr>
		</table>
	</td>
</tr>
<tr id="detailPersonneContact" class="detailAttestationsFiscales">
	<td>
		<table>
			<tr>
				<td width="400"><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_ATTESTATION_PERSONNE_CONTACT_CAISSE"/></label></td>
				<td><input type="text" size="8" name="prenomPersonneContact" id="prenomPersonneContact" data-g-string="mandatory:true" value="<%=viewBean.getPrenomPersonneContact()%>"></td>
				<td><input type="text" size="28" name="nomPersonneContact" id="nomPersonneContact" data-g-string="mandatory:true" value="<%=viewBean.getNomPersonneContact()%>"></td>
				<td> / <input type="text" size="12" class="clearable" name="telephoneContactCaisse" id="telephoneContactCaisse" data-g-string="mandatory:true" value="<%=viewBean.getTelephoneContactCaisse()%>"></td>
				<td></td>
				<td><input type="button" name="modifierCaisse" id="modifierCaisse" class="modifierCaisse" value="<ct:FWLabel key="JSP_PF_ATTESTATION_MODIFIER_CAISSE"/>" onclick="cancel()"></td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
		</table>
	</td>
</tr>
<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>