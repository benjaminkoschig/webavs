
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
    AFQuittanceListViewBean viewBean = (AFQuittanceListViewBean)request.getAttribute ("viewBean");
    size = viewBean.size ();
    detailLink ="naos?userAction=naos.beneficiairepc.quittance.afficher&selectedId=";
    session.setAttribute("listViewBean",viewBean);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
     <%@page import="globaz.naos.db.beneficiairepc.AFQuittanceListViewBean"%>
    <%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
    <%@page import="globaz.naos.db.beneficiairepc.AFQuittance"%>
<%@page import="globaz.phenix.toolbox.CPToolBox"%>
<TH width="">N° journal</TH>
    <TH width="">N° affilié</TH>
    <TH width="">N° AVS</TH>
    <TH width="">Période</TH>
    <TH width="">Total</TH>
    <TH width="">Etat</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%
    AFQuittance lineBean  = (AFQuittance)viewBean.getEntity(i);
	actionDetail = "parent.location.href='"+detailLink+lineBean.getIdQuittance() + "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + lineBean.getIdTiers() +"'";
	String tmp = detailLink + lineBean.getIdQuittance();
	%>
    <TD class="mtd" onclick="<%=actionDetail%>" width="15%" align="center"><%=lineBean.getIdJournal()%></TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="18%" align="center"><%=lineBean.getNumAffilieBeneficiaire()%></TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="15%" align="center"><%=lineBean.getNumAvs()%></TD>
    <TD class="mtd" onClick="<%=actionDetail%>" width="30%" align="center"><%=lineBean.getMois()%></TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="9%" align="right"><%=lineBean.getTotalVerse()%></TD>
    <TD class="mtd" onclick="<%=actionDetail%>" width="*" align="center"><%=lineBean.getEtatQuittanceLibelle()%></TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
		<%-- tpl:put name="zoneTableFooter" --%>  
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>