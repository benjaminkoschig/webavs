<%@ page import="globaz.naos.translation.CodeSystem" %><%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.assurance.assurance.afficher&selectedId=";
	menuName="Assurance";
	globaz.naos.db.assurance.AFAssuranceListViewBean viewBean = (globaz.naos.db.assurance.AFAssuranceListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		

		if (!(request.getParameter("colonneSelection") != null && request.getParameter("colonneSelection").equals("yes"))) {
	%>
    <TH width="30">&nbsp;</TH>
	<% } %>
	<TH nowrap width="350">Bezeichnung</TH>
	<TH width="150">Buchhaltungsrubrik</TH>
	<TH width="80">Kanton</TH>
	<TH width="120">Art</TH>
	<TH width="250">Hinweis</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getAssuranceId(i)+"'";
		
		// Pas de menu et pas de lien sur le Detail si on est en mode selection.
		if (request.getParameter("colonneSelection") != null && request.getParameter("colonneSelection").equals("yes")) {
			actionDetail = "";
		} else {
    %>
	<TD class="mtd" width="30" >
		<<ct:menuPopup menu="AFOptionsAssurances" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getAssuranceId(i)%>">
			<ct:menuParam key="assuranceId" value="<%=viewBean.getAssuranceId(i)%>"/>
		</ct:menuPopup>
	</TD>
	<% } %>
	<TD class="mtd" onClick="<%=actionDetail%>" width="350"><%=viewBean.getAssuranceLibelle(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="right" width="150"><%=viewBean.getRubriqueComptable(i).getIdExterne()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" align="left" width="80"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssuranceCanton(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="120"><%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssuranceGenre(i))%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="250"><%=viewBean.getAssuranceReferenceLibelle(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>