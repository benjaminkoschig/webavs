<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.osiris.db.historique.CAHistoriqueBulletinSoldeListViewBean viewBean = (globaz.osiris.db.historique.CAHistoriqueBulletinSoldeListViewBean) session.getAttribute("listViewBean");
	size = viewBean.size();

	globaz.osiris.db.historique.CAHistoriqueBulletinSoldeViewBean historiqueBulletinSolde = null;
	detailLink ="osiris?userAction=osiris.historique.historiqueBulletinSolde.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    <th colspan="2" align="left">Durchführung Datum</th>
    <th align="left">Saldo</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%
    historiqueBulletinSolde = (globaz.osiris.db.historique.CAHistoriqueBulletinSoldeViewBean) viewBean.get(i);
    actionDetail = "parent.location.href='"+detailLink+historiqueBulletinSolde.getIdHistorique()+"'";
%>
    <td class="mtd" width="16" >
		<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + historiqueBulletinSolde.getIdHistorique()%>">
			<ct:menuParam key="selectedId" value="<%=historiqueBulletinSolde.getIdHistorique()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=historiqueBulletinSolde.getDateHistorique()%>&nbsp;</td>
    <td class="mtd" nowrap onClick="<%=actionDetail%>"><%=historiqueBulletinSolde.getSoldeFormatter()%>&nbsp;</td>

    
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>