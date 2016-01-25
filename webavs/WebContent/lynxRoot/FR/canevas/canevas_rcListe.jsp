<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXCanevasListViewBean viewBean = (LXCanevasListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXCanevasViewBean canevas = null;

detailLink ="lynx?userAction=lynx.canevas.canevas.afficher&idOperationCanevas=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.lynx.db.canevas.LXCanevasViewBean"%>
<%@page import="globaz.lynx.db.canevas.LXCanevasListViewBean"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.lynx.db.canevas.LXCanevasOperation"%>
	
	
	<TH width="30">&nbsp;</TH>
	<TH width="400">Libell&eacute;</TH>
	<TH width="200">Fournisseur</TH>
	<TH width="200">No Can. Interne</TH>
	<TH width="200">No Can. Fournisseur</TH>
    <TH width="200">Montant</TH>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    canevas = (LXCanevasViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + canevas.getIdOperationCanevas() + "'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + canevas.getIdOperationCanevas()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=canevas.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=canevas.getIdExterneFournisseur() + " - " + canevas.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=canevas.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=canevas.getReferenceExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right"><%=JANumberFormatter.formatNoRound(canevas.getMontant())%>&nbsp;</td>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>