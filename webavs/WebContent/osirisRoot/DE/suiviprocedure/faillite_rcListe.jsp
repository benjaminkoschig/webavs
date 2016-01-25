<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteListViewBean" %>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteViewBean" %>
<%
	CAFailliteListViewBean viewBean = (CAFailliteListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();

	CAFailliteViewBean faillite = null;
	detailLink ="osiris?userAction=osiris.suiviprocedure.faillite.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th colspan="2" align="left">Konkursdatum</th>
    <th align="left">Forderungseingabe</th>
    <th align="left">Definitive Forderungseingabe</th>
    <th align="left">Annullierung Forderungseingabe</th>
    <th align="left">Abberufung &#47; Widerruf</th>
    <th align="left">Einstellung des Konkursverfahrens</th>
    <th align="left">Kollokationsplan</th>
    <th align="left">Änderung Kollokationsplan</th>
    <th align="left">Schluss des Konkursverfahrens</th>
    <th align="left">Betrag der Ergebnisses</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%
    faillite = (CAFailliteViewBean) viewBean.get(i);
    actionDetail = "parent.location.href='"+detailLink+faillite.getIdFaillite()+"'";
%>
    <td class="mtd" width="16" >
		<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + faillite.getIdFaillite()%>">
		<ct:menuParam key="selectedId" value="<%=faillite.getIdFaillite()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateFaillite()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateProduction()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateProductionDefinitive()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateAnnulationProduction()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateRevocation()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateSuspensionFaillite()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateEtatCollocation()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateModificationEtatCollocation()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getDateClotureFaillite()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=faillite.getMontantProduction()%>&nbsp;</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>