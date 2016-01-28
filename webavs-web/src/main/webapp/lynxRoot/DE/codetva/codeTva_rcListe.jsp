<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXCodeTvaListViewBean viewBean = (LXCodeTvaListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXCodeTvaViewBean codeTva = null;

detailLink ="lynx?userAction=lynx.codetva.codeTva.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	
<%@page import="globaz.lynx.db.codetva.LXCodeTvaListViewBean"%>
<%@page import="globaz.lynx.db.codetva.LXCodeTvaViewBean"%>
	<TH width="30">&nbsp;</TH>
	<TH width="300">Beschreibung</TH>
	<TH width="300">Satz</TH>
	<TH width="300">Beginndatum</TH>
	<TH width="300">Enddatum</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    codeTva = (LXCodeTvaViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+codeTva.getIdCodeTVA()+"'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-CodeTVA" label="<%=optionsPopupLabel%>" target="top.fr_main">
			<ct:menuParam key="selectedId" value="<%=codeTva.getIdCodeTVA()%>"/>
	    </ct:menuPopup>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=codeTva.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=codeTva.getTaux()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=codeTva.getDateDebut()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=codeTva.getDateFin()%>&nbsp;</td>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>