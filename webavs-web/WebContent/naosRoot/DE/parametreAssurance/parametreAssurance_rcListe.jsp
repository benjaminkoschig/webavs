<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<%
	detailLink = "naos?userAction=naos.parametreAssurance.parametreAssurance.afficher&selectedId=";
	globaz.naos.db.parametreAssurance.AFParametreAssuranceListViewBean viewBean = (globaz.naos.db.parametreAssurance.AFParametreAssuranceListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%> 
	<%
		
	%>
	<TH width="30">&nbsp;</TH>
	<TH align="left" width="250">Art</TH>
	<TH align="center" width="120">Begindatum</TH>
	<!--TH align="center" width="120">Date de fin</TH-->
	<TH align="center" width="70">Geschlecht</TH>
	<TH align="center" width="150">Wert</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%> 
	<% 	
		actionDetail = targetLocation + "='" + detailLink + viewBean.getParametreAssuranceId(i) + "'";
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getParametreAssuranceId(i)%>"/>
	</TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" width="250" align="left"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenre(i))%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" width="120" align="center"><%=viewBean.getDateDebut(i)%></TD>
	<!--TD nowrap class="mtd" onClick="<=actionDetail>" width="120" align="center"><=viewBean.getDateFin(i)></TD-->
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" width="70" align="center"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getSexe(i))%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" width="150"  align="center"><%=viewBean.getValeur(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>