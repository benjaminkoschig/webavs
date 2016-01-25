<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeListeImpotSource"%>
<%@page import="globaz.perseus.vb.impotsource.PFListeImpotSourceViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
	
	idEcran="PPF2111";
	PFListeImpotSourceViewBean viewBean = (PFListeImpotSourceViewBean) session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	userActionValue = "perseus.impotsource.listeImpotSource.executer";
	//userActionValue = "perseus.attestationsfiscales.attestationsFiscales.executer";
	
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
	
	if ($("#typeListe").val() === "") {
		ok = false;
	}
	
	if (!ok) {
		alert("Les champs marqués d'une * sont obligatoires !");
	}
	return ok;
}

function afficherPeriode(csTypeListe){
	if (csTypeListe.value == "") {
		document.all('periodeListeRecapitulative').style.display = 'none';
		document.all('periodeListeCorrective').style.display = 'none';
	}
	else if (csTypeListe.value == <%=CSTypeListeImpotSource.LISTE_RECAPITULATIVE.getCodeSystem()%>) {
			document.all('periodeListeRecapitulative').style.display = 'block';
			document.all('periodeListeCorrective').style.display = 'none';
	}else{
			document.all('periodeListeCorrective').style.display = 'block';
			document.all('periodeListeRecapitulative').style.display = 'none';
	}
}


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_GENERATION_LISTE_IP_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

<tr>
	<td>
		<table>	
			<tr height="20"><td width="400"></td></tr>
			<tr>
				<td><label style="font-weight: bold;" for=numeroDebiteur><ct:FWLabel key="JSP_PF_GENERATION_LISTE_IP_NUMERO_DEBITEUR"/></label></td>
				<td><input type="text" name="numeroDebiteur" id="numeroDebiteur" value="<%=viewBean.getNumeroDebiteur()%>" class="disabled" readonly></td>
			</tr>
		
		
			<tr height="20"><td width="400"></td></tr>
			<tr>
				<td><label style="font-weight: bold;" for=eMailAdresse><ct:FWLabel key="JSP_PF_GENERATION_LISTE_IP_E_ADMAIL"/></label></td>
				<td><input type="text" name="eMailAdresse" id="eMailAdresse" value="<%=viewBean.geteMailAdresse(objSession.getUserEMail())%>" class="libelleLong" data-g-string="mandatory:true"></td>
			</tr>
			
			<tr height="20"><td width="400"></td></tr>
			<tr>
				<td><label style="font-weight: bold;" for=typeListe><ct:FWLabel key="JSP_PF_GENERATION_LISTE_IP_TYPE"/></label></td>
					<td><ct:select name="typeListe" id="typeListe" notation="data-g-select='mandatory:true'" defaultValue="" wantBlank="true" onchange="afficherPeriode(this)">
						<ct:optionsCodesSystems csFamille="<%= IPFConstantes.CSGROUP_TYPE_LISTE_IMPOT_SOURCE %>"/>
					</ct:select></td>
			</tr>
			
			
			<tbody id="periodeListeRecapitulative" style="display:none;">
			<tr>
				<tr height="20"><td width="400"></td></tr>
			</tr>
			<tr>
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_GENERATION_LISTE_IP_PERIODE"/></label></td>
				<td><input type="text" name="periodeGenerationListeRecapitulative" id="periodeGenerationListeRecapitulative" value="<%=viewBean.getPeriodeGenerationListeRecapitulative()%>" class="disabled" readonly></td>
				<input type="hidden" name="idPeriode" id="idPeriode" value="<%=viewBean.getIdPeriode()%>" />
			</tr>
			</tbody>
			
			<tbody id="periodeListeCorrective" style="display:none;">
			<tr>
				<tr height="20"><td width="400"></td></tr>
			</tr>
			<tr>
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_GENERATION_LISTE_CORRECTIVE_IP_PERIODE"/></label></td>
				<td><input type="text" name="anneeGenerationListeCorrective" id="anneeGenerationListeCorrective" value="<%=viewBean.getAnneeGenerationListeCorrective()%>" class="disabled" readonly></td>
			</tr>
			</tbody>
			
		</table>
	</td>
</tr>

<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>