<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="globaz.osiris.db.interets.*" %>
<%@ page import="globaz.osiris.translation.CACodeSystem" %>
<%

CAApercuInteretMoratoireListViewBean viewBean = (CAApercuInteretMoratoireListViewBean) session.getAttribute (globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
size = viewBean.getSize();
detailLink = "osiris?userAction=osiris.interets.interetMoratoire.afficher&domaine=" + viewBean.getForDomaine() + "&selectedId=";
globaz.osiris.db.interets.CAApercuInteretMoratoire _line = null;

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

    <TH align="center">Datum</TH>
    <TH align="center">Debitor</TH>
    <TH align="center">Abrechnung</TH>
    <TH align="center">Zinsenart</TH>
    <TH align="center">Zinsen</TH>
    <TH align="center">Status</TH>
   	<TH align="center">Auswählen</TH>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    	_line = (globaz.osiris.db.interets.CAApercuInteretMoratoireViewBean) viewBean.getEntity(i);
    
    	actionDetail = "parent.location.href='"+detailLink+_line.getIdInteretMoratoire()+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + viewBean.getIdTiers(i)+"'";
    %>
    <TD class="mtd" width="70" valign="top" onClick="<%=actionDetail%>"><%=_line.getDateCalcul()%></TD>
    <TD class="mtd" width="300" onClick="<%=actionDetail%>">
	    <%= CACodeSystem.getLibelle(session,_line.getEnteteFacture().getIdRole())%> <%=_line.getIdExterneRole()%><br>
	    <%=_line.getDescription()%>
    </TD>
    <TD class="mtd" valign="top" onClick="<%=actionDetail%>"><%=viewBean.isDomaineCA()?_line.getIdExterne() + _line.getLibelleDescription():_line.getEnteteFacture().getDescriptionDecompte()%></TD>
    <TD class="mtd" valign="top" onClick="<%=actionDetail%>"><%=globaz.osiris.translation.CACodeSystem.getLibelle(session, _line.getIdGenreInteret())%></TD>
    <TD class="mtd" valign="top" align="right" onClick="<%=actionDetail%>"><%=_line.getTotalMontantInteret()%></TD>
    <TD class="mtd" valign="top" onClick="<%=actionDetail%>"> <%=globaz.osiris.translation.CACodeSystem.getLibelle(session, _line.getStatus())%></TD>
	<TD class="mtd" valign="top" align="center"> <input type="checkbox" name="chkTraiter"  value="<%=_line.getIdInteretMoratoire()%>" <%=!CAInteretMoratoire.CS_A_CONTROLER.equals(_line.getStatus())?"disabled":""%>></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>