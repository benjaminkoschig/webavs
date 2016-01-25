<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@page import="globaz.perseus.vb.decision.PFDecisionRefusDemandeViewBean"%>
<%@page import="globaz.globall.db.BSession"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_LOT_"
	idEcran="PPF0221";
	PFDecisionRefusDemandeViewBean viewBean = (PFDecisionRefusDemandeViewBean) session.getAttribute("viewBean");
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String idDossier = viewBean.getIdDossier(); //request.getParameter("idDossier");
	String eMailAdresse = objSession.getUserEMail();
	
	userActionValue = "perseus.decision.decisionRefusDemande.executer";
	
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
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
		<table>
			<tr>
				<td><label style="font-weight: bold;" for=adrMail><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_EMAIL_GESTIONNAIRE" /></label>&nbsp;</td>
				<td width="30"></td>
				<td><input type="text" name="eMailAdresse" id="eMailAdresse" value="<%=eMailAdresse%>" class="libelleLong"></td>
				<td width="50"></td>
				<td><label style="font-weight: bold;" for="dateDocument"><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_DATE_DOCUMENT" /></label>&nbsp;</td>
				<td width="30"></td>
				<td><input type="text" name="dateDocument" value="<%=viewBean.getDateDocument()%>"/></td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td><label style="font-weight: bold;" for="utilisateurPreparation"><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_GESTIONNAIRE" /></label>&nbsp;</td>
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
				<td height="10"></td>
			</tr>
			<tr>
				<td><label style="font-weight: bold;" for=agence><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_CAISSE" /></label>&nbsp;</td>
				<td width="87"></td>
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
				<td><label style="font-weight: bold;" for=agenceCommunale><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_AGENCE" /></label>&nbsp;</td>
				<td width="87"></td>
				<td><ct:FWListSelectTag name= "agenceAssurance" data="<%=viewBean.getListeAgenceAssurance()%>" notation="data-g-select='mandatory:true'" defaut = "test"/></td>
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
				<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_REQUERANT" /></label>&nbsp;</td>
				<td valign="top"><%=viewBean.getDetailAssure()%></td>
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>			
				<td valign="top">
					<label style="font-weight: bold;" for="adresseCourrier"><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_ADRESSE" />&nbsp;</label>
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
						
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>