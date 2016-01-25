<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="globaz.perseus.vb.demande.PFDemandeListViewBean"%>
<%@page import="globaz.perseus.vb.demande.PFDemandeViewBean"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	//Les labels de cette page commence par le préfix "JSP_PF_DEM_L"
	PFDemandeListViewBean viewBean = (PFDemandeListViewBean) request.getAttribute("viewBean");
	size=viewBean.getSize();
	detailLink = "perseus?userAction=perseus.demande.demande.afficher&selectedId=";
	menuName = "perseus-menuprincipal";
%>

<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
		<TH>&nbsp;</TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_DETAIL_ASSURE"/></TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_TYPE"/></TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_DATE_DEPOT"/></TH>
		<TH data-g-periodformatter=" "><ct:FWLabel key="JSP_PF_DEM_L_PERIODE"/></TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_ETAT"/></TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_GEST"/></TH>
		<TH><ct:FWLabel key="JSP_PF_DEM_L_NO"/></TH> 
   	
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		<%
			PFDemandeViewBean line = (PFDemandeViewBean) viewBean.getEntity(i);
			
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"&idDossier="+line.getDemande().getDossier().getId()+"'";
		%>
		
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionsdemandes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="selectedId" value="<%=line.getDemande().getSituationFamiliale().getId()%>"/>
     			<ct:menuParam key="idDemande" value="<%=line.getDemande().getId()%>"/>
     			<ct:menuParam key="idDossier" value="<%=line.getDemande().getDossier().getId() %>"/>
      			<% if (CSTypeDemande.PC_AVS_AI.getCodeSystem().equals(line.getDemande().getSimpleDemande().getTypeDemande())) { %> 
     				<ct:menuExcludeNode nodeId="CALCULERPCFA"/>
     				<ct:menuExcludeNode nodeId="VOIRPCFA"/>
     				<ct:menuExcludeNode nodeId="DECSANSCALCUL"/>
     				<ct:menuExcludeNode nodeId="CREANCIER"/>
     				<ct:menuExcludeNode nodeId="DECISIONS_DEMANDE"/>
     			<% } else if (CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(line.getDemande().getSimpleDemande().getTypeDemande())) { %>
     				<ct:menuExcludeNode nodeId="DECSANSCALCUL"/>
     				<ct:menuExcludeNode nodeId="CREANCIER"/>
     				<ct:menuExcludeNode nodeId="DECISIONS_DEMANDE"/>
     				<ct:menuExcludeNode nodeId="DEMANDE_PC_AVS_AI"/>
     			<% } else { %>
     				<ct:menuExcludeNode nodeId="DEMANDE_PC_AVS_AI"/>
     			<% } %>
     			<% if (CSEtatDemande.VALIDE.getCodeSystem().equals(line.getDemande().getSimpleDemande().getCsEtatDemande())) { %> 
     				<ct:menuExcludeNode nodeId="CALCULERPCFA"/>
     				<ct:menuExcludeNode nodeId="DECSANSCALCUL"/>
      				<ct:menuExcludeNode nodeId="DEMANDE_PC_AVS_AI"/>
     			<% } else { %>
      				<ct:menuExcludeNode nodeId="OUVERTUREQD"/>
     			<% } %>
     			<% if (line.getDemande().getSimpleDemande().getCalculable() && !line.getDemande().getSimpleDemande().getRefusForce() && !line.getDemande().getSimpleDemande().getNonEntreeEnMatiere()) { %> 
     				<ct:menuExcludeNode nodeId="DECSANSCALCUL"/>
     			<% } else { %>
     				<ct:menuExcludeNode nodeId="CALCULERPCFA"/>
     				<ct:menuExcludeNode nodeId="CREANCIER"/>
     			<% } %>
     		</ct:menuPopup>
     	</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFUserHelper.getDetailAssure(objSession,line.getDemande().getDossier().getDemandePrestation().getPersonneEtendue())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getDemande().getSimpleDemande().getTypeDemande())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDemande().getSimpleDemande().getDateDepot()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDemande().getSimpleDemande().getDateDebut() + " - " + line.getDemande().getSimpleDemande().getDateFin() %></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getDemande().getSimpleDemande().getCsEtatDemande())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=PFGestionnaireHelper.getDetailGestionnaire(objSession,line.getDemande().getSimpleDemande().getIdGestionnaire())%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>

		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	