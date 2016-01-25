<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.fact.AFFactListViewBean viewBean = (globaz.naos.db.fact.AFFactListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.fact.factAffilie.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

		<TH width="30">&nbsp;</TH>
		<TH align="left" width="150">Cotisation</TH>
		<TH align="center" width="70">D&eacute;but</TH>
		<TH align="center" width="70">Fin</TH>
		<TH align="right" width="70">Masse</TH>
		<TH align="right" width="70">Montant</TH>
		<TH align="right" width="100">Type de facturation</TH>
		<TH align="left" width="70">Extourne</TH>
		<TH align="left" width="100">Id de passage</TH>
		<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%    
		 	actionDetail = targetLocation + "='" +detailLink + viewBean.getFacturationId(i)+ "'";   
		%>
		<TD class="mtd" width="30" >

		<ct:menuPopup menu="AFMenuVide" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=viewBean.getFacturationId(i)%>"/>

		</TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="left"><%=viewBean.getCotisation(i).getAssurance().getAssuranceLibelleCourt()%></TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="center"><%=viewBean.getDateDebut(i)%></TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getDateFin(i)%></TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getMassePeriodiciteNouveau(i)%></TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getMontant(i)%></TD>
		<TD nowrap class="mtd" onClick="<%=actionDetail%>" align="right"><%=viewBean.getTypeFacturation(i)%></TD>
		<% 
			if (viewBean.getExtourner(i).booleanValue()){
		%>
			<TD class="mtd" align="center"><IMG src="<%=request.getContextPath()%>/images/select2.gif" ></TD>
		<% } else { %>
			<TD class="mtd" align="center"><%=""%></TD>
		<% } %>
		<TD nowrap class="mtd" align="right"><%=viewBean.getPassageId(i)%></TD>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>