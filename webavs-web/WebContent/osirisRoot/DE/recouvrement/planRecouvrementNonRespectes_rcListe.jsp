<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.recouvrement.CAPlanRecouvrementNonRespectesListViewBean"%>
<%@ page import="globaz.osiris.db.access.recouvrement.CAPlanRecouvrementNonRespectes"%>
<%
	CAPlanRecouvrementNonRespectesListViewBean viewBean = (CAPlanRecouvrementNonRespectesListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=";
	String previousLine="";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH>Abrechnungskonto</TH>
    <TH>Sektionen</TH>
    <TH>Zahlungsvereinbarung-Nr.</TH>
    <TH>Fälligkeit</TH>
    <TH>Betrag</TH>            
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		CAPlanRecouvrementNonRespectes line = (CAPlanRecouvrementNonRespectes) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPlanRecouvrement() + "'";
	%>
    <TD class="mtd" width="">
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink + line.getIdPlanRecouvrement())%>"/>
	</TD>

	<%if(previousLine.equals(line.getCompteAnnexe().getIdExterneRole()+line.getCouvertureListe())){%>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">&nbsp;</TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">&nbsp;</TD>		
	<% }else{ %>
		<%previousLine=line.getCompteAnnexe().getIdExterneRole()+line.getCouvertureListe();%>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCompteAnnexe().getIdExterneRole()+"&nbsp;"+line.getCompteAnnexe().getDescription()%></TD>
		<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getCouvertureListe()%></TD>
	<% } %>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%=line.getIdPlanRecouvrement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%=line.getDateExigibilite()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getMontant()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>