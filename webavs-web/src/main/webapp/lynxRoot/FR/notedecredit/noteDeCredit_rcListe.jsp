<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.lynx.db.notedecredit.LXNoteDeCreditViewBean"%>
<%@page import="globaz.lynx.db.notedecredit.LXNoteDeCreditListViewBean"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%
LXNoteDeCreditListViewBean viewBean = (LXNoteDeCreditListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXNoteDeCreditViewBean noteCredit = null;

detailLink ="lynx?userAction=lynx.notedecredit.noteDeCredit.afficher&idOperation=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    	
	<TH width="30">&nbsp;</TH>
	<TH width="100">Date</TH>
	<TH width="400">Libell&eacute;</TH>
	<TH width="200">Fournisseur</TH>
	<TH width="200">No Interne</TH>
	<TH width="200">No Fournisseur</TH>
    <TH width="200">Montant</TH>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    noteCredit = (LXNoteDeCreditViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + noteCredit.getIdOperation() + "'";
    
    String style = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(noteCredit.getCsEtat())) {
    	style = "color:blue";
    } else if(LXOperation.CS_ETAT_ANNULE.equals(noteCredit.getCsEtat())) {
    	style = "color:gray";
    }
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + noteCredit.getIdOperation()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=noteCredit.getDateFacture()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=noteCredit.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=noteCredit.getIdExterneFournisseur() + " - " + noteCredit.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=noteCredit.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=noteCredit.getReferenceExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=style%>"><%=JANumberFormatter.formatNoRound(noteCredit.getMontant())%>&nbsp;</td>
	
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>