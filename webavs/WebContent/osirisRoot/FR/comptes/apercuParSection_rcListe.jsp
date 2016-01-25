<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.osiris.api.APICompteAnnexe"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.comptes.*" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil.*" %>
<%
targetLocation = "location.href";
CASectionManagerListViewBean viewBean = (CASectionManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_SECTION_MANAGER);
detailLink ="osiris?userAction=osiris.comptes.apercuParSection.afficher&id=";
String defaultListLink ="osiris?userAction=osiris.comptes.apercuSectionDetaille.chercher&id=";

size = viewBean.size();
String idRole="";
String idExterneRole ="";
if (size > 0) {
	APICompteAnnexe compteAnnexe = ((globaz.osiris.db.comptes.CASection) viewBean.getFirstEntity()).getCompteAnnexe();
	idRole = compteAnnexe.getIdRole();
	idExterneRole = compteAnnexe.getIdExterneRole();
}
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH colspan="2" align="left">Num&eacute;ro</TH>
    <TH nowrap width="100" align="left">Date</TH>
    <TH align="left">Description</TH>
    <TH align="right">Base</TH>
    <TH align="right">Pmt / comp.</TH>
    <TH align="right">Solde</TH>
    <TH align="right" width="20">&nbsp;</TH>
    <TH align="left">Contentieux</TH>
    <% CASection _ligne=null; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	_ligne = (globaz.osiris.db.comptes.CASection) viewBean.getEntity(i);
	
	actionDetail = "parent.location.href='"+defaultListLink+_ligne.getIdSection()+"'";
%>

<!--    <TD><A href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.apercuParSection.afficher&id=<%=_ligne.getIdSection()%>" target="fr_main"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" border="0"></A></TD>-->
    <TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-DetailSection" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink+_ligne.getIdSection())%>">
		<ct:menuParam key="id" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="idSection" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="selectedId" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="noAffiliationId" value="<%=idExterneRole%>"/>
		<ct:menuParam key="forIdSection" value="<%=_ligne.getIdSection()%>"/>
		<ct:menuParam key="idCompteAnnexe" value="<%=_ligne.getIdCompteAnnexe()%>"/>
		<ct:menuParam key="idPlanRecouvrement" value="<%=_ligne.getIdPlanRecouvrement()%>"/>
		<ct:menuParam key="idRole" value="<%=idRole%>"/>


		<% if (globaz.jade.client.util.JadeStringUtil.isIntegerEmpty(_ligne.getIdPlanRecouvrement())) {%>
		<ct:menuExcludeNode nodeId="echeances_plan"/>
		<% } %>
    </ct:menuPopup>
    </TD>
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
	<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="20" align="right"><%if (_ligne.getAttenteLSVDD().booleanValue()){%> <IMG src="<%=servletContext%>/images/pending.gif" title="Attente LSV/DD"><%} else {%>&nbsp;<%}%></TD>
	<TD class="mtd" nowrap onClick="<%=actionDetail%>" width="200" align="left"><%=_ligne.getResumeContentieux()%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> 
	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getForIdCompteAnnexe()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>