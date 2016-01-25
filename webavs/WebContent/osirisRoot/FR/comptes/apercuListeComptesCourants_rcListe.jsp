<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="java.math.BigDecimal"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%
	globaz.osiris.db.comptes.CACompteCourantManagerListViewBean viewBean = (globaz.osiris.db.comptes.CACompteCourantManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
	size = viewBean.size();
	session.setAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT,viewBean);
	globaz.osiris.db.comptes.CACompteCourantLienCG _compteCourant = null;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th colspan="2" width="150" align="left">Compte-courant</th>
    <th nowrap align="left">Description</th>
    <th nowrap>Priorit&eacute;</th>
    <th nowrap>Ventilation</th>
    <th width="226" align="right">Solde</th>
    <th width="226" align="right">Solde CG</th>
    <th width="226" align="right">Difference</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
    <%
    	_compteCourant = (globaz.osiris.db.comptes.CACompteCourantLienCG) viewBean.getEntity(i);
    	actionDetail = targetLocation  + "='" + "osiris?userAction=osiris.comptes.apercuListeComptesCourants.afficher&selectedId=" +_compteCourant.getIdCompteCourant() + "'";
    	detailLink ="osiris?userAction=osiris.comptes.apercuListeComptesCourants.afficher&selectedId=";
    %>
    <td class="mtd" width="16">
    	<ct:menuPopup menu="CA-CompteCourant" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink='<%=(detailLink + _compteCourant.getIdCompteCourant())%>'>
			<ct:menuParam key="forIdExterne" value="<%=_compteCourant.getIdExterne()%>"/>
	    </ct:menuPopup>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" width="150"><%=_compteCourant.getRubrique().getIdExterne()%></td>
    <td class="mtd" onClick="<%=actionDetail%>" nowrap><%=_compteCourant.getRubrique().getDescription()%></td>
    <td class="mtd" onClick="<%=actionDetail%>" nowrap align="center">
      <%if (!_compteCourant.getPriorite().equalsIgnoreCase("0"))%>
      <%=_compteCourant.getPriorite()%></td>
    <td class="mtd" onClick="<%=actionDetail%>" nowrap align="center">
      <%if (_compteCourant.getAccepterVentilation().booleanValue()){%>
      <img src="<%=request.getContextPath()%>/images/select.gif" height="20"/>
      <%} else {%>
      <img src="<%=request.getContextPath()%>/images/blank.gif" height="20"/>
      <%}%>
    </td>
    <%
    	BigDecimal solde = new BigDecimal(_compteCourant.getSolde());
    	BigDecimal soldeCG = new BigDecimal(_compteCourant.getSoldeCG());
    	BigDecimal diff = solde.subtract(soldeCG);
    %>
    <td class="mtd" onClick="<%=actionDetail%>" width="226" align="right"><%=JANumberFormatter.formatNoRound(_compteCourant.getSolde())%></td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(_compteCourant.getSoldeCG())%></td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(diff.toString())%></td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>