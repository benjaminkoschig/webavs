<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.fact.AFFactListViewBean viewBean = (globaz.naos.db.fact.AFFactListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.fact.factCotisation.afficher&selectedId=";
%>                
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
   
	<TH class="mtd" width="30" >&nbsp;</TH>
	<TH align="center" width="100">Beginn</TH>
	<TH align="center" width="100">Ende</TH>
	<TH align="right" width="60">Lohnsumme</TH>
	<TH align="right" width="60">Betrag</TH>
	<TH align="right" width="150"> Rechnungsart</TH>
	<TH align="left" width="60">Stornierung</TH>
	<TH align="left" width="150">Journal-Nr.</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		actionDetail = targetLocation + "='" + detailLink + viewBean.getFacturationId(i) + "'";
	%>
	<TD class="mtd" width="16" >
		<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getFacturationId(i)%>"/>
	</TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="center"><%=viewBean.getDateDebut(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getDateFin(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getMassePeriodiciteNouveau(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getMontant(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getTypeFacturation(i))%></TD>
	<% 
		if (viewBean.getExtourner(i).booleanValue()){
	%>
		<TD class="mtd" onClick="<%=actionDetail%>" align="center"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
	<% } else { %>
		<TD class="mtd" onClick="<%=actionDetail%>" align="center"><%=""%></TD>
	<% } %>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getPassageId(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>