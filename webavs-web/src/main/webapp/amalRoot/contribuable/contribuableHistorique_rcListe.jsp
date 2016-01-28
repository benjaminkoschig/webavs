<%@page import="globaz.amal.vb.contribuable.AMContribuableHistoriqueListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	    <%
	    	AMContribuableHistoriqueListViewBean viewBean=(AMContribuableHistoriqueListViewBean)request.getAttribute("viewBean");
    	
   	    	size=viewBean.getSize();
   	    	detailLink = "amal?userAction=amal.contribuable.contribuable.afficher&selectedId=";
	    	    	    	
   	    	menuName = "amal-menuprincipal";	    	    
	    %>
<%-- /tpl:insert --%>
<%@ include file="/theme/list/javascripts.jspf" %>	   	    
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
<%@page import="globaz.amal.vb.contribuable.AMContribuableHistoriqueViewBean"%>

		<TH><ct:FWLabel key="JSP_AM_CON_L_DOSSIER"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_L_NNSS"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_R_NO_CONTRIBUABLE"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_L_NOMPRENOM"/></TH>
   		<TH><ct:FWLabel key="JSP_AM_CON_L_DATENAISSANCE"/></TH>
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	AMContribuableHistoriqueViewBean line = (AMContribuableHistoriqueViewBean)viewBean.getEntity(i);
        
        String detailUrl = "parent.location.href='" + detailLink + line.getContribuableHistoriqueRCListe().getId()+"&fromHisto=1'";
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
		
    	
		<TD class="mtd" width="20px">
				&nbsp;
		</TD>
		
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableHistoriqueRCListe().getNnssCtbInfo()%>&nbsp;</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableHistoriqueRCListe().getNumContribuableCtbInfo()%>&nbsp;</TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableHistoriqueRCListe().getNomCtbInfo()%>&nbsp;<%=line.getContribuableHistoriqueRCListe().getPrenomCtbInfo()%></TD>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=line.getContribuableHistoriqueRCListe().getDateNaissanceCtbInfo()%>&nbsp;</TD>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>