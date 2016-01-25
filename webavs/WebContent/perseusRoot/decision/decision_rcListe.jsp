<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDecision"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PF_DECISION_L"
	PFDecisionListViewBean viewBean = (PFDecisionListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	menuName = "perseus-menuprincipal";	
	//detailLink = baseLink + "afficher";
	detailLink = "perseus?userAction=perseus.decision.decision.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%@ include file="/perseusRoot/ajax/javascriptsAndCSS.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="ch.globaz.perseus.business.constantes.IPFActions"%>
<%@page import="globaz.perseus.vb.decision.PFDecisionListViewBean"%>
<%@page import="globaz.perseus.vb.decision.PFDecisionViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<tr>		
			<TH><ct:FWLabel key="JSP_PF_DECISION_L_DECISION"/></TH>
			<TH><ct:FWLabel key="JSP_PF_DECISION_L_REQUDETAIL"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_PERDROIT"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_ETAT"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_TYPE"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_PREPPAR"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_DATEPREP"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_VALIDEPAR"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_DATEVALIDE"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_DECISION_L_NUMERO"/></TH>
	   </tr>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
		<%
		PFDecisionViewBean decision = (PFDecisionViewBean) viewBean.getEntity(i);
		//String actionDecisionDetail = decision.getDecisionAction();
		//String buildUrl = detailLink + actionDecisionDetail +".afficher&idDemandePc="+decision.getIdDemande()+"&idDecision="+decision.getId()+"&idDroit="+decision.getIdDroit()+"&idVersionDroit="+decision.getIdVersionDroit()+"&noVersion="+decision.getNoVersion();
		//String detailUrl = "parent.location.href='" + buildUrl +"'";
		String detailUrl = "parent.location.href='" + detailLink + decision.getId()+"&idDemande="+decision.getDecision().getDemande().getSimpleDemande().getId()+"'";
		PersonneEtendueComplexModel personne= decision.getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue();
		%>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
 		<TD class="mtd" nowrap width="20px">
			<ct:menuPopup menu="perseus-optionsDecision" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + decision.getId()%>">
		 		<ct:menuParam key="idDemande" value="<%=decision.getDecision().getDemande().getSimpleDemande().getId()%>"/>
		 		<ct:menuParam key="idDossier" value="<%=decision.getDecision().getDemande().getSimpleDemande().getIdDossier()%>"/>
				<!--  Si l'état pas pret on cache le menu -->
		 		<% if(!decision.isDecisionReadyForValidation()){
		 		%><ct:menuExcludeNode nodeId="VALIDATIONS"/><% 
		 		} 
		 		else{
		 		%><ct:menuParam key="csTypeDecision" value="<%=CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem() %>"/><%	
		 		}%>  
			</ct:menuPopup>
		</TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%=decision.getDetailAssure(objSession,personne)%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= decision.getDecision().getDemande().getSimpleDemande().getDateDebut() %> - <%= decision.getDecision().getDemande().getSimpleDemande().getDateFin() %></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= objSession.getCodeLibelle(decision.getDecision().getSimpleDecision().getCsEtat())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= objSession.getCodeLibelle(decision.getDecision().getSimpleDecision().getCsTypeDecision())%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" title="<%= viewBean.getCurrentUserFullName()%>"><%= decision.getDecision().getSimpleDecision().getUtilisateurPreparation()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getSimpleDecision().getDatePreparation()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>" title="<%= viewBean.getCurrentUserFullName()%>"><%= decision.getDecision().getSimpleDecision().getUtilisateurValidation()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getSimpleDecision().getDateValidation()%></TD>
		<TD class="mtd" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getSimpleDecision().getNumeroDecision()%></TD>
		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>