 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
  <%@ page import="globaz.globall.util.*" %>
<%
globaz.osiris.db.comptes.CACompteurManagerListViewBean viewBean = (globaz.osiris.db.comptes.CACompteurManagerListViewBean)session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_COMPTEUR_MANAGER);
globaz.osiris.db.comptes.CACompteur _compteur = null ; 
size = viewBean.size();
%>  

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
    <TH align="left">Jahr</TH>
    <TH nowrap width="90" align="left">Zähler</TH>
    <TH width="332" align="left">Beschreibung</TH>
    <TH width="110" align="right">Lohnsumme</TH>
    <TH width="110" align="right">Beiträge</TH>
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> 
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
    <%	_compteur = (globaz.osiris.db.comptes.CACompteur) viewBean.getEntity(i); %>
    <TD class="mtd" width="70" align="left"><%=_compteur.getAnnee()%></TD>
    <TD class="mtd" nowrap width="90" align="left">
      <%if (_compteur.getRubrique() != null){%>
      <%=_compteur.getRubrique().getIdExterne()%>
      <%}%>
    </TD>
    <TD class="mtd" width="332" align="left">
      <%if (_compteur.getRubrique() != null){%>
      <%=_compteur.getRubrique().getDescription()%>
      <%}%>
    </TD>
    <TD class="mtd" width="110" align="right"><%=JANumberFormatter.formatNoRound(_compteur.getCumulMasse())%></TD>
    <TD class="mtd" width="110" align="right"><%=JANumberFormatter.formatNoRound(_compteur.getCumulCotisation())%></TD>
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