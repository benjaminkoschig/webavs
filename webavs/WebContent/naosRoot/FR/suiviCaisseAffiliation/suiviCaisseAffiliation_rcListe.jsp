<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.naos.db.suiviCaisseAffiliation.AFSuiviCaisseAffiliationListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.suiviCaisseAffiliation.suiviCaisseAffiliation.afficher&selectedId=";
	AFSuiviCaisseAffiliationListViewBean viewBean 
			= (AFSuiviCaisseAffiliationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%

	%>
	<TH width="30">&nbsp;</TH>
	<TH align="left" width="150">Genre de Caisse</TH>
	<TH align="left" width="100">N&deg; de caisse</TH>
	<TH align="center" width="100">Date d&eacute;but</TH>
	<TH align="center" width="100">Date fin</TH>
	<TH align="left" width="200">Motif non-soumis</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<% 	
		actionDetail = targetLocation + "='" + detailLink + viewBean.getSuiviCaisseId(i) + "'";
		String libelleGenre = globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenreCaisse(i));
		if(viewBean.getAccessoire(i)){
			libelleGenre += " (accessoire)";
		}
	%>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getSuiviCaisseId(i)%>"/>
	</TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="left" width="150"><%=libelleGenre%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right" width="100"><%=viewBean.getCodeAdministration(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDateDebut(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="center" width="100"><%=viewBean.getDateFin(i)%></TD>
	<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="left" width="200"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getMotif(i))%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>