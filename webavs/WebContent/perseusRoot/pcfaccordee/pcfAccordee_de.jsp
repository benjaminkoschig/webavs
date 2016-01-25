<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.menu.ext.implementation.FWSessionAdapter"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDecision"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatPcfaccordee"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.pcfaccordee.PFPcfAccordeeViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
	PFPcfAccordeeViewBean viewBean = (PFPcfAccordeeViewBean) session.getAttribute("viewBean");
	autoShowErrorPopup = true;
	idEcran="PPF1011";

	
	bButtonUpdate = false;
	
	if (viewBean.getPcfAccordee().getDemande().getSimpleDemande().getFromRI()) {
		bButtonUpdate = true;
	}
		
	if (CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(viewBean.getPcfAccordee().getSimplePCFAccordee().getCsEtat())) {
		bButtonUpdate = false;
		bButtonDelete = false;	
	}
	
	if(!objSession.hasRight("perseus", FWSecureConstants.ADD)){
		bButtonUpdate = false;
		bButtonDelete = false;
	}
	
%>

<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script language="JavaScript">

	var pcalLink = "perseus?userAction=perseus.pcfaccordee.planCalcul.afficher&selectedId=";
	
	function recalculer() {
		document.location.href = "perseus?userAction=perseus.pcfaccordee.calcul.afficher&_method=upd&idDemande=<%=viewBean.getPcfAccordee().getDemande().getId() %>";
	}
	
	function del() {
   		if (window.confirm("<%=objSession.getLabel("JSP_PF_DOS_SUPPRESSION_CONFIRMATION")%>")){
	        document.forms[0].elements('userAction').value="perseus.pcfaccordee.pcfAccordee.supprimer";
	        document.forms[0].submit();
	    }
	}
	
	function validate() {
	    state = true;
        document.forms[0].elements('userAction').value="perseus.pcfaccordee.pcfAccordee.modifier";
	    return state;
	}    
	
	function cancel() {
		document.forms[0].elements('userAction').value="back";
	}
	
	function init() {}
	
	function postInit() {}
	
	var affichePlanCalcul = function (idPcal) {
		window.open(pcalLink+idPcal);
	}

</script>


<ct:menuChange displayId="menu" menuId="perseus-menuprincipal" showTab="options" />
<ct:menuChange displayId="options" menuId="perseus-optionspcfaccordee">
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getPcfAccordee().getDemande().getSituationFamiliale().getId() %>"/>
	<ct:menuSetAllParams key="idDemande" value="<%=viewBean.getPcfAccordee().getDemande().getId() %>"/>
	<ct:menuSetAllParams key="idDossier" value="<%=viewBean.getPcfAccordee().getDemande().getDossier().getId() %>"/>
	<ct:menuSetAllParams key="idPcfAccordee" value="<%=viewBean.getPcfAccordee().getId() %>"/>
	<ct:menuActivateNode active="yes" nodeId="DECISION_SUPPRESSIONVOLONTAIRE"/>
	<% if (!viewBean.getPcfAccordee().getDemande().getSimpleDemande().getFromRI() || CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(viewBean.getPcfAccordee().getSimplePCFAccordee().getCsEtat()) || CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande()) || viewBean.hasDecisionCreee()|| CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(viewBean.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande())|| CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(viewBean.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande())) { %>
	<ct:menuActivateNode active="no" nodeId="PREPARTIONPROJETDECISION"/>
	<% } else  { %>
	<ct:menuActivateNode active="yes" nodeId="PREPARTIONPROJETDECISION"/>
	<% } %>
	<% if (CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(viewBean.getPcfAccordee().getSimplePCFAccordee().getCsEtat()) || CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande()) || viewBean.hasDecisionCreee()) { %>
	<ct:menuActivateNode active="no" nodeId="DECAVECCALCUL"/>
	<% } else  { %>
	<ct:menuActivateNode active="yes" nodeId="DECAVECCALCUL"/>
	<% } %>
	<% if (CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(viewBean.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande())) { %>
	<ct:menuActivateNode active="no" nodeId="DECISIONS_PCFACCORDEES"/>
	<% } else  { %>
	<ct:menuActivateNode active="yes" nodeId="DECISIONS_PCFACCORDEES"/>
	<% } %>
	<% if (CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(viewBean.getPcfAccordee().getSimplePCFAccordee().getCsEtat())) { %>
	<ct:menuActivateNode active="yes" nodeId="DECISION_SUPPRESSIONVOLONTAIRE"/>
	<% } else  { %>
	<ct:menuActivateNode active="no" nodeId="DECISION_SUPPRESSIONVOLONTAIRE"/>
	<% } %>
</ct:menuChange>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_PF_PCFACCORDEE_TITRE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
						
						<tr>
							<td><ct:FWLabel key="JSP_PF_PCFACCORDEE_REQUERANT"/></td>
							<td><%=PFUserHelper.getDetailAssure(objSession, viewBean.getPcfAccordee().getDemande().getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()) %></td>
						</tr>
						<tr>
							<td colspan="2"><br></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_PF_PCFACCORDEE_PERIODE"/></td>
							<td colspan="2">
								<ct:FWLabel key="JSP_FR_PCFACCORDEE_DU"/>
								<span><strong><%=" "+viewBean.getPcfAccordee().getDemande().getSimpleDemande().getDateDebut() + " " %></strong></span>
								<ct:FWLabel key="JSP_FR_PCFACCORDEE_AU"/>
								<span><strong><%=" "+ viewBean.getPcfAccordee().getDemande().getSimpleDemande().getDateFin()%></strong></span>
							</td>
						</tr>
						<tr>
							<td colspan="2"><br /><hr /><br /></td>
						</tr>
						<tr>
							<td colspan="2">
								<table bgcolor="white" cellpadding="5">
									<thead>
										<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_DATE_CALCUL"/></th>
										<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_PRESTATION_MENSUELLE"/></th>
										<th><ct:FWLabel key="JSP_PF_PCFACCORDEE_EXCEDANT_REVENU"/></th>
										<th>&nbsp;</th>
									</thead>
									<tbody>
										<td><%=viewBean.getPcfAccordee().getSimplePCFAccordee().getDateCalcul() %></td>
										<td data-g-amountformatter=" "><%=viewBean.getPcfAccordee().getSimplePCFAccordee().getMontant() %></td>
										<td data-g-amountformatter=" "><%=viewBean.getPcfAccordee().getSimplePCFAccordee().getExcedantRevenu() %></td>
										<td><a href="#" onClick="affichePlanCalcul('<%= viewBean.getId()%>')">calcul</a></td>
									</tbody>
								</table>
							</td>
						</tr>
						<% if (viewBean.getPcfAccordee().getDemande().getSimpleDemande().getFromRI()) { %>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_PF_PCFACCORDEE_MESURE_ENCOURAGEMENT"/>
								</td>
								<td>
									<ct:inputText name="mesureEncouragement" notation="data-g-amount=' '"/>
								</td>
							</tr>
							<tr>
								<td>
									<ct:FWLabel key="JSP_PF_PCFACCORDEE_MESURE_CHARGES_LOYER"/>
								</td>
								<td>
									<ct:inputText name="mesureChargesLoyer" notation="data-g-amount=' '"/>
								</td>
							</tr>
							<% if (viewBean.getPcfAccordee().getDemande().getSimpleDemande().getCoaching()) { %>
								<tr>
									<td>
										<ct:FWLabel key="JSP_PF_PCFACCORDEE_MESURE_COACHING"/>
									</td>
									<td>
										<ct:inputText name="mesureCoaching" notation="data-g-amount=' '"/>
									</td>
								</tr>
							<% } %>
						<% } %>
						<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
					<% if (!CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(viewBean.getPcfAccordee().getSimplePCFAccordee().getCsEtat())&& objSession.hasRight("perseus", FWSecureConstants.UPDATE)) { %>
						<input type="button" class="btnCtrl" value="Recalculer" onclick="recalculer()" /> 
					<% } %>
				<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
