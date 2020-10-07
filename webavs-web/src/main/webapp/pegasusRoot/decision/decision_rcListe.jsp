<%-- Creator: SCE, 6.10 --%>
<script type="text/javascript">
	var conjointOpen = false;
	var hasConjoint;
	
</script>
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_PC_DECISION_L"
	PCDecisionListViewBean viewBean = (PCDecisionListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
	menuName = "pegasus-menuprincipal";	
	//detailLink = baseLink + "afficher";
	detailLink = "pegasus?userAction=";
	String warnTitle = objSession.getLabel("DECISION_ADAPTATION_WARNING_TITLE");
	String warnContent = objSession.getLabel("DECISION_ADAPTATION_WARNING_CONTENT");
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
$(function () {
	//gestion des décision d'adaptation , unnbind du click sur le detail
	//Ajout du message d'infos
	globazNotation.growl.init();
	
	$('.64042006, .reprise').prop('onclick',null);
	
	$('.64042006').click(function () {
		globazNotation.growl.warn("<%= warnTitle%>", "<%= warnContent%>");
	});
});
</script>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.decision.PCDecisionListViewBean"%>
<%@page import="globaz.pegasus.vb.decision.PCDecisionViewBean"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
		<tr>		
			<TH><ct:FWLabel key="JSP_PC_DECISION_L_DECISION"/></TH>
			<TH><ct:FWLabel key="JSP_PC_DECISION_L_REQUDETAIL"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_PERDROIT"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_DATE_DEC"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_ETAT"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_TYPE"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_MONTANT"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_PREPPAR"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_DATEPREP"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_VALIDEPAR"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_DATEVALIDE"/></TH>
		    <TH><ct:FWLabel key="JSP_PC_DECISION_L_NODECISION"/></TH>
	   </tr>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
		<%
		PCDecisionViewBean decision = (PCDecisionViewBean) viewBean.getEntity(i);
		String actionDecisionDetail = decision.getDecisionAction();
		String buildUrl = detailLink + actionDecisionDetail +".detail&idDemandePc="+decision.getIdDemande()+"&idDecision="+decision.getId()+"&idDroit="+decision.getIdDroit()+"&idVersionDroit="+decision.getIdVersionDroit()+"&noVersion="+decision.getNoVersion();
		String detailUrl = "parent.location.href='" + buildUrl +"'";
		PersonneEtendueComplexModel personne=decision.getDecision().getDecisionHeader().getPersonneEtendue();
		Boolean hasConjoint = decision.hasConjoint();
		%>
		
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		
		<%-- tpl:put name="zoneList" --%>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap width="20px">
			<ct:menuPopup menu="pegasus-optionsdecision" detailLabelId="MENU_OPTION_DECISION_DETAIL" detailLink="<%=buildUrl%>">
			<!--  Si l'état pas pret on cache le menu -->
		 	<% if(!decision.isDecisionReadyForValidation()){
		 	%><ct:menuExcludeNode nodeId="VALIDATIONS"/><% 
		 	} 
		 	else{
		 	%><ct:menuParam key="csTypeDecisionPrep" value="<%= IPCDecision.CS_TYPE_REFUS_SC %>"/><%	
		 	}%>
		 	<% if(!decision.hasDecompte()){%>
		 		<ct:menuExcludeNode nodeId="DECOMPTE"/>
		 		<ct:menuExcludeNode nodeId="DETTE_EN_COMPTAT"/> 

		 	<%}else%>
				<% if(!decision.hasCreancier()){%>
				<ct:menuExcludeNode nodeId="CREANCE_ACCORDEE"/>
				<%}%>
		 	<ct:menuExcludeNode nodeId="DECISION_DETAIL"/> 		 	
		 	<ct:menuExcludeNode nodeId="RETOUR_LIST"/>
		 	<ct:menuParam key="idVersionDroit" value="<%=decision.getIdVersionDroit()%>"/>
		 	<ct:menuParam key="idDroit" value="<%=decision.getIdDroit()%>"/>
		 	<ct:menuParam key="noVersion" value="<%=decision.getNoVersion()%>"/>
		 	<ct:menuParam key="idDemandePc" value="<%=decision.getIdDemande()%>"/>
		 	<ct:menuParam key="idDecision" value="<%=decision.getId()%>"/>
		 	<ct:menuParam key="dateDec" value="<%=decision.getDateDecision()%>"/>
			</ct:menuPopup> 
		</TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %>" nowrap onclick="<%=detailUrl %>">
		
			<div style="position:relative;">
				<div>
					<%=PCUserHelper.getDetailAssure(objSession,personne)%>
				</div>
				<span style="position:absolute; top:0; right:0"
					  data-g-note="idExterne:<%=decision.getId()%>, 
								   tableSource: PCDECHEA, inList: true">
				</span>
			</div>
		</TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>" data-g-periodformatter=" "><%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision() %> - <%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision() %></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDateDecision()%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= objSession.getCodeLibelle(decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= objSession.getCodeLibelle(decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision())%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>" data-g-amountformatter=" "><%=decision.getMontantPC() %></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>" title="<%= viewBean.getCurrentUserFullName()%>"><%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getPreparationPar()%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDatePreparation()%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= JadeStringUtil.isBlankOrZero(decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getValidationPar())?"&nbsp":decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getValidationPar()%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= JadeStringUtil.isBlankOrZero(decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDateValidation())?"&nbsp":decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getDateValidation()%></TD>
		<TD class="mtd dec<%=decision.getId() %> <%=decision.getDecisionCSType() %> <%= decision. isDacComingFromRepriseClassCss() %>" nowrap onclick="<%=detailUrl%>"><%= decision.getDecision().getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></TD>
		<script type="text/javascript">
			
			hasConjoint = <%= hasConjoint %>;
			if(hasConjoint&&!conjointOpen){
				$('.dec<%= decision.getId()%>').css('border-top','2px solid blue');
				conjointOpen = true;
			}
			else if(hasConjoint&&conjointOpen){
				$('.dec<%= decision.getId()%>').css('border-bottom','2px solid blue');
				conjointOpen = false;
			}
		</script>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>
