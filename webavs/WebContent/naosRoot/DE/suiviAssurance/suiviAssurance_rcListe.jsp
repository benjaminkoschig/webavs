<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.suiviAssurance.AFSuiviAssuranceListViewBean viewBean = (globaz.naos.db.suiviAssurance.AFSuiviAssuranceListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.suiviAssurance.suiviAssurance.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

	<TH width="30">&nbsp;</TH>
	<TH align="left" width="250">Versicherung</TH>
	<TH align="center" width="150">Status der Verfolgung</TH>
	<TH align="center" width="120">Vorgesehenes Datum</TH>
	<TH align="right" width="120">Effektives Datum</TH>
	<TH align="right" width="120">Enddatum</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% 	
		actionDetail = targetLocation + "='" + detailLink + viewBean.getSuiviAssuranceId(i) + "'";
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getSuiviAssuranceId(i)%>"/>
	</TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="left" width="250"><%=viewBean.getAssurance(i).getAssuranceLibelleCourt()%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="center" width="150"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getStatut(i))%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right" width="120"><%=viewBean.getDatePrevue(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right" width="120"><%=viewBean.getDateEffective(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right" width="120"><%=viewBean.getDateFin(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>