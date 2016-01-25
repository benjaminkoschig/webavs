
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
  <%@ page import="java.math.*" %>
<%
targetLocation = "location.href";
globaz.osiris.db.comptes.CASoldeCompteAnnexeCCManagerListViewBean viewBean = (globaz.osiris.db.comptes.CASoldeCompteAnnexeCCManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_COMPTEANNEXE);
size = viewBean.getSize();

%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH width="150" align="left">Compte-courant</TH>
    <TH nowrap align="left">Description</TH>
    <TH width="226" align="right">Solde</TH>
    <% BigDecimal montantTotal = new BigDecimal(0); %>
<%    globaz.osiris.db.comptes.CASoldeCompteAnnexeCC _compteCourant=null;%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%	_compteCourant = (globaz.osiris.db.comptes.CASoldeCompteAnnexeCC) viewBean.getEntity(i); %>
    <TD class="mtd" width="150" align="left"><%=_compteCourant.getCompteCourant().getRubrique().getIdExterne()%></TD>
    <TD class="mtd" nowrap align="left"><%=_compteCourant.getCompteCourant().getRubrique().getDescription()%></TD>
    <TD class="mtd" width="226" align="right"><%=globaz.globall.util.JANumberFormatter.formatNoRound(String.valueOf(_compteCourant.getSolde()))%></TD>
    <% montantTotal = montantTotal.add(new BigDecimal(_compteCourant.getSolde()));%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
  <TR align="center" class="title">
    <TD width="150" align="left"></TD>
    <TD nowrap align="left">*Total</TD>
    <TD width="226" align="right"><%=globaz.globall.util.JANumberFormatter.formatNoRound(String.valueOf(montantTotal))%></TD>
  </TR>
	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
	</ct:menuChange>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>