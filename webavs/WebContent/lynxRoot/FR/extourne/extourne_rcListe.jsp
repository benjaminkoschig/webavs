<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXExtourneListViewBean viewBean = (LXExtourneListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXExtourneViewBean extourne = null;

detailLink ="lynx?userAction=lynx.extourne.extourne.afficher&idOperation=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	
<%@page import="globaz.lynx.db.operation.LXOperation"%>
<%@page import="globaz.lynx.db.extourne.LXExtourneViewBean"%>
<%@page import="globaz.lynx.db.extourne.LXExtourneListViewBean"%>

	<TH width="30">&nbsp;</TH>
	<TH width="100">Date</TH>
	<TH width="400">Libell&eacute;</TH>
	<TH width="200">Fournisseur</TH>
	<TH width="150">No. Interne</TH>
	<TH width="200">No. Fact. Fournisseur</TH>
	<TH width="200">Montant</TH>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    extourne = (LXExtourneViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+extourne.getIdOperation()+"'";
    
    String style = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(extourne.getCsEtat())) {
    	style = "color:blue";
    } else if(LXOperation.CS_ETAT_ANNULE.equals(extourne.getCsEtat())) {
    	style = "color:gray";
    }
    %>

    <td class="mtd" width="16">
    	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + extourne.getIdOperation()%>"/>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=extourne.getDateFacture()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=extourne.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=extourne.getIdExterneFournisseur() + " - " + extourne.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=extourne.getIdExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=extourne.getReferenceExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=style%>"><%=extourne.getMontantFormatted()%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>