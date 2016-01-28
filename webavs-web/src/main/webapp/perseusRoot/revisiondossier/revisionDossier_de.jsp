<%@page import="globaz.perseus.vb.revisiondossier.PFRevisionDossierViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	
	PFRevisionDossierViewBean viewBean = (PFRevisionDossierViewBean) session.getAttribute("viewBean");
	idEcran="PPF2211";

	userActionValue = "perseus.revisiondossier.revisionDossier.executer";

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	
	if(objSession.hasRight("perseus", FWSecureConstants.ADD)){
		showProcessButton = !processLaunched;
	}else{
		showProcessButton = false;
	}
	
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>


<script type="text/javascript">

	function validate() {
		return window.confirm("<%=objSession.getLabel("JSP_PF_REVISION_DOSSIER_CONFIRMATION")%>")
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
	
	function validate() {
		ok = true;
		if ($("#adresseMail").val() === "") {
			ok = false;
		}
		if ($("#csCaisse").val() === "") {
			ok = false;
		}
		if ($("#dateRevision").val() === "") {
			ok = false;
		}
		if (!ok) {
			alert("Les champs marqués d'une * sont obligatoires !");
		}
		return ok;
	}

</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_REVISION_DOSSIER_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
														
<tr>
	<td>
		<table>
			<tr>
				<td><label style="font-weight: bold;" for=adresseMail><ct:FWLabel key="JSP_PF_REVISION_DOSSIER_MAIL" /></label>&nbsp;</td>
				
				<td><input type="text" name="adresseMail" id="adresseMail" value="<%=JadeThread.currentUserEmail()%>" class="libelleLong"></td>
				
			</tr>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>	
				<td><label style="font-weight: bold;" for="dateRevision"><ct:FWLabel key="JSP_PF_REVISION_DOSSIER_DATE" /></label>&nbsp;</td>
			
				<td><input type="text" name="dateRevision" id="dateRevision" value="<%=JadeStringUtil.toNotNullString(viewBean.getDateRevision())%>" data-g-calendar="mandatory:true,type:month"/></td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td>
		<table>
			<tr>
				<td height="10"></td>
			</tr>
			<tr>
				<td><label style="font-weight: bold;" for=agence><ct:FWLabel key="JSP_PF_DECISION_REFUS_DEMANDE_CAISSE" /></label>&nbsp;</td>
				<td width="144"></td>
				<td>
					<ct:select name="csCaisse" id="csCaisse" notation="data-g-select='mandatory:true'" defaultValue="" wantBlank="true" onchange="afficherGed(this)">
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
		</table>
	</td>
</tr>
		
					
						<%-- /tpl:insert --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/process/bodyClose.jspf" %>