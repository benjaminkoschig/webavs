<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.lynx.db.societesdebitrice.LXSocieteDebitriceListViewBean viewBean = (globaz.lynx.db.societesdebitrice.LXSocieteDebitriceListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean societe = null;

detailLink ="lynx?userAction=lynx.societesdebitrice.societeDebitrice.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
 
    <TH width="30">&nbsp;</TH>
	<TH width="150">Num&eacute;ro</TH>
	<TH width="400">Nom</TH>
	<TH width="400">Adresse postale</TH>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <% 
    societe = (globaz.lynx.db.societesdebitrice.LXSocieteDebitriceViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + societe.getIdSociete() + "'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-Societe" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=societe.getIdSociete()%>"/>
			<ct:menuParam key="idSociete" value="<%=societe.getIdSociete()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=societe.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=societe.getNom()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=societe.getAdresse()%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>