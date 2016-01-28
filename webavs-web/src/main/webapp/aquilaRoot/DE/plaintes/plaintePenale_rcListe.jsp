<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.aquila.db.plaintes.COPlaintePenaleListViewBean viewBean = (globaz.aquila.db.plaintes.COPlaintePenaleListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "aquila?userAction=aquila.plaintes.plaintePenale.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<TH>&nbsp;</TH>
	<TH>Klagedatum</TH>
	<TH>Beschreibung</TH> 
	<TH>Typ</TH>
	<TH>Grund</TH>
	<TH>Vom</TH>
	<TH>bis</TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		globaz.aquila.db.plaintes.COPlaintePenaleViewBean line = (globaz.aquila.db.plaintes.COPlaintePenaleViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPlainte()+"&idCompteAuxiliaire="+line.getIdCompteAuxiliaire()+"'";
	%>
	<TD class="mtd" width="18">
    <ct:menuPopup menu="CO-OptionsPlaintesPenales" label="<%=optionsPopupLabel%>" target="top.fr_main">
		<ct:menuParam key="selectedId" value="<%=line.getIdPlainte()%>"/>
		<ct:menuParam key="idCompteAuxiliaire" value="<%=line.getIdCompteAuxiliaire()%>"/>
    </ct:menuPopup>
	</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDatePlainte()%></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLabelDescriptionPlainte()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLabelTypePlainte()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLabelMotifPlainte()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeDu()%></TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getPeriodeAu()%></TD>
	
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>