<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXEscompteListViewBean viewBean = (LXEscompteListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXEscompteViewBean escompte = null;

detailLink ="lynx?userAction=lynx.escompte.escompte.afficher&idOperation=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	
<%@page import="globaz.lynx.db.escompte.LXEscompteListViewBean"%>
<%@page import="globaz.lynx.db.escompte.LXEscompteViewBean"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
	
	<TH width="30">&nbsp;</TH>
	<TH width="100">Date</TH>
	<TH width="400">Libell&eacute;</TH>
	<TH width="200">Fournisseur</TH>
	<TH width="200">No. Interne</TH>
	<TH width="200">No. Fact. Fournisseur</TH>
	<TH width="200">Montant</TH>
	<TH width="15">Bloqu&eacute;</TH>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    escompte = (LXEscompteViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='"+detailLink+escompte.getIdOperation()+"'";
    
    String style = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(escompte.getCsEtat())) {
    	style = "color:blue";
    } else if(LXOperation.CS_ETAT_ANNULE.equals(escompte.getCsEtat())) {
    	style = "color:gray";
    }
    
    String bloque = "";
    if(escompte.getEstBloque().booleanValue()) {
    	bloque = "<img title=\"Bloqué\" src=\"" + request.getContextPath()+"/images/cadenas.gif\"";
    }
    %>

    <td class="mtd" width="16">
    	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + escompte.getIdOperation()%>"/>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=escompte.getDateFacture()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=escompte.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=escompte.getIdExterneFournisseur() + " - " + escompte.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=escompte.getIdExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=escompte.getReferenceExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=style%>"><%=escompte.getMontantFormattedPositif()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=bloque%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>