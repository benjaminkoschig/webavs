<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteListViewBean" %>
<%@ page import="globaz.osiris.db.suiviprocedure.CAFailliteViewBean" %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%
	CAFailliteListViewBean viewBean = (CAFailliteListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();

	CAFailliteViewBean faillite = null;
	detailLink ="osiris?userAction=osiris.suiviprocedure.faillite.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th colspan="2" align="left">Date faillite</th>
    <th align="left">Production</th>
    <th align="left">Production d&eacute;finitive</th>
    <th align="left">Annulation production</th>
    <th align="left">R&eacute;vocation &#47; r&eacute;tractation</th>
    <th align="left">Suspension faillite</th>
    <th align="left">Etat de collocation</th>
    <th align="left">Modification &eacute;tat collocation</th>
    <th align="left">Clôture faillite</th>
    <th align="left">Montant de production</th>
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
    <td class="mtd" nowrap onClick="<%=actionDetail%>" width="20"><%=JANumberFormatter.formatNoRound(faillite.getMontantProduction(), 2)%>&nbsp;</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>