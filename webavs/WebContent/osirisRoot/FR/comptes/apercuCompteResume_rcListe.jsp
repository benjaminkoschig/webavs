
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
targetLocation = "location.href";
globaz.osiris.db.contentieux.CAExtraitCompteListViewBean viewBean = (globaz.osiris.db.contentieux.CAExtraitCompteListViewBean)session.getAttribute(globaz.osiris.servlet.action.compte.CAComptesAnnexesAction.VBL_COMPTERESUME);
size = viewBean.size();
wantPagination=false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	 	<script type="text/javascript">
		 	notationManager.b_stop = true;
	 	</script>
	<TH align="left">Date comptable</TH>
    <TH align="left">Date valeur</TH>
    <TH nowrap width="90" align="left">N&deg; section</TH>
    <TH width="332" align="left">Description</TH>
    <TH width="110" align="right">Hors compte annexe</TH>
    <TH width="110" align="right">Doit</TH>
    <TH width="110" align="right">Avoir</TH>
    <TH width="200" align="right">Solde</TH>
    <TH align="left">Prov. pmt</TH>
	<%globaz.framework.util.FWCurrency soldeCumule = new globaz.framework.util.FWCurrency();	%>
	<%	globaz.osiris.db.contentieux.CALigneExtraitCompte _ligne = null ; %>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%	_ligne = (globaz.osiris.db.contentieux.CALigneExtraitCompte) viewBean.getLigneExtraitCompte().get(i); %>
	<%globaz.framework.util.FWCurrency _doitAvoir = new globaz.framework.util.FWCurrency();
		_doitAvoir.add(_ligne.getTotal());
		globaz.framework.util.FWCurrency _horsCompteAnnexe = new globaz.framework.util.FWCurrency();
		_horsCompteAnnexe.add(_ligne.getHorsCompteAnnexe());
	%>
	<TD class="mtd"><%=_ligne.getDateJournal()%></TD>
    <TD class="mtd"><%=_ligne.getDate()%></TD>
    <TD class="mtd" nowrap width="90"><%=_ligne.getIdExterne()%></TD>
    <TD class="mtd" width="332"><%=_ligne.getDescription()%></TD>
    <TD class="mtd" width="110" align="right"><%if (!_horsCompteAnnexe.isZero()) { %><%=_ligne.getHorsCompteAnnexe() %><% } %>&nbsp;</TD>
    <TD class="mtd" width="110" align="right"><%if (_doitAvoir.isPositive())%><%=_doitAvoir.toStringFormat()%>&nbsp;</TD>
    <TD class="mtd" width="110" align="right"><%if (_doitAvoir.isNegative()){ _doitAvoir.negate();%><%=_doitAvoir.toStringFormat()%><%}%>&nbsp;</TD>
	<%soldeCumule.add(_ligne.getTotal());%>
    <TD class="mtd" width="200" align="right"><%=soldeCumule.toStringFormat()%></TD>
    <TD class="mtd"><%=_ligne.getProvenancePmt()%>&nbsp;</TD>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<ct:menuChange displayId="options" menuId="CA-ApercuParSectionDossier" showTab="options">
		<ct:menuSetAllParams key="id" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdCompteAnnexe()%>"/>
		<ct:menuSetAllParams key="idCompteAnnexe" value="<%=viewBean.getIdCompteAnnexe()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>