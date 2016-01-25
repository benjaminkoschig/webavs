<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="java.util.Map.Entry"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.perseus.vb.lot.PFImprimerDecisionFactureViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@page import="ch.globaz.perseus.business.constantes.CSCaisse"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFConstantes"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatLot"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_PF_LOT_"
	idEcran="PPF0761";
	PFImprimerDecisionFactureViewBean viewBean = (PFImprimerDecisionFactureViewBean) session.getAttribute("viewBean");
	
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	String adrMail = objSession.getUserEMail();
	
	userActionValue = "perseus.lot.imprimerDecisionFacture.executer";
	
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

<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.perseus.vb.lot.PFLotViewBean"%>
<SCRIPT language="javascript">
function afficherGed(csCaisse){
	if (csCaisse.value == <%=CSCaisse.CCVD.getCodeSystem()%>) {
		<% if(CSEtatLot.LOT_VALIDE.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getEtatCs()) && viewBean.isSendToGed(CSCaisse.CCVD.getCodeSystem())){ %>
			document.all('ged').style.display = 'block';
		<% } else { %>
			document.all('ged').style.display = 'none';
		<% } %>
	} else if (csCaisse.value == <%=CSCaisse.AGENCE_LAUSANNE.getCodeSystem()%>) {
		<% if(CSEtatLot.LOT_VALIDE.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getEtatCs()) && viewBean.isSendToGed(CSCaisse.AGENCE_LAUSANNE.getCodeSystem())){ %>
			document.all('ged').style.display = 'block';
		<% } else { %>
			document.all('ged').style.display = 'none';
		<% } %>
	} else {
		<% if(CSEtatLot.LOT_VALIDE.getCodeSystem().equals(viewBean.getLot().getSimpleLot().getEtatCs()) && viewBean.isSendToGed("")){ %>
			document.all('ged').style.display = 'block';
		<% } else { %>
			document.all('ged').style.display = 'none';
		<% } %>
	}
}


$(function (){ 	
	$("#checkboxAgences").fadeOut(0);
	$('#caisse').change(function(){
		var $this = $(this);
		if($this.val() === "Agences"){
			$("#checkboxAgences").fadeIn(0);
			
			var isCheckBoxChecked = false;
			$('#checkboxAgences input').each(function () {
				if($(this).is(':checked')){
					isCheckBoxChecked = true;
				}
				if(isCheckBoxChecked){
					$('#btnOk').show();
				}else{
					$('#btnOk').hide();
				}
			})
		}else{
			$("#checkboxAgences").fadeOut(0);
			$('#btnOk').show();
		}
	});
	
	$('#checkboxAgences input').change(function () {
		var isAllCheckBoxUnchecked = true;
		
		$('#checkboxAgences input').each(function () {
			if($(this).is(':checked')){
				isAllCheckBoxUnchecked = false;
			}
		})
		
		if(isAllCheckBoxUnchecked){
			$('#btnOk').hide();
		}else{
			$('#btnOk').show();
		}
		
	});
})

var agencesId = "";
function doOkAction() {
	agencesId = "";
	$('#checkboxAgences input:checked').each(function () {
		agencesId += this.id + ";";	
	});
	
$('#checkBoxAgenceVal').val(agencesId);
	
	var isValid = true;
	try {
		isValid = validate();
	} catch (ex) {
		//do nothing, valid is ok
	}
	if (isValid) {
		document.forms[0].submit();
	}
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PF_IMPRIMER_DECISION_FACTURE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<td>
		<table >
			<tr>
				<td><label style="font-weight: bold;" for=adrMail><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_EMAIL_GESTIONNAIRE" /></label>&nbsp;</td>
				<td width="30"></td>
				<td colspan=2><input type="text" name="adrMail" id="adrMail" value="<%=adrMail%>" class="libelleLong"></td>
				<td width="50"></td>
			</tr>
			<TR>
				<td><label style="font-weight: bold;" for="dateDocument"><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_DATE_DOCUMENT" /></label>&nbsp;</td>
					<td width="30"></td>
					<td colspan=2><input type="text" class="clearable" name="dateDocument" id="dateDocument" data-g-calendar="mandatory:true" value="<%=viewBean.getDateDocumentDefault()%>" /></td>
					<td><input type="hidden" name="dateDocument" value="<%=viewBean.getDateDocumentDefault()%>"/></td>
			</TR>
			<tr>
				<td valign="top"><label style="font-weight: bold;" for=agence><ct:FWLabel key="JSP_PF_DECISION_FACTURE_D_CAISSE" /></label>&nbsp;</td>
				<td width="25"></td>
				<td valign="top">
					<ct:select name="caisse" id="caisse" notation="data-g-select='mandatory:true'" defaultValue="" wantBlank="true" onchange="afficherGed(this)">
						<ct:optionsCodesSystems csFamille="<%= IPFConstantes.CSGROUP_CAISSE %>"/>
						<%if(!viewBean.getListeAgence().isEmpty()){ %>
							<ct:option value="Agences" id="Agences" label='Agences'/>
						<%}%>
					</ct:select>
				</td>
			
				<td id="checkboxAgences">	
					<%
					for(Entry<String, String> uneAgence : viewBean.getListeAgence().entrySet()) {
						String key = uneAgence.getKey();
						String value = uneAgence.getValue();
					%>
						<input type="checkbox" id="<%=key%>" checked="checked"> <%=value%> <br/>
					<%} %>
				</td>
				
				<input type="hidden" id="checkBoxAgenceVal" name="agencesSelectionne" />
		
			</tr>
				<tbody id="ged" style="display:none;">
				<tr>
					<td><label style="font-weight: bold;"><ct:FWLabel key="JSP_PF_DECISION_GED"/></label></td>
					<td width="25"></td>
					<td><input type="checkbox" name="isSendToGed" id="isSendToGed" <%=viewBean.getIsSendToGed()%>></td>
				</tr>
			</tbody>
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