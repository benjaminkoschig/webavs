<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeDemande"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatPcfaccordee"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatDemande"%>
<%@page import="globaz.perseus.utils.PFGestionnaireHelper"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.pcfaccordee.PFPcfAccordeeViewBean"%>
<%@page import="globaz.perseus.vb.pcfaccordee.PFPcfAccordeeListViewBean"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%
	
	PFPcfAccordeeListViewBean viewBean=(PFPcfAccordeeListViewBean)request.getAttribute("viewBean");
   	size=viewBean.getSize();
   	
   	detailLink = "perseus?userAction=perseus.pcfaccordee.pcfAccordee.afficher&selectedId=";

%>
	    	
<style type="text/css">

	.tdItalique {
		font-style:italic;
		opacity : 0.5;
		filter : alpha(opacity=50); 
	}

</style>
	    	
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
		    <th>&nbsp;</th>
		    <th><ct:FWLabel key="JSP_PF_PCFACCORDEE_DETAILS_ASSURE"/></th>
		    <th data-g-amountformatter=" "><ct:FWLabel key="JSP_PF_PCFACCORDEE_MONTANT"/></th>
		    <th data-g-periodformatter=" "><ct:FWLabel key="JSP_PF_PCFACCORDEE_PERIODE"/></th>
		    <th><ct:FWLabel key="JSP_PF_PCFACCORDEE_ETAT"/></th>
		    <th><ct:FWLabel key="JSP_PF_PCFACCORDEE_RC_GESTIONNAIRE"/></th>
		    <th><ct:FWLabel key="JSP_PF_PCFACCORDEE_NO"/></th>
	    
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
        <%
            PFPcfAccordeeViewBean line = (PFPcfAccordeeViewBean)viewBean.getEntity(i); 
        	String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
        	boolean willBePaid = line.willBePaid(); 
        	String cssItal = "";
        	if (!willBePaid) {
        		cssItal = "tdItalique";
        	}
	    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionspcfaccordee" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
     			<ct:menuParam key="selectedId" value="<%=line.getPcfAccordee().getDemande().getSituationFamiliale().getId() %>"/>
	     		<ct:menuParam key="idDemande" value="<%=line.getPcfAccordee().getDemande().getId() %>"/>
	     		<ct:menuParam key="idDossier" value="<%=line.getPcfAccordee().getDemande().getDossier().getId() %>"/>
	     		<ct:menuParam key="idPcfAccordee" value="<%=line.getPcfAccordee().getId() %>"/>
	     		<ct:menuExcludeNode nodeId="DECAVECCALCUL"/>
	     		<ct:menuExcludeNode nodeId="PREPARTIONPROJETDECISION"/>
	     		<% if (CSTypeDemande.FOND_CANTONAL.getCodeSystem().equals(line.getPcfAccordee().getDemande().getSimpleDemande().getTypeDemande())) { %>
	     		<ct:menuExcludeNode nodeId="DECISIONS_PCFACCORDEES"/>
	     		<% } %>
	     		<% if (!CSEtatPcfaccordee.VALIDE.getCodeSystem().equals(line.getPcfAccordee().getSimplePCFAccordee().getCsEtat())) { %>
	     		<ct:menuExcludeNode nodeId="DECISION_SUPPRESSIONVOLONTAIRE"/>
	     		<% } %>
	     	</ct:menuPopup>
	    </TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=PFUserHelper.getDetailAssure(objSession,line.getPcfAccordee().getDemande().getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue())%></TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=line.getPcfAccordee().getSimplePCFAccordee().getMontant()%></TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=line.getPcfAccordee().getDemande().getSimpleDemande().getDateDebut() + "-" + line.getPcfAccordee().getDemande().getSimpleDemande().getDateFin()%></TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(line.getPcfAccordee().getSimplePCFAccordee().getCsEtat())%></TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=PFGestionnaireHelper.getDetailGestionnaire(objSession,line.getPcfAccordee().getDemande().getSimpleDemande().getIdGestionnaire())%></TD>
		<TD class="mtd <%=cssItal %>" nowrap onClick="<%=detailUrl%>"><%=line.getPcfAccordee().getId()%></TD>
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	