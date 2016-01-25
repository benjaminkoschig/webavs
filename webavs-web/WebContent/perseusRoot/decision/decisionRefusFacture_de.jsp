<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@page import="ch.globaz.perseus.business.models.situationfamille.MembreFamille"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeFacture"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeGarde"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="globaz.perseus.vb.decision.PFDecisionRefusFactureViewBean"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.perseus.business.models.decision.CopieDecision"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.models.decision.AnnexeDecision"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.constantes.CSChoixDecision"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDecision"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDecision"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Calendar"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page
	import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@ page language="java" import="globaz.globall.http.*"
	contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/process/header.jspf"%>
<%-- tpl:put name="zoneInit" --%>
<%
	PFDecisionRefusFactureViewBean viewBean = (PFDecisionRefusFactureViewBean) session.getAttribute("viewBean");

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		
	String eMailAdresse = objSession.getUserEMail();
	String idDossier = viewBean.getIdDossier();
	String user = objSession.getUserName();
	
	userActionValue = "perseus.decision.decisionRefusFacture.executer";
	
	//Les labels de cette page commence par le préfix "JSP_PF_DECISION_A"
	idEcran = "PPF0231";
	autoShowErrorPopup = true;
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		showProcessButton = !processLaunched;
	}else{
		showProcessButton = false;
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>


<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf"%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script> 
<%-- tpl:put name="zoneScripts" --%>

<script type="text/javascript">

	function validate() {
		ok = true;
		if ($("#eMailAdresse").val() === "") {
			ok = false;
		}
		if ($("#dateDocument").val() === "") {
			ok = false;
		}
		if ($("#gestionnaire").val() === "") {
			ok = false;
		}
		if ($("#caisse").val() === "") {
			ok = false;
		}
		if ($("#agenceAssurance").val() === "") {
			ok = false;
		}
		if ($("#typeFacture").val() === "") {
			ok = false;
		}
		if ($("#texteLibre").val() === "") {
			ok = false;
		}
		if (!ok) {
			alert("Les champs marqués d'une * sont obligatoires !");
		}
		return ok;
	}
	
	function afficherGed(csCaisse){
		if (csCaisse.value == <%=CSCaisse.CCVD.getCodeSystem()%>) {
			if(<%=viewBean.isSendToGed(CSCaisse.CCVD.getCodeSystem())%>){
				document.all('ged').style.display = 'block';
			}else{
				document.all('ged').style.display = 'none';
			}
		}else{
			if(<%=viewBean.isSendToGed(CSCaisse.AGENCE_LAUSANNE.getCodeSystem())%>){
				document.all('ged').style.display = 'block';
			}else{
				document.all('ged').style.display = 'none';
			}
		}
	}

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf"%>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_TITRE" />
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
	<table>
		<tr>
			<td><label style="font-weight: bold;" for=eMailAdresse><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_EMAIL_GESTIONNAIRE" /></label>&nbsp;</td>
			<td width="30"></td>
			<td><input type="text" name="eMailAdresse" id="eMailAdresse" value="<%=eMailAdresse%>" class="libelleLong" data-g-string="mandatory:true" />
			</td>
			<td width="50"></td>
			<td><label style="font-weight: bold;" for="dateDocument"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_DATE_DOCUMENT" /></label>&nbsp;</td>
			<td width="30"></td>
			<td><input type="text" class="clearable" name="dateDocument" id="dateDocument" data-g-calendar="mandatory:true" value="<%=viewBean.getDateDocumentDefault()%>" /></td>
			<td><input type="hidden" name="dateDocument" value="<%=viewBean.getDateDocumentDefault()%>"/></td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td><label style="font-weight: bold;" for="utilisateurPreparation"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_NOM_GESTIONNAIRE" /></label>&nbsp;</td>
			<td width="30"></td>
			<td><ct:FWListSelectTag
				name= "gestionnaire"
			 	data="<%=globaz.perseus.utils.PFGestionnaireHelper.getResponsableData(objSession)%>"
				notation="data-g-select='mandatory:true'"
				defaut = "<%=objSession.getUserName()%>"/>
			</td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td colspan="6"><hr></td>
</tr>

<tr>
	<td>
	<table>
		<tr>
			<td><label style="font-weight: bold;" for=agence><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_CAISSE" /></label>&nbsp;</td>
			<td width="25"></td>
			<td>
				<ct:select name="caisse" id="caisse" notation="data-g-select='mandatory:true'" defaultValue="" wantBlank="true" onchange="afficherGed(this)">
					<ct:optionsCodesSystems csFamille="<%= IPFConstantes.CSGROUP_CAISSE %>"/>
				</ct:select>
			</td>
		</tr>
		
		<tbody id="ged" style="display:none;">
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_GED"/></label></td>
				<td width="25"></td>
				<td><input type="checkbox" name="isSendToGed" id="isSendToGed" <%=viewBean.getIsSendToGed()%>></td>
			</tr>
		</tbody>
		
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td><label style="font-weight: bold;" for=agenceCommunale><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_AGENCE" /></label>&nbsp;</td>
			<td width="25"></td>
			<td><ct:FWListSelectTag name= "agenceAssurance" data="<%=viewBean.getListeAgenceAssurance()%>" notation="data-g-select='mandatory:true'" defaut = "test"/></td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td><label style="font-weight: bold;" for=typeFacture><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_TYPE_FACTURE" /></label>&nbsp;</td>
			<td width="25"></td>
			<td><ct:FWCodeSelectTag  name= "typeFacture" codeType="<%=IPFConstantes.CSGROUP_TYPE_FACTURE%>" notation="data-g-select='mandatory:true'" wantBlank="true" defaut = "test"/></td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	</td>
</tr>

<tr>
	<td colspan="6"><hr></td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_NOM_REQUERANT" /></label>&nbsp;</td>
			<td valign="top"><%=viewBean.getDetailAssure(idDossier)%></td>
		</tr>
		<tr>
			<td height="10"></td>
		</tr>
		<tr>			
			<td valign="top">
				<label style="font-weight: bold;" for="adresseCourrier"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_ADRESSE_COURRIER" />&nbsp;</label>
			</td>
			<td valign="top">
				<div name="adresseCourrier" data-g-adresse="mandatory:true,service:findAdresse,defaultvalue:¦<%=viewBean.getAdresseCourrier()%>¦">
    				<input type="hidden" id="idTiersAdresseCourrier" class="avoirAdresse.idTiers" name="idTiersAdresseCourrier" value="<%=viewBean.getIdTiersAdresseCourrier()%>" />
    				<input type="hidden" id="idDomaineApplicatifAdresseCourrier" class="avoirAdresse.idApplication" name="idDomaineApplicatifAdresseCourrier" value="<%=viewBean.getIdDomaineApplicatifAdresseCourrier()%>" />
				</div>
			</td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td colspan="6"><hr></td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td height="10"></td>
		</tr>
		<tr>
			<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_MEMBRE_FAMILLE" /></label>&nbsp;</td>
			<td width="20"></td>
			<td>
				<ct:select name="membreFamille" 
					   id="membreFamille"
					   wantBlank="true" 
					   notation="data-g-select='mandatory:false'" >
				    <%for(MembreFamille mf:viewBean.getListeMembreFamille()){ 
				     	String desc =mf.getPersonneEtendue().getTiers().getDesignation1() + " "
						+ mf.getPersonneEtendue().getTiers().getDesignation2(); 
				     %>
						<ct:option id="<%=mf.getId()%>" value="<%=mf.getId()%>"  label="<%=desc%>"/>
					<%} %>
			</ct:select>
			</td>
		</tr> 
		<tr>
			<td height="10"></td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td colspan="6"><hr></td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_MOTIF_TEXTE_LIBRE" /></label>&nbsp;</td>
		</tr>
	 	<tr>
			<td><u><i><ct:FWLabel key="JSP_PF_DECISION_A_PARAGRAPHE_PRECEDENT"/></i></u></td>
		</tr>
		<tr>
			<td valign="top" style="height: 1cm; width: 20cm"><i><%=viewBean.getParagraphePrecedent()%></i></td>
		</tr>
	</table>
	</td>
</tr>
<tr>
	<td>
	<table>
		<tr>
			<td><textarea name="texteLibre" id="texteLibre" data-g-string="mandatory:true" rows="10" cols="100"></textarea>&nbsp;</td>
		</tr>
	</table>
	</td>
</tr>

						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>