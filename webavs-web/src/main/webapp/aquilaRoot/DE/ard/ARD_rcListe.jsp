<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.globall.util.*" %>
<%
	globaz.aquila.db.ard.COARDListViewBean viewBean = (globaz.aquila.db.ard.COARDListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.ard.ARD.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>&nbsp;</TH>
	<TH align="left">VGS Datum</TH>
	<TH align="right">Ursprünglicher Betrag</TH> 
	<TH align="left">Einspruchsdatum KVG</TH>
	<TH align="left">Einspruchsdatum EVG</TH>
	<TH align="left">Annullierungsdatum</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.aquila.db.ard.COARDViewBean line = (globaz.aquila.db.ard.COARDViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdDetailARD()+"&idCompteAnnexe="+line.getIdCompteAnnexe()+"'";
	%>
	<TD class="mtd" width="18">
    <ct:menuPopup menu="CO-OptionsARD" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=line.getIdDetailARD()%>"/>		
		<ct:menuParam key="idCompteAnnexe" value="<%=line.getIdCompteAnnexe()%>"/>
		
    </ct:menuPopup>
	</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateARD()%></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(line.getMontantARD(), 2)%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateRecours()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateRecoursTFA()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateAnnulation()%></TD>
	
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>