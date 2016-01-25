<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.jade.context.JadeThreadContext"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	    <%
	    	PFDossierListViewBean viewBean=(PFDossierListViewBean)request.getAttribute("viewBean");
	    	size=viewBean.getSize();
	    	
	    	detailLink = "perseus?userAction=perseus.dossier.dossier.afficher&selectedId=";
	    	
	    	menuName = "perseus-menuprincipal";	    	
	    	
	    	String service = BSessionUtil.getSessionFromThreadContext().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
	    %>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
<%@page import="globaz.perseus.vb.dossier.PFDossierViewBean"%>
<%@page import="globaz.perseus.vb.dossier.PFDossierListViewBean"%>
<TH colspan="2"><ct:FWLabel key="JSP_PF_DOS_L_DOSSIER"/></TH>
<TH colspan="2"><ct:FWLabel key="JSP_PF_DOS_L_DETAIL_ASSURE"/></TH>
   		<TH><ct:FWLabel key="JSP_PF_DOS_L_DATEREV"/></TH>
   		<TH><ct:FWLabel key="JSP_PF_DOS_L_GEST"/></TH>
   		<TH><ct:FWLabel key="JSP_PF_DOS_L_NO"/></TH>   	
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%PFDossierViewBean line = (PFDossierViewBean)viewBean.getEntity(i); 
    
    String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
    
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
		<TD class="mtd" nowrap width="20px">
	     	<ct:menuPopup menu="perseus-optionsdossiers" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
	     			<ct:menuParam key="idDossier" value="<%=line.getId()%>"/>  
	     			<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>  
		 	</ct:menuPopup>
     	</TD>
     	
		<TD class="mtd" width="20px" nowrap onClick="<%=actionDetail%>">
			<% if (JadeStringUtil.isEmpty(line.getDossier().getDossier().getAnnoncesChangements())) { %>
				&nbsp;
			<% } else { %>
				<a href="#" title="<%=line.getDossier().getDossier().getAnnoncesChangements() %>">A</a>
			<% } %>
		</TD>
     	
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDetailAssure()%></TD>
		
		<TD class="mtd">
			<a href="#" onclick="window.open('<%=servletContext%><%=("/perseus")%>?userAction=perseus.dossier.dossier.actionAfficherDossierGed&amp;noAVSId=<%=line.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()%>&amp;idTiersExtraFolder=<%=line.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getIdTiers()%>&amp;serviceNameId=<%=service%>')" >GED</a>			
		</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDossier().getDossier().getDateRevision()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getDetailGestionnaire()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getId()%></TD>		
		
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	