<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.osiris.db.services.*" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil.*" %>
<%
targetLocation = "location.href";
CARechercheMontantSectionManagerListViewBean viewBean = (CARechercheMontantSectionManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
detailLink ="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=";
String defaultListLink ="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=";
size = viewBean.size();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" align="left">Nummer</TH>
    <TH nowrap width="100" align="left">Kontoart</TH>
    <TH nowrap align="left">Konto</TH>
    <TH nowrap width="100" align="left">Sektion</TH>
    <TH nowrap width="100" align="left">Datum</TH>
    <TH align="left">Beschreibung</TH>
    <TH align="right">Rechnung / Belastung</TH>
    <TH align="right">Zahlung / Gutschrift</TH>
    <TH align="right">Saldo</TH>
    <TH align="left">Rechtspflege</TH>
    <% CASection _ligne=null; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	_ligne = (globaz.osiris.db.comptes.CASection) viewBean.getEntity(i);
	globaz.osiris.api.APICompteAnnexe compteAnnexeLigne = _ligne.getCompteAnnexe();
	actionDetail = "parent.location.href='"+defaultListLink+_ligne.getIdSection()+"'";
%>

<!--    <TD><A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=_ligne.getIdSection()%>" target="fr_main"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></A></TD>-->
    <TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ligne.getIdSection())%>">
		<ct:menuParam key="id" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="idSection" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="selectedId" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="noAffiliationId" value="<%=compteAnnexeLigne.getIdExterneRole()%>"/>
    </ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=_ligne.getCompteAnnexe().getIdExterneRole()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=_ligne.getCompteAnnexe().getRole().getDescription()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" ><%=_ligne.getCompteAnnexe().getDescription()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="70"><%=_ligne.getIdExterne()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" nowrap width="90"><%=_ligne.getDateSection()%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" >
    <% if (!globaz.jade.client.util.JadeStringUtil.isDecimalEmpty(_ligne.getIdRemarque())){ %>
    <img src="<%=request.getContextPath()%>/images/attach.png" style="float:right;" title="<%=_ligne.getRemarque().getTexte()%>">
    <% } %>
    <%=_ligne.getDescription()%>

    </TD>
<%	java.math.BigDecimal bSolde = new java.math.BigDecimal(_ligne.getSolde());
	java.math.BigDecimal bPmtCmp = new java.math.BigDecimal(_ligne.getPmtCmp());
%>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="110" align="right"><%=globaz.globall.util.JANumberFormatter.formatNoRound(bSolde.subtract(bPmtCmp).toString(),2)%></TD>
    <TD class="mtd" nowrap onClick="<%=actionDetail%>" width="110" align="right"><%=_ligne.getPmtCmpFormate()%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="110" align="right"><%=_ligne.getSoldeFormate()%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="200" align="left"><%=_ligne.getResumeContentieux()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>