<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.db.notedecreditlier.LXNoteDeCreditLierViewBean"%>
<%@page import="globaz.lynx.db.notedecreditlier.LXNoteDeCreditLierListViewBean"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>

<%
LXNoteDeCreditLierListViewBean viewBean = (LXNoteDeCreditLierListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXNoteDeCreditLierViewBean noteCredit = null;

detailLink ="lynx?userAction=lynx.notedecreditlier.noteDeCreditLier.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    	
	
<%@page import="globaz.framework.util.FWCurrency"%><TH width="30">&nbsp;</TH>
	<TH width="100">Rechnung</TH>
	<TH width="400">Erstellungsdatum</TH>
    <TH width="200">Betrag</TH>
    
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
	    noteCredit = (LXNoteDeCreditLierViewBean) viewBean.getEntity(i);
	    String parametres = "&idOperation=" + noteCredit.getIdOperation() + "&idSociete=" + noteCredit.getIdSociete();
	    parametres += "&idFournisseur=" + noteCredit.getIdFournisseur() + "&idSection=" + noteCredit.getIdSection();
	    parametres += "&libelleNote=" + request.getParameter("libelleNote");
	    actionDetail = "parent.location.href='" + detailLink + noteCredit.getIdOperation() + parametres + "'";
	   
	    FWCurrency currency = new FWCurrency();
	    currency.add(noteCredit.getMontant());  
	    currency.negate();
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + noteCredit.getIdOperation()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=noteCredit.getIdExterneSectionLiee()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center"><%=noteCredit.getDateOperation()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="center"><%=currency.toStringFormat()%>&nbsp;</td>
	
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>